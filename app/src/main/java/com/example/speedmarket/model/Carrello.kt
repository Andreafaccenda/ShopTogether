package com.example.speedmarket.model

data class Carrello(
    var id: String,
    var utente: Utente?,
    val lista_prodotti: MutableList<Prodotto>? = arrayListOf(),
    val prezzo: Float
)