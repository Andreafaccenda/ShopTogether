package com.example.speedmarket.model

import java.io.Serializable

data class Prodotto(
    var id: String,
    var nome: String,
    var produttore: String,
    var categoria: String,
    var sub_categoria:String,
    var immagine: String,
    var prezzo_unitario: Float,
    var quantita: Float,
    var descrizione: String,
    var data_scadenza: String,
    var offerta: Float?,
    var disponibilita : Int,
    var unita_ordinate : Int
) : Serializable
{
    constructor(): this("","","","","","",
        0.0f, 0.0f,"","",
        0.0f,0,0)
}






