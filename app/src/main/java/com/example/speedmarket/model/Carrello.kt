package com.example.speedmarket.model

data class Carrello(
    var id: String,
    var lista_prodotti: MutableList<Prodotto>? = arrayListOf(),
    var prezzo: String,
    var date :String,
    var stato : Stato) {
    constructor(): this("",null,"","",stato=Stato.incompleto)

    enum class Stato{consegnato,spedizione,elaborazione,incompleto}
}
