package com.example.speedmarket.database

import androidx.room.*
import com.example.speedmarket.model.Carrello
import com.example.speedmarket.model.Prodotto


@Entity
data class DatabaseProdotto(
    @PrimaryKey
    val id: String,
    val nome: String,
    val produttore: String,
    val categoria: String,
    val sub_categoria :String,
    val immagine: String,
    val prezzo_unitario: Float,
    val quantita: Float,
    val descrizione: String,
    val data_scadenza: String,
    val offerta: Float?,
    val disponibilita : Int,
    val unita_ordinate : Int) {
    constructor(): this("","","","","","",0.0f,
    0.0f,"","",0.0f,0,0)
}

@Entity
data class DatabaseCarrello(
    @PrimaryKey
    val id: String,
    val lista_prodotti: MutableList<Prodotto>?,
    val prezzo: String,
    var stato:Carrello.Stato) {
    constructor(): this("",null,"", Carrello.Stato.incompleto)
}

fun List<DatabaseProdotto>.asDomainModelProdotto(): List<Prodotto> {
    return map {
        Prodotto(
            id = it.id,
            nome = it.nome,
            produttore = it.produttore,
            categoria = it.categoria,
            sub_categoria = it.sub_categoria,
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

fun Prodotto.toDatabaseProdotto() = DatabaseProdotto(id=id, nome=nome, produttore=produttore,
    categoria=categoria,sub_categoria=sub_categoria, immagine=immagine, prezzo_unitario=prezzo_unitario, quantita=quantita,
    descrizione=descrizione, data_scadenza=data_scadenza, offerta=offerta,
    disponibilita=disponibilita, unita_ordinate=disponibilita)

fun List<DatabaseCarrello>.asDomainModelCarrello(): List<Carrello> {
    return map {
        Carrello(
            id = it.id,
            lista_prodotti = it.lista_prodotti,
            prezzo = it.prezzo,
            stato = it.stato
        )
    }
}

fun DatabaseCarrello.toCarrello() = Carrello(
    id=id,
    lista_prodotti=lista_prodotti,
    prezzo=prezzo,
    stato = stato
)

fun Carrello.toDatabaseCarrello() = DatabaseCarrello(
    id=id,
    lista_prodotti=lista_prodotti,
    prezzo=prezzo,
    stato =stato
)
