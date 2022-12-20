package com.example.speedmarket.repository

import androidx.lifecycle.LiveData
import com.example.speedmarket.model.Prodotto
import com.example.speedmarket.model.Utente
import com.example.speedmarket.util.UiState

interface ProdRepository {
    fun getProducts(result: (UiState<List<Prodotto>>) -> Unit)
    fun addProduct(prodotto: Prodotto, result: (UiState<Pair<Prodotto,String>>) -> Unit)
    fun deleteProduct(prodotto: Prodotto, result: (UiState<String>) -> Unit)
    fun updateProduct(prodotto: Prodotto, result: (UiState<String>) -> Unit)
    fun getProductsLocal(): LiveData<List<Prodotto>>
    //...
}