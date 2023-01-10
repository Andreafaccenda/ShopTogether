package com.example.speedmarket.model

data class Pagamento(var numero_carta: String,
                     var data_scadenza: String){
    constructor(): this("","")
}
