package com.example.speedmarket.repository

import com.example.speedmarket.model.Carrello
import com.example.speedmarket.util.UiState

interface CarrelloRepository {
    fun getCarrello(result: (UiState<List<Carrello>>) -> Unit)
    fun addCarrello(carrello: Carrello, result: (UiState<String>) -> Unit)
    fun deleteCarrello(carrello: Carrello, result: (UiState<String>) -> Unit)
    fun updateCarrello(carrello: Carrello, result: (UiState<String>) -> Unit)
    //fun getCarrelliLocal(): LiveData<List<Carrello>>
}