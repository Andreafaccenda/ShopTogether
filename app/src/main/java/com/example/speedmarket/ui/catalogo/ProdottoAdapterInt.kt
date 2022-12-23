package com.example.speedmarket.ui.catalogo

import com.example.speedmarket.model.Prodotto

interface ProdottoAdapterInt {
    fun updateList(list: MutableList<Prodotto>)
    fun filtraListaCategoria(tipo: String, list: MutableList<Prodotto>)
    fun filtraListaNomeChange(tipo: String, list: MutableList<Prodotto>)
    fun filtraListaNome(tipo: String, list: MutableList<Prodotto>)
    fun removeItem(id: String)

}
