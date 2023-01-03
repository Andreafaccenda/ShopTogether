package com.example.speedmarket.database

import androidx.room.*
import com.example.speedmarket.model.Carrello
import com.example.speedmarket.model.Prodotto
import com.google.gson.Gson


@Entity
data class DatabaseProdotto(
    @PrimaryKey
    val id: String,
    val nome: String,
    val produttore: String,
    val categoria: String,
    val immagine: String,
    val prezzo_unitario: Float,
    val quantita: Float,
    val descrizione: String,
    val data_scadenza: String,
    val offerta: Float?,
    val disponibilita : Int,
    val unita_ordinate : Int) {
    constructor(): this("","","","","",0.0f,
    0.0f,"","",0.0f,0,0)
}

@Entity
data class DatabaseCarrello(
    @PrimaryKey
    val id: String,
    val lista_prodotti: MutableList<Prodotto>?,
    val prezzo: Float,
    val ordine_completato:Boolean) {
    constructor(): this("",null,0.0f,false)
}

fun List<DatabaseProdotto>.asDomainModelProdotto(): List<Prodotto> {
    return map {
        Prodotto(
            id = it.id,
            nome = it.nome,
            produttore = it.produttore,
            categoria = it.categoria,
            immagine = it.immagine,
            prezzo_unitario = it.prezzo_unitario,
            quantita = it.quantita,
            descrizione = it.descrizione,
            data_scadenza = it.data_scadenza,
            offerta = it.offerta,
            disponibilita =it.disponibilita,
            unita_ordinate=it.unita_ordinate
        )
    }
}

fun List<DatabaseCarrello>.asDomainModelCarrello(): List<Carrello> {
    return map {
        Carrello(
            id = it.id,
            lista_prodotti = it.lista_prodotti,
            prezzo = it.prezzo,
            ordine_completato=it.ordine_completato
        )
    }
}
