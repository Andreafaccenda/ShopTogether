package com.example.speedmarket.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.speedmarket.database.DatabaseProdotto
import com.example.speedmarket.database.ProductsDatabase
import com.example.speedmarket.database.asDomainModel
import com.example.speedmarket.model.Prodotto
import com.example.speedmarket.model.Utente
import com.example.speedmarket.util.FireStoreCollection
import com.example.speedmarket.util.UiState
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class ProdRepositoryImp(
    private val database: FirebaseFirestore,
    application: Application): ProdRepository {

    private val productDao = ProductsDatabase.getInstance(application).prodottoDao() //db locale
    private val storage = FirebaseStorage.getInstance()

    // lista dei prodotti nel db locale
    private val products: LiveData<List<Prodotto>> = Transformations.map(productDao
    .getProdotti()) {
        it.asDomainModel()
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
                    productDao.insertProdotti(products)
                    result.invoke(
                        UiState.Success(products.asDomainModel())
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

    override fun addProduct(prodotto: Prodotto, result: (UiState<Pair<Prodotto, String>>) -> Unit) {
        val document = database.collection(FireStoreCollection.PRODOTTI).document()
        prodotto.id = document.id
        document
            .set(prodotto)
            .addOnSuccessListener {
                result.invoke(
                    UiState.Success(Pair(prodotto, "Prodotto registrato con successo!")))
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure(it.localizedMessage))
            }
    }

    override fun deleteProduct(prodotto: Prodotto, result: (UiState<String>) -> Unit) {
        database.collection(FireStoreCollection.PRODOTTI).document(prodotto.id)
            .delete()
            .addOnSuccessListener {
     //           productDao.delete(product) // rimuove il prodotto dal db locale
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
   //             productDao.update(prodotto) //aggiorna la copia nel db locale
                result.invoke(UiState.Success("Note has been update successfully"))
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure(it.localizedMessage))
            }
    }

}
