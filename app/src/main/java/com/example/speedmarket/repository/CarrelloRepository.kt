package com.example.speedmarket.repository

import androidx.lifecycle.LiveData
import com.example.speedmarket.database.DaoCarrello
import com.example.speedmarket.database.DatabaseCarrello
import com.example.speedmarket.model.Carrello
import com.example.speedmarket.model.Utente
import com.example.speedmarket.util.UiState

interface CarrelloRepository {
    fun getCarrello(utente: Utente?, result: (UiState<Carrello>) -> Unit)
    fun deleteCarrello(carrello: Carrello, result: (UiState<String>) -> Unit)
    fun updateCarrello(carrello: Carrello, result: (UiState<String>) -> Unit)
    fun getCarrelloLocal(id: String): LiveData<DatabaseCarrello>
    fun getListaCarrelliLocal(): LiveData<List<Carrello>>
}