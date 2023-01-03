package com.example.speedmarket.repository

import androidx.lifecycle.LiveData
import com.example.speedmarket.model.Carrello
import com.example.speedmarket.model.Utente
import com.example.speedmarket.util.UiState

interface CarrelloRepository {
    fun getCarrello(utente: Utente?, result: (UiState<List<Carrello>>) -> Unit)
    fun addCarrello(carrello: Carrello, result: (UiState<String>) -> Unit)
    fun deleteCarrello(carrello: Carrello, result: (UiState<String>) -> Unit)
    fun updateCarrello(carrello: Carrello, result: (UiState<String>) -> Unit)
    fun getCarrelliLocal(): LiveData<List<Carrello>>
}