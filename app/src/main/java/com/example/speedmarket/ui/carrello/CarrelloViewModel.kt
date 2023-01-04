package com.example.speedmarket.ui.carrello

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.speedmarket.model.Carrello
import androidx.lifecycle.LiveData
import com.example.speedmarket.model.Utente
import com.example.speedmarket.repository.CarrelloRepository
import com.example.speedmarket.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CarrelloViewModel @Inject constructor(
    private val repository: CarrelloRepository
): ViewModel() {

    //val carrelliLocal = repository.getCarrelliLocal()

    private val _carrello = MutableLiveData<UiState<Carrello>>()
    val carrello: LiveData<UiState<Carrello>>
        get() = _carrello

    private val _updateCarrello = MutableLiveData<UiState<String>>()
    val updateCarrello: LiveData<UiState<String>>
        get() = _updateCarrello


    fun updateCarrello(carrello: Carrello){
        _updateCarrello.value = UiState.Loading
        repository.updateCarrello(carrello) { _updateCarrello.value = it }
    }

  fun getCarrello(utente: Utente) {
        _carrello.value = UiState.Loading
        repository.getCarrello(utente) { _carrello.value = it }
    }


}