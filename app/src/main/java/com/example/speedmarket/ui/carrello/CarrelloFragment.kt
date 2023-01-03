package com.example.speedmarket.ui.carrello

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.speedmarket.R
import com.example.speedmarket.databinding.FragmentCarrelloBinding
import com.example.speedmarket.model.Carrello
import com.example.speedmarket.model.Prodotto
import com.example.speedmarket.model.Utente
import com.example.speedmarket.ui.auth.AuthViewModel
import com.example.speedmarket.ui.catalogo.CatalogoFragment
import com.example.speedmarket.util.*
import dagger.hilt.android.AndroidEntryPoint
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.properties.Delegates

@AndroidEntryPoint
class CarrelloFragment : Fragment() {

    lateinit var binding: FragmentCarrelloBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var utente: Utente
    private lateinit var prodotto: Prodotto
    private var inizializzato by Delegates.notNull<Boolean>()
    val viewModelAuth: AuthViewModel by viewModels()
    val viewModelCarrello: CarrelloViewModel by viewModels()
    private val adapter by lazy { CarrelloAdapter() }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCarrelloBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnBackPressed()
        binding.txtSpedizione.setOnClickListener{
            toast("la spedizione ha un prezzo di €5 per ordini inferiori ai €50")
        }
        getUtente()
        oberver()
        viewModelCarrello.getCarrello(utente)
        val args = this.arguments
        if (args.toString() == "null") {
            inizializzato = false
        } else {
            prodotto = args?.getSerializable("prodotto") as Prodotto
            inizializzato = true
        }


        recyclerView = binding.recyclerViewCarrello
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        binding.recyclerViewCarrello.adapter = adapter

    }


    private fun oberver() {
        viewModelCarrello.carrello.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                }
                is UiState.Failure -> {
                    toast(state.error)
                }
                is UiState.Success -> {
                    for (carrello in state.data.toMutableList()) {
                        if (!carrello.ordine_completato) {
                            for (prodotto in carrello.lista_prodotti!!.iterator()) {
                                carrello.prezzo += prodotto.prezzo_unitario * prodotto.offerta!! * prodotto.quantita * prodotto.unita_ordinate
                                binding.txtPrezzoCarrello.text="${calcolaPrezzo(carrello.prezzo)}€"
                                binding.txtTotaleSpesaCarrello.text="${calcolaPrezzo(carrello.prezzo+5)}€"
                                binding.txtPrezzoIva.text="${calcolaPrezzo((carrello.prezzo*22)/100)}€"
                            }
                            if (inizializzato) {
                                carrello.lista_prodotti?.add(prodotto)
                                viewModelCarrello.updateCarrello(carrello)
                            }
                            carrello.lista_prodotti?.let { adapter.updateList(it) }
                        }
                    }
                }
            }
        }
    }

    fun getUtente() {
        viewModelAuth.getSession { user ->
            if (user != null) {
                utente = user
            }
        }
    }


    fun calcolaPrezzo(prezzo_totale: Float): String {
        val dec = DecimalFormat("#.##")
        dec.roundingMode = RoundingMode.DOWN
        val prezzo = dec.format(prezzo_totale)
        return prezzo
    }


}