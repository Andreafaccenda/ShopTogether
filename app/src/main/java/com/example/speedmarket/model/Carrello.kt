package com.example.speedmarket.model

data class Carrello(
    var id: String,
    var lista_prodotti: MutableList<Prodotto>? = arrayListOf(),
    var prezzo: String,
    var ordine_completato:Boolean) {
    constructor(): this("",null,"",false)

}
