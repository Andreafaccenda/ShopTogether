package com.example.speedmarket.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.speedmarket.database.DatabaseProdotto
import com.example.speedmarket.database.ProductsDatabase
import com.example.speedmarket.database.asDomainModelProdotto
import com.example.speedmarket.database.toDatabaseProdotto
import com.example.speedmarket.model.Prodotto
import com.example.speedmarket.util.FireStoreCollection
import com.example.speedmarket.util.UiState
import com.google.firebase.firestore.FirebaseFirestore

class ProdRepositoryImp(
    private val database: FirebaseFirestore,
    application: Application): ProdRepository {

    private val productDao = ProductsDatabase.getInstance(application).prodottoDao() //db locale

    // lista dei prodotti nel db locale
    private val products: LiveData<List<Prodotto>> = Transformations.map(productDao
    .getProdotti()) {
        it.asDomainModelProdotto()
    }

    override fun getProducts(result: (UiState<List<Prodotto>>) -> Unit){
        val db = database.collection(FireStoreCollection.PRODOTTI)
        db.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val products = arrayListOf<DatabaseProdotto>()
                    for (field in document) {
                        val product = field.toObject(DatabaseProdotto::class.java)
                        products.add(product)
                    }
                    result.invoke(
                        UiState.Success(products.asDomainModelProdotto())
                    )
                }
                else {
                    result.invoke(UiState.Failure("No such document"))
                }
            }.addOnFailureListener {
                result.invoke(UiState.Failure(
                    it.localizedMessage))
            }
    }

    override fun getProductsLocal(): LiveData<List<Prodotto>> {
        return products
    }

    override fun addProduct(prodotto: Prodotto, result: (UiState<String>) -> Unit) {
        val document = database.collection(FireStoreCollection.PRODOTTI).document(prodotto.id)
        document
            .set(prodotto)
            .addOnSuccessListener {
                productDao.insertProdotti(prodotto.toDatabaseProdotto())
                result.invoke(
                    UiState.Success( "Prodotto registrato con successo!"))
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure(it.localizedMessage))
            }
    }

    override fun deleteProduct(prodotto: Prodotto, result: (UiState<String>) -> Unit) {
        database.collection(FireStoreCollection.PRODOTTI).document(prodotto.id)
            .delete()
            .addOnSuccessListener {
                productDao.delete(prodotto.toDatabaseProdotto())
                result.invoke(UiState.Success("Prodotto rimosso con successo!"))
            }
            .addOnFailureListener { error ->
                result.invoke(UiState.Failure(error.message))
            }
    }

    override fun updateProduct(prodotto: Prodotto, result: (UiState<String>) -> Unit) {
        val document = database.collection(FireStoreCollection.PRODOTTI).document(prodotto.id)
        document
            .set(prodotto)
            .addOnSuccessListener {
                productDao.update(prodotto.toDatabaseProdotto())
                result.invoke(UiState.Success("Prodotto aggiornato con successo!"))
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure(it.localizedMessage))
            }
    }

}
