package com.example.speedmarket.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.speedmarket.model.Carrello
import com.example.speedmarket.model.Utente
import com.example.speedmarket.util.FireStoreCollection
import com.example.speedmarket.util.FireStoreDocumentField
import com.example.speedmarket.util.UiState
import com.google.firebase.firestore.FirebaseFirestore

class CarrelloRepositoryImp(
    private val database: FirebaseFirestore,
    application: Application): CarrelloRepository {

    /*private val carrelloDao = CarrelloDatabase.getInstance(application).carrelloDao() //db locale

    // lista carrelli db locale
    private val carrelli: LiveData<List<Carrello>> = Transformations.map(carrelloDao
        .getCarrello()) {
        it.asDomainModelCarrello()
    }*/

    override fun getCarrello(utente: Utente?, result: (UiState<Carrello>) -> Unit) {
        database.collection(FireStoreCollection.CARRELLI).whereEqualTo(FireStoreDocumentField.ID,utente?.id)
            .get()
               .addOnSuccessListener {
                   /*val carrelli = arrayListOf<Carrello>()
                   for (document in it) {
                       val carrello = document.toObject(Carrello::class.java)
                       carelli.add(carrello)
                       carelli.size.toString()*/
                   var carrello = Carrello()
                   for (document in it) {
                       carrello = document.toObject(Carrello::class.java)
                   }
                   result.invoke(
                       UiState.Success(carrello)
                   )

               }.addOnFailureListener {
                   result.invoke(UiState.Failure(
                       it.localizedMessage))
               }
       }

    override fun addCarrello(
        carrello: Carrello,
        result: (UiState<String>) -> Unit
    ) {
        val document = database.collection(FireStoreCollection.CARRELLI).document()
        carrello.id = document.id
        document
            .set(carrello)
            .addOnSuccessListener {
                /**   convertire prodotto da Prodotto a DatabaseProdotto
                 *   productDao.update(prodotto)
                 */
                result.invoke(
                    UiState.Success("Carrello registrato con successo!"))
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
        val document = database.collection(FireStoreCollection.CARRELLI).document(carrello.id)
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
        TODO("Not yet implemented")
    }


    /* override fun getCarrelliLocal(): LiveData<List<Carrello>> {
          return carrelli
      }*/
}