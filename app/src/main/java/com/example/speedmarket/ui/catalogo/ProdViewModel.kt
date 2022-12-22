package com.example.speedmarket.ui.catalogo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.speedmarket.model.Prodotto
import com.example.speedmarket.model.Utente
import com.example.speedmarket.repository.ProdRepository
import com.example.speedmarket.repository.ProdRepositoryImp
import com.example.speedmarket.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProdViewModel @Inject constructor(
    private val repository: ProdRepository
): ViewModel() {

    private val products = repository.getProductsLocal()

    private val _prodotto = MutableLiveData<UiState<List<Prodotto>>>()
    val prodotto: LiveData<UiState<List<Prodotto>>>
        get() = _prodotto

    fun getProducts() {
        _prodotto.value = UiState.Loading
        repository.getProducts() { _prodotto.value = it }
    }

}