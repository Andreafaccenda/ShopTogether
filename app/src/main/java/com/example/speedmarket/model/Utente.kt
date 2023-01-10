package com.example.speedmarket.model

import android.net.Uri

data class Utente(
    var id: String = "",
    val nome: String = "",
    val cognome: String = "",
    val email: String = "",
    val password: String = "",
    var immagine_profilo: String = "",
    var residenza: String = "",
    var indirizzo_spedizione: String = "",
    var numero_telefono: Long = 0,
    var genere: String = "",
    val profileCompleted: Boolean = false)