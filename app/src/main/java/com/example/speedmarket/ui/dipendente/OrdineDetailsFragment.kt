package com.example.speedmarket.ui.dipendente

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.speedmarket.R
import com.example.speedmarket.databinding.FragmentOrdineDetailsBinding
import com.example.speedmarket.model.Carrello
import com.example.speedmarket.model.Utente
import com.example.speedmarket.ui.ProfileManager
import com.example.speedmarket.ui.auth.AuthViewModel
import com.example.speedmarket.ui.carrello.checkOut.RiepilogoAdapter
import com.example.speedmarket.util.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_ordine_details.*
import java.math.RoundingMode
import java.text.DecimalFormat
@AndroidEntryPoint
class OrdineDetailsFragment : Fragment(), ProfileManager{

    lateinit var binding :FragmentOrdineDetailsBinding
    private val args : OrdineDetailsFragmentArgs by navArgs()
    private val viewModelAuth: AuthViewModel by viewModels()
    private lateinit  var carrello : Carrello
    private lateinit var recyclerView: RecyclerView
    private val adapter by lazy { RiepilogoAdapter() }
    override var utente: Utente? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentOrdineDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.setupOnBackPressedFragment()
        carrello=args.carrello
        binding.turnBack.setOnClickListener{
            view.findNavController().navigate(R.id.action_ordineDetailsFragment_to_staffHomeFragment)
        }
        binding.layoutCarta.hide()
        binding.layoutSpedizione.hide()
        binding.layoutInfoProfile.hide()
        binding.layoutChangeState.hide()
        binding.btnInformationCarta.setOnClickListener {
            showLayoutCarta()
        }
        binding.btnInformationSpedizione.setOnClickListener {
            showLayoutSpedizione()
        }
        binding.btnInformationUtente.setOnClickListener {
            showLayoutProfile()
        }
        getUserObserver()
        viewModelAuth.getUtente(carrello.id)
        observerUpdateUser()
        binding.cambiaStato.setOnClickListener{
           binding.layoutChangeState.show()
            binding.radioGroupStato.setOnCheckedChangeListener { _, _ ->
                if(ordine_consegnato.isChecked){
                    updateStateCarrello(Carrello.Stato.consegnato)
                    ordine_consegnato.isEnabled = false
         //           binding.layoutChangeState.hide()
                }
                if(ordine_elaborazione.isChecked){
                    updateStateCarrello(Carrello.Stato.elaborazione)
                    ordine_elaborazione.isEnabled=false
        //            binding.layoutChangeState.hide()
                }
                if(ordine_spedizione.isChecked){
                    updateStateCarrello(Carrello.Stato.spedizione)
                    ordine_spedizione.isEnabled=false
        //            binding.layoutChangeState.hide()
                }
            }
        }
        recyclerView = binding.recyclerViewRiepilogoCarrello
        recyclerView.layoutManager =  LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter=adapter
        adapter.onItemClick={
            val bottomSheetDialog = bottomSheetDialog(it)
            bottomSheetDialog.show()
            bottomSheetDialog.findViewById<Button>(R.id.btnChiudi)?.setOnClickListener {
                bottomSheetDialog.dismiss()
            }
        }
        binding.idOrdine.setOnClickListener{
            val clipboard = ContextCompat.getSystemService(requireContext(), ClipboardManager::class.java)
            val clip = ClipData.newPlainText("label",binding.idOrdine.text)
            clipboard?.setPrimaryClip(clip)
            toast("Copied to clipboard")
        }
    }

    override fun updateUI() {
        binding.idProfile.text="ID:${utente?.id}"
        binding.nomeProfile.text="Nome:${utente?.nome}"
        binding.cognomeProfile.text="Cognome:${utente?.cognome}"
        binding.emailProfile.text="Email:${utente?.email}"
        binding.cambiaStato.text="Stato ordine: ${this.carrello.stato}"
        binding.idOrdine.text=this.carrello.id
        this.carrello.lista_prodotti?.let { adapter.updateList(it) }
        var tot = 0.0F
        for(product in this.carrello.lista_prodotti!!){
            tot+=(product.unita_ordinate* product.offerta!! *product.quantita*product.prezzo_unitario)
        }
        binding.prezzo.text="€${calcolaPrezzo(tot)}"
        binding.totale.text="€${this.carrello.prezzo}"
        binding.spedizione.text="€5"

        binding.etNumeroCarta.setText(this.carrello.pagamento!!.numero_carta)
        binding.etNumeroCarta.isEnabled=false
        binding.etDataScadenza.setText(this.carrello.pagamento!!.data_scadenza)
        binding.etDataScadenza.isEnabled=false
        binding.layoutSpedizione.isEnabled=false
        binding.txtCitta.setText(this.carrello.indirizzoSpedizione!!.citta)
        binding.txtProvincia.setText(this.carrello.indirizzoSpedizione!!.provincia)
        binding.txtCap.setText(this.carrello.indirizzoSpedizione!!.cap)
        binding.txtVia.setText(this.carrello.indirizzoSpedizione!!.via)
        binding.txtNumeroCivico.setText(this.carrello.indirizzoSpedizione!!.numero_civico)

        if (this.carrello.stato == Carrello.Stato.consegnato)
            ordine_consegnato.isChecked = true
        else if (this.carrello.stato == Carrello.Stato.spedizione)
            ordine_spedizione.isChecked = true
        else if (this.carrello.stato == Carrello.Stato.elaborazione)
            ordine_elaborazione.isChecked = true
    }

    private fun calcolaPrezzo(prezzo: Float): String {
        val dec = DecimalFormat("#.##")
        dec.roundingMode = RoundingMode.DOWN
        return dec.format(prezzo)
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

    private fun showLayoutProfile(){
        if(binding.layoutInfoProfile.isShown){
            binding.btnInformationUtente.setCompoundDrawablesWithIntrinsicBounds(requireContext().resources.getDrawable(R.drawable.chiudi,context!!.theme), null, null, null)
            binding.layoutInfoProfile.hide()
        }else{
            binding.btnInformationUtente.setCompoundDrawablesWithIntrinsicBounds(requireContext().resources.getDrawable(R.drawable.apri,context!!.theme), null, null, null)
            binding.layoutInfoProfile.show()
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

    private fun getUserObserver() {
        viewModelAuth.utente.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                }
                is UiState.Failure -> {
                    toast(state.error)
                }
                is UiState.Success -> {
                    utente = state.data!!
                    updateUI()
                }
            }
        }
    }

    private fun updateStateCarrello(stato: Carrello.Stato){
        for(carrello in utente?.lista_carrelli!!){
            if(carrello == this.carrello){
                carrello.stato=stato
                viewModelAuth.updateUserInfo(utente!!)
            }
        }
    }

    private fun observerUpdateUser() {
        viewModelAuth.updateUserInfo.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                }
                is UiState.Failure -> {
                    toast(state.error)
                }
                is UiState.Success -> {
                    toast("Ordine modificato con successo!")
                }
            }
        }
    }

    private fun setupOnBackPressedFragment(){
        val callback=object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                view?.findNavController()?.navigate(R.id.action_ordineDetailsFragment_to_staffHomeFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }
}