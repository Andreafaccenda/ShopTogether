package com.example.speedmarket.model

data class Prodotto(
    var id: String,
    val nome: String,
    val produttore: String,
    val categoria: String,
    val immagine: String,
    val prezzo_unitario: Float,
    val quantita: Float,
    val descrizione: String,
    val data_scadenza: String,
    val offerta: Float?
)
