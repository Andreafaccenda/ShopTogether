package com.example.speedmarket.model

import java.io.Serializable

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
    val offerta: Float?,
    var disponibilita : Int,
    var unita_ordinate : Int
) : Serializable
{
    constructor(): this("","","","","",0.0f,
        0.0f,"","",0.0f,0,0)
}

