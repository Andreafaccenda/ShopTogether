package com.example.speedmarket.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.speedmarket.model.Prodotto


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
    val offerta: Float?)  {
    constructor() : this("","","","","",0.0f,
    0.0f,"","",0.0f)
}


fun List<DatabaseProdotto>.asDomainModel(): List<Prodotto> {
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
            offerta = it.offerta
        )
    }
}