package com.example.speedmarket.ui.catalogo

import com.example.speedmarket.model.Prodotto

interface ProdottoAdapterInt {
    fun updateList(list: MutableList<Prodotto>)
    fun filtraLista_categoria(tipo: String, list: MutableList<Prodotto>)
    fun filtraLista_nome_change(tipo: String, list: MutableList<Prodotto>)
    fun filtraLista_nome(tipo: String, list: MutableList<Prodotto>)

}
