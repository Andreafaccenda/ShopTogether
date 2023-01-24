package com.example.speedmarket.ui.catalogo

import com.example.speedmarket.model.Prodotto

interface ProdottoAdapterInt {
    fun updateList(list: MutableList<Prodotto>)
    fun filtraListaCategoria(tipo: String, list: MutableList<Prodotto>)
    fun filtraListaNomeChange(tipo: String, list: MutableList<Prodotto>)
    fun filtraListaNome(tipo: String, list: MutableList<Prodotto>)
    fun filtraListaPrezzo(prezzo: String, list: MutableList<Prodotto>)
    fun filtraListaMarchio(marchio: String, list: MutableList<Prodotto>)
    fun filtraListaSottoCategoria(subCategoria: String, list: MutableList<Prodotto>)
    fun filtraListaMarchioPrezzo(prezzo:String,marchio: String, list: MutableList<Prodotto>)
    fun filtraListaCategoriaPrezzo(prezzo:String,subCategoria: String, list: MutableList<Prodotto>)
    fun filtraListaCategoriaMarchio(marchio:String,subCategoria: String, list: MutableList<Prodotto>)
    fun filtraLista(prezzo: String,marchio:String,subCategoria: String, list: MutableList<Prodotto>)
    fun filtraListaOfferta(list: MutableList<Prodotto>)
    fun filtraListaqrCode(qrCode: String, list: MutableList<Prodotto>)
    fun removeItem(id: String)

}
