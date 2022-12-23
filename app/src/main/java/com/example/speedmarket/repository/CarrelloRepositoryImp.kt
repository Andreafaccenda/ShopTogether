package com.example.speedmarket.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.speedmarket.database.CarrelloDatabase
import com.example.speedmarket.database.DatabaseCarrello
import com.example.speedmarket.database.asDomainModelCarrello
import com.example.speedmarket.model.Carrello
import com.example.speedmarket.util.FireStoreCollection
import com.example.speedmarket.util.UiState
import com.google.firebase.firestore.FirebaseFirestore

class CarrelloRepositoryImp(
    private val database: FirebaseFirestore,
    application: Application): CarrelloRepository {

    private val carrelloDao = CarrelloDatabase.getInstance(application).carrelloDao() //db locale

    // lista carrelli db locale
    private val carrelli: LiveData<List<Carrello>> = Transformations.map(carrelloDao
        .getListaCarrelli()) {
        it.asDomainModelCarrello()
    }

    override fun getListaCarrelli(result: (UiState<List<Carrello>>) -> Unit) {
        val db = database.collection(FireStoreCollection.CARRELLI)
        db.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val carrelli = arrayListOf<DatabaseCarrello>()
                    for (field in document) {
                        val carrello = field.toObject(DatabaseCarrello::class.java)
                        carrelli.add(carrello)
                    }
                    carrelloDao.insertListaCarrelli(carrelli)
                    result.invoke(
                        UiState.Success(carrelli.asDomainModelCarrello())
                    )
                }
            }
    }

    override fun addCarrello(
        carrello: Carrello,
        result: (UiState<Pair<Carrello, String>>) -> Unit
    ) {
        val document = database.collection(FireStoreCollection.CARRELLI).document()
        carrello.id = document.id
        document
            .set(carrello)
            .addOnSuccessListener {
                result.invoke(
                    UiState.Success(Pair(carrello, "Carrello registrato con successo!")))
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure(it.localizedMessage))
            }
    }

    override fun deleteCarrello(carrello: Carrello, result: (UiState<String>) -> Unit) {
        database.collection(FireStoreCollection.CARRELLI).document(carrello.id)
            .delete()
            .addOnSuccessListener {
                /** convertire carrello da Carrello a DatabaseCarrello
                 *  carrelloDao.delete(carrello)
                 */
                result.invoke(UiState.Success("Carrello rimosso con successo!"))
            }
            .addOnFailureListener { error ->
                result.invoke(UiState.Failure(error.message))
            }
    }

    override fun updateCarrello(carrello: Carrello, result: (UiState<String>) -> Unit) {
        val document = database.collection(FireStoreCollection.PRODOTTI).document(carrello.id)
        document
            .set(carrello)
            .addOnSuccessListener {
                /** convertire carrello da Carrello a DatabaseCarrello
                 *  carrelloDao.update(carrello)
                 */
                result.invoke(UiState.Success("Carrello aggiornato con successo!"))
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure(it.localizedMessage))
            }
    }

    override fun getCarrelliLocal(): LiveData<List<Carrello>> {
        return carrelli
    }
}