package com.example.speedmarket.ui.carrello

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.speedmarket.model.Carrello
import androidx.lifecycle.LiveData
import com.example.speedmarket.repository.CarrelloRepository
import com.example.speedmarket.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CarrelloViewModel @Inject constructor(
    private val repository: CarrelloRepository
): ViewModel() {

    val carrelliLocal = repository.getCarrelliLocal()

    private val _carrelli = MutableLiveData<UiState<List<Carrello>>>()
    val carrelli: LiveData<UiState<List<Carrello>>>
        get() = _carrelli

    fun getListaCarrelli() {
        _carrelli.value = UiState.Loading
        repository.getListaCarrelli { _carrelli.value = it }
    }
}