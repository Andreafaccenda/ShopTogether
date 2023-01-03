package com.example.speedmarket.ui.carrello

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.speedmarket.R
import com.example.speedmarket.databinding.FragmentCarrelloBinding
import com.example.speedmarket.model.Carrello
import com.example.speedmarket.model.Prodotto
import com.example.speedmarket.model.Utente
import com.example.speedmarket.ui.auth.AuthViewModel
import com.example.speedmarket.util.UiState
import com.example.speedmarket.util.setupOnBackPressed
import com.example.speedmarket.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CarrelloFragment : Fragment() {

    lateinit var binding: FragmentCarrelloBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var utente: Utente
    private lateinit var carrelli_utente: MutableList<Carrello>
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
        getUtente()
        oberver()
        viewModelCarrello.getCarrello(utente)

        val args = this.arguments
        if(args.toString() == "null"){
                binding.txtTitle.text=getString(R.string.carrello_vuoto)
        }else{
            var prodotto : Prodotto = args?.getSerializable("prodotto") as Prodotto
          /* for(carrello in carrelli_utente){
                if(!carrello.ordine_completato){
                    //carrello.lista_prodotti?.add(prodotto)
                    Log.d("Tag",carrello.toString())
                    //viewModelCarrello.updateCarrello(carrello)
                }

            }*/


        }
/*
            lista_prodotti= arrayListOf()
            lista_prodotti.add(prodotto)
            var carrello=Carrello(utente.id,lista_prodotti,0.0f,false)
            viewModelCarrello.updateCarrello(carrello)

            /* recyclerView = binding.recyclerViewCarrello
             recyclerView.layoutManager =  LinearLayoutManager(requireContext())
             recyclerView.setHasFixedSize(true)
             binding.recyclerViewCarrello.adapter=adapter*/


        }*/
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
                   carrelli_utente=state.data.toMutableList()
                    Log.d("Tag",carrelli_utente.toString())
                }
            }
        }
    }
    fun getUtente() {
        viewModelAuth.getSession { user ->
            if (user != null) {
                utente=user
            }
        }
    }


}