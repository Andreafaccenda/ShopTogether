package com.example.speedmarket.model

class Utente(
    val id: String = "",
    val nome: String = "",
    val cognome: String = "",
    val email: String = "",
    val password: String = "",
    val immagine_profilo: String = "",
    val residenza: String = "",
    val numero_telefono: Long = 0,
    val genere: String = "",
    val profileCompleted: Boolean = false)