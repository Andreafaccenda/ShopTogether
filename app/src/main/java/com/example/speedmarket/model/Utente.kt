package com.example.speedmarket.model

data class Utente(
    var id: String = "",
    val nome: String = "",
    val cognome: String = "",
    val email: String = "",
    val password: String = "",
    var immagine_profilo: String = "",
    var residenza: Indirizzo = Indirizzo("","","","",""),
    var indirizzo_spedizione: Indirizzo = Indirizzo("","","","",""),
    var pagamento: Pagamento = Pagamento("",""),
    var lista_carrelli: MutableList<Carrello>? = arrayListOf(),
    var numero_telefono: String = "",
    var genere: String = "",
    var profileCompleted: Boolean = false){


}