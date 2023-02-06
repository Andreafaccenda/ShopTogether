package com.example.speedmarket.model

data class Indirizzo(
    var citta : String,
    var provincia : String,
    var cap : String,
    var via : String,
    var numero_civico : String){

    constructor(): this("","","","","")
}


