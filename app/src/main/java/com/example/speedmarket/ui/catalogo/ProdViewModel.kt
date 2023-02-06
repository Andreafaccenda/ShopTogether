package com.example.speedmarket.ui.catalogo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.speedmarket.model.Prodotto
import com.example.speedmarket.repository.ProdRepository
import com.example.speedmarket.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProdViewModel @Inject constructor(
    private val repository: ProdRepository
): ViewModel() {

    val prodottiLocal = repository.getProductsLocal()

    private val _prodotto = MutableLiveData<UiState<List<Prodotto>>>()
    val prodotto: LiveData<UiState<List<Prodotto>>>
        get() = _prodotto

    private val _updateProdotto = MutableLiveData<UiState<String>>()

    private val _addProduct = MutableLiveData<UiState<String>>()
    val addProduct: LiveData<UiState<String>>
        get() = _addProduct


    fun getProducts() {
        _prodotto.value = UiState.Loading
        repository.getProducts() { _prodotto.value = it }
    }
    fun updateProduct(prodotto: Prodotto){
        _updateProdotto.value = UiState.Loading
        repository.updateProduct(prodotto) { _updateProdotto.value = it }
    }
    fun addProduct(prodotto: Prodotto){
        _addProduct.value = UiState.Loading
        repository.addProduct(prodotto) { _addProduct.value = it }
    }


}