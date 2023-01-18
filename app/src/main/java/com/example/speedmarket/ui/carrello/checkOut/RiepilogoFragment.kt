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
import com.example.speedmarket.model.Utente
import com.example.speedmarket.ui.ProfileManager
import com.example.speedmarket.ui.auth.AuthViewModel
import com.example.speedmarket.ui.carrello.CarrelloFragment
import com.example.speedmarket.ui.carrello.CarrelloViewModel
import com.example.speedmarket.util.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RiepilogoFragment : Fragment(), ProfileManager {

    private lateinit var binding: FragmentRiepilogoBinding
    private lateinit var recyclerView: RecyclerView
    override var utente: Utente? = null
    val viewModelAuth: AuthViewModel by viewModels()
    val viewModelCarrello: CarrelloViewModel by viewModels()
    private val adapter by lazy { RiepilogoAdapter() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRiepilogoBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnBackPressedFragment(CarrelloFragment())
        binding.layoutCarta.hide()
        binding.layoutSpedizione.hide()
        binding.btnInformationCarta.setOnClickListener {
           show_layout_carta()
        }
        binding.btnInformationSpedizione.setOnClickListener {
            show_layout_spedizione()
        }
        getUserSession()
        getUserObs()
        utente?.let { viewModelAuth.getUtente(it.id) }

        getCarrelloObserver()
        utente?.let { viewModelCarrello.getCarrello(it) }

        Log.d("Utente", utente.toString())
        recyclerView = binding.recyclerViewRiepilogoCarrello
        recyclerView.layoutManager =  LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        binding.recyclerViewRiepilogoCarrello.adapter=adapter
    }

    private fun getUserObs() {
        viewModelAuth.utente.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                }
                is UiState.Failure -> {
                    toast(state.error)
                }
                is UiState.Success -> {
                    utente = state.data
                    updateUI()
                }
            }
        }
    }

    private fun getCarrelloObserver() {
        viewModelCarrello.carrello.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                }
                is UiState.Failure -> {
                    toast(state.error)
                }
                is UiState.Success -> {
                    state.data.lista_prodotti?.let { adapter.updateList(it)}
                    binding.txtPrezzo.text=state.data.prezzo
                }
            }
        }
    }

    override fun updateUI() {
        /**
         * Non aggiorna il metodo di pagamento
         */

        binding.etNumeroCarta.setText(utente?.pagamento!!.numero_carta)
        binding.etDataScadenza.setText(utente?.pagamento!!.data_scadenza)

        if (utente?.indirizzo_spedizione?.citta.isNullOrEmpty()) {
            binding.txtCitta.setText(utente?.residenza!!.citta)
            binding.txtProvincia.setText(utente?.residenza!!.provincia)
            binding.txtCap.setText(utente?.residenza!!.cap)
            binding.txtVia.setText(utente?.residenza!!.via)
            binding.txtNumeroCivico.setText(utente?.residenza!!.numero_civico)
        }
        else {
            binding.txtCitta.setText(utente?.indirizzo_spedizione!!.citta)
            binding.txtProvincia.setText(utente?.indirizzo_spedizione!!.provincia)
            binding.txtCap.setText(utente?.indirizzo_spedizione!!.cap)
            binding.txtVia.setText(utente?.indirizzo_spedizione!!.via)
            binding.txtNumeroCivico.setText(utente?.indirizzo_spedizione!!.numero_civico)
        }
    }

    private fun show_layout_carta(){
        if(binding.layoutCarta.isShown){
           binding.btnInformationCarta.setCompoundDrawablesWithIntrinsicBounds(requireContext().resources.getDrawable(R.drawable.chiudi,context!!.theme), null, null, null)
           binding.layoutCarta.hide()
        }else{
           binding.btnInformationCarta.setCompoundDrawablesWithIntrinsicBounds(requireContext().resources.getDrawable(R.drawable.apri,context!!.theme), null, null, null)
           binding.layoutCarta.show()
        }
    }

    private fun show_layout_spedizione(){
        if(binding.layoutSpedizione.isShown){
            binding.btnInformationSpedizione.setCompoundDrawablesWithIntrinsicBounds(requireContext().resources.getDrawable(R.drawable.chiudi,context!!.theme), null, null, null)
            binding.layoutSpedizione.hide()
        }else{
            binding.btnInformationSpedizione.setCompoundDrawablesWithIntrinsicBounds(requireContext().resources.getDrawable(R.drawable.apri,context!!.theme), null, null, null)
            binding.layoutSpedizione.show()
        }
    }
   private fun getUserSession() {
        viewModelAuth.getSession { user ->
            if (user != null) {
                utente = user
            }
        }
    }

}