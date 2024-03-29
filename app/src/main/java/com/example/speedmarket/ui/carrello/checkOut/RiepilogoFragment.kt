package com.example.speedmarket.ui.carrello.checkOut

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.speedmarket.R
import com.example.speedmarket.databinding.FragmentRiepilogoBinding
import com.example.speedmarket.model.Carrello
import com.example.speedmarket.model.Prodotto
import com.example.speedmarket.model.Utente
import com.example.speedmarket.ui.ProfileManager
import com.example.speedmarket.ui.auth.AuthViewModel
import com.example.speedmarket.ui.carrello.CarrelloFragment
import com.example.speedmarket.ui.carrello.CarrelloViewModel
import com.example.speedmarket.ui.catalogo.ProdViewModel
import com.example.speedmarket.ui.home.Home
import com.example.speedmarket.util.*
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class RiepilogoFragment : Fragment(), ProfileManager {

    private lateinit var binding: FragmentRiepilogoBinding
    private lateinit var recyclerView: RecyclerView
    override var utente: Utente? = null
    private lateinit var carrello :Carrello
    private lateinit var prodotti: MutableList<Prodotto>
    private val viewModelAuth: AuthViewModel by viewModels()
    private val viewModelCarrello: CarrelloViewModel by viewModels()
    private val viewModelProdotto: ProdViewModel by viewModels()
    private val adapter by lazy { RiepilogoAdapter() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRiepilogoBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnBackPressedFragment(CarrelloFragment())
        observer()
        binding.layoutCarta.hide()
        binding.layoutSpedizione.hide()
        binding.btnInformationCarta.setOnClickListener {
           showLayoutCarta()
        }
        binding.btnInformationSpedizione.setOnClickListener {
            showLayoutSpedizione()
        }
        getUserSession()
        getUserObs()
        utente?.let { viewModelAuth.getUtente(it.id) }

        getCarrelloObserver()

        adapter.onItemClick={
            val bottomSheetDialog = bottomSheetDialog(it)
            bottomSheetDialog.show()
            bottomSheetDialog.findViewById<Button>(R.id.btnChiudi)?.setOnClickListener {
                bottomSheetDialog.dismiss()
            }
        }
        getProdottiObs()
        recyclerView = binding.recyclerViewRiepilogoCarrello
        recyclerView.layoutManager =  LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        binding.recyclerViewRiepilogoCarrello.adapter=adapter

        binding.btnCarrelloCompletato.setOnClickListener{
            if(checkEditText()){
                val sdf = SimpleDateFormat("dd/M/yyyy")
                val currentDate = sdf.format(Date())
                this.carrello.date = currentDate
                this.carrello.stato=Carrello.Stato.elaborazione
                if(utente?.indirizzo_spedizione?.citta!!.isNotEmpty()){
                    this.carrello.indirizzoSpedizione= utente!!.indirizzo_spedizione
                }else{
                    this.carrello.indirizzoSpedizione=utente!!.residenza
                }
                this.carrello.pagamento=utente!!.pagamento
                viewModelCarrello.updateCarrello(this.carrello)
                aggiornaQuantita()
                viewModelCarrello.deleteCarrello(this.carrello)
                utente?.lista_carrelli?.add(this.carrello)
                utente?.let { it1 -> viewModelAuth.updateUserInfo(it1)}
                replaceFragment(Home())
            }
            else toast("informazioni non tutte complete!")
        }
    }

    private fun aggiornaQuantita() {
        for (prodotto_acquistato in this.carrello.lista_prodotti!!) {
            for (prodotto in this.prodotti) {
                if (prodotto == prodotto_acquistato) {
                    prodotto.disponibilita -= prodotto_acquistato.unita_ordinate
                    viewModelProdotto.updateProduct(prodotto)
                }
            }
        }
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
        viewModelCarrello.carrelliLocal.observe(viewLifecycleOwner) { carrelli ->
            carrelli?.apply {
                for (item in carrelli) {
                    if (item.id == utente?.id && item.stato == Carrello.Stato.incompleto) {
                        carrello = item
                        carrello.lista_prodotti?.let { adapter.updateList(it)}
                        binding.txtPrezzo.text="€${carrello.prezzo}"
                    }
                }
            }
        }
    }

    private fun getProdottiObs() {
        viewModelProdotto.prodottiLocal.observe(viewLifecycleOwner) { listaProdotti ->
            listaProdotti?.apply {
                prodotti = listaProdotti.toMutableList()
            }
        }
    }

    override fun updateUI() {
        binding.etNumeroCarta.setText(utente?.pagamento!!.numero_carta)
        binding.etNumeroCarta.isEnabled=false
        binding.etDataScadenza.setText(utente?.pagamento!!.data_scadenza)
        binding.etDataScadenza.isEnabled=false

        if (utente?.indirizzo_spedizione?.citta.isNullOrEmpty()) {
            binding.layoutSpedizione.isEnabled=false
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

    private fun showLayoutCarta(){
        if(binding.layoutCarta.isShown){
           binding.btnInformationCarta.setCompoundDrawablesWithIntrinsicBounds(requireContext().resources.getDrawable(R.drawable.chiudi,context!!.theme), null, null, null)
           binding.layoutCarta.hide()
        }else{
           binding.btnInformationCarta.setCompoundDrawablesWithIntrinsicBounds(requireContext().resources.getDrawable(R.drawable.apri,context!!.theme), null, null, null)
           binding.layoutCarta.show()
        }
    }

    private fun showLayoutSpedizione(){
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
    private fun checkEditText():Boolean{
        var isValid =true
            if(binding.txtCitta.text.isNullOrEmpty()
                || binding.txtProvincia.text.isNullOrEmpty()
                || binding.txtCap.text.isNullOrEmpty()
                || binding.txtVia.text.isNullOrEmpty()
                || binding.txtNumeroCivico.text.isNullOrEmpty()
                || binding.etNumeroCarta.text.isNullOrEmpty()
                || binding.etDataScadenza.text.isNullOrEmpty()
                || binding.etCvv.text.isNullOrEmpty()) {isValid=false}


        return isValid
    }

    fun observer() {
            viewModelAuth.updateUserInfo.observe(viewLifecycleOwner) { state ->
                when (state) {
                    is UiState.Loading -> {
                        binding.btnCarrelloCompletato.text = ""
                        binding.ordineProgressBar.show()
                    }
                    is UiState.Failure -> {
                        binding.btnCarrelloCompletato.text="Ordina"
                        binding.ordineProgressBar.hide()
                        toast(state.error)
                    }
                    is UiState.Success -> {
                        binding.btnCarrelloCompletato.text = "Ordina"
                        binding.ordineProgressBar.hide()
                        toast("Ordine completato")
                        binding.btnCarrelloCompletato.isClickable=false
                    }
                }
            }
    }

}