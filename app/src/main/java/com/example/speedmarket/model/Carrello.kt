package com.example.speedmarket.model
import java.io.Serializable

data class Carrello(
    var id: String,
    var lista_prodotti: MutableList<Prodotto>? = arrayListOf(),
    var prezzo: String,
    var date:String,
    var stato: Stato,
    var indirizzoSpedizione: Indirizzo?,
    var pagamento: Pagamento?
) : Serializable {
    constructor(): this("",null,"","",
        stato =Stato.incompleto,null,null)

    enum class Stato{consegnato, spedizione, elaborazione, incompleto}
}






