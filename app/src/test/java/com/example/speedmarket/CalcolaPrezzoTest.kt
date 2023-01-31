package com.example.speedmarket

import com.example.speedmarket.model.Carrello
import com.example.speedmarket.model.Prodotto
import com.example.speedmarket.ui.carrello.CarrelloFragment
import junit.framework.Assert.assertEquals
import org.junit.Test

class CalcolaPrezzoTest {

    @Test
    fun prezzoTotaleCarrello() {
        val prodotto1 = Prodotto(id="abcd",nome="prodotto1", produttore = "", categoria = "",
            sub_categoria = "", immagine = "", prezzo_unitario = 2.5f, quantita = 0.5f, descrizione = "",
            data_scadenza = "", offerta = 1f, disponibilita = 10, unita_ordinate = 2)
        val prezzo1 = prodotto1.quantita * prodotto1.unita_ordinate * prodotto1.offerta!! * prodotto1.prezzo_unitario
        val prodotto2 = Prodotto(id="efgh",nome="prodotto2", produttore = "", categoria = "",
            sub_categoria = "", immagine = "", prezzo_unitario = 1.5f, quantita = 0.5f, descrizione = "",
            data_scadenza = "", offerta = 0.9f, disponibilita = 5, unita_ordinate = 3)
        val prezzo2 = prodotto2.quantita * prodotto2.unita_ordinate * prodotto2.offerta!! * prodotto2.prezzo_unitario
        val carrello = Carrello(id = "jklm", lista_prodotti = mutableListOf(prodotto1, prodotto2),
            prezzo = "", date = "", stato = Carrello.Stato.incompleto, indirizzoSpedizione = null,
            pagamento = null)

        setPrezzoCarrello(carrello)
        val prezzoTotale = prezzo1 + prezzo2 + 5
        val prezzoCarrello = carrello.prezzo.replace(",",".")
        assertEquals(prezzoCarrello.toFloat(), prezzoTotale, 0.01f)
    }

    /**
     * Funzione updatePriceCart in CarrelloFragment ma senza il riempimento delle TextView
     */
    private fun setPrezzoCarrello(carrello: Carrello) {
        val carrelloFragment = CarrelloFragment()
        var prezzo = 0.0F
        if (!carrello.lista_prodotti.isNullOrEmpty()) {
            for (elem in carrello.lista_prodotti!!)
                prezzo += (elem.quantita * elem.unita_ordinate * elem.offerta!! * elem.prezzo_unitario)
        }
        carrello.prezzo = carrelloFragment.calcolaPrezzo(prezzo+5)
    }
}