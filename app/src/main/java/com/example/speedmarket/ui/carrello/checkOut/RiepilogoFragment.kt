package com.example.speedmarket.ui.carrello.checkOut

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
import com.example.speedmarket.databinding.FragmentRiepilogoBinding
import com.example.speedmarket.databinding.FragmentSpedizioneBinding
import com.example.speedmarket.model.Carrello
import com.example.speedmarket.model.Utente
import com.example.speedmarket.ui.auth.AuthViewModel
import com.example.speedmarket.ui.carrello.CarrelloFragment
import com.example.speedmarket.ui.carrello.CarrelloViewModel
import com.example.speedmarket.ui.catalogo.ProdViewModel
import com.example.speedmarket.ui.catalogo.ProdottoAdapter
import com.example.speedmarket.util.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RiepilogoFragment : Fragment() {

    private lateinit var binding: FragmentRiepilogoBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var utente: Utente
    val viewModelAuth: AuthViewModel by viewModels()
    val viewModelCarrello: CarrelloViewModel by viewModels()
    private val adapter by lazy { RiepilogoAdapter() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRiepilogoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnBackPressedFragment(CarrelloFragment())
        binding.layoutCarta.hide()
        binding.layoutSpedizione.hide()
        show_layout()
        getUtente()
        oberver()
        viewModelCarrello.getCarrello(utente)
        recyclerView = binding.recyclerViewRiepilogoCarrello
        recyclerView.layoutManager =  LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        binding.recyclerViewRiepilogoCarrello.adapter=adapter

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
                    state.data.lista_prodotti?.let { adapter.updateList(it)}
                    binding.txtPrezzo.text=state.data.prezzo.toString()

                }
            }
        }
    }
    private fun show_layout(){
        binding.subtitleCarta.setOnClickListener{
            binding.subtitleCarta.setCompoundDrawablesWithIntrinsicBounds(requireContext().resources.getDrawable(R.drawable.apri,context!!.theme), null, null, null)
            binding.layoutCarta.show()
            binding.subtitleCarta.setOnClickListener{
                binding.subtitleCarta.setCompoundDrawablesWithIntrinsicBounds(requireContext().resources.getDrawable(R.drawable.chiudi,context!!.theme), null, null, null)
                binding.layoutCarta.hide()
            }
        }
        binding.subtitleSpedizione.setOnClickListener{
            binding.subtitleSpedizione.setCompoundDrawablesWithIntrinsicBounds(requireContext().resources.getDrawable(R.drawable.apri,context!!.theme), null, null, null)
            binding.layoutSpedizione.show()
            binding.subtitleSpedizione.setOnClickListener{
                binding.subtitleSpedizione.setCompoundDrawablesWithIntrinsicBounds(requireContext().resources.getDrawable(R.drawable.chiudi,context!!.theme), null, null, null)
                binding.layoutSpedizione.hide()
            }
        }
    }
   private fun getUtente() {
        viewModelAuth.getSession { user ->
            if (user != null) {
                utente = user
            }
        }
    }

}