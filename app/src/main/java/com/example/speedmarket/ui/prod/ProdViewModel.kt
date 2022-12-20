package com.example.speedmarket.ui.prod

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

    private val _notes = MutableLiveData<UiState<List<Prodotto>>>()
    val note: LiveData<UiState<List<Prodotto>>>
        get() = _notes

    private val _addProduct = MutableLiveData<UiState<Pair<Prodotto,String>>>()
    val addNote: LiveData<UiState<Pair<Prodotto, String>>>
        get() = _addProduct

    private val _updateProduct = MutableLiveData<UiState<String>>()
    val updateNote: LiveData<UiState<String>>
        get() = _updateProduct

    private val _deleteProduct = MutableLiveData<UiState<String>>()
    val deleteNote: LiveData<UiState<String>>
        get() = _deleteProduct

    fun getProducts() {
        _notes.value = UiState.Loading
        repository.getProducts() { _notes.value = it }
    }

    fun addProduct(prodotto: Prodotto){
        _addProduct.value = UiState.Loading
        repository.addProduct(prodotto) { _addProduct.value = it }
    }

    fun updateProduct(prodotto: Prodotto){
        _updateProduct.value = UiState.Loading
        repository.updateProduct(prodotto) { _updateProduct.value = it }
    }

    fun deleteProduct(prodotto: Prodotto){
        _deleteProduct.value = UiState.Loading
        repository.deleteProduct(prodotto) { _deleteProduct.value = it }
    }

}