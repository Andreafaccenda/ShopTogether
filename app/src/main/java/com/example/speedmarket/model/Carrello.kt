package com.example.speedmarket.model

class Carrello(
    var utente: Utente,
    val lista_prodotti: MutableList<Prodotto>,
    val prezzo: Float
) {
}