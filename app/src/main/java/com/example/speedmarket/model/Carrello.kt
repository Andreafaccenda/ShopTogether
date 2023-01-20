package com.example.speedmarket.model

import java.io.Serializable

data class Carrello(
    var id: String,
    var lista_prodotti: MutableList<Prodotto>? = arrayListOf(),
    var prezzo: String,
    var date :String,
    var stato : Stato) : Serializable {
    constructor(): this("",null,"","",stato=Stato.incompleto)

    enum class Stato{consegnato,spedizione,elaborazione,incompleto}
}
