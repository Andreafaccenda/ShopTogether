package com.example.speedmarket.ui.carrello

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.speedmarket.R
import com.example.speedmarket.databinding.FragmentCarrelloBinding
import com.example.speedmarket.model.*
import com.example.speedmarket.ui.auth.AuthViewModel
import com.example.speedmarket.ui.carrello.checkOut.CheckOutFragment
import com.example.speedmarket.ui.catalogo.ProdViewModel
import com.example.speedmarket.util.*
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_app.*
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*

@Suppress("DEPRECATION")
@AndroidEntryPoint
class CarrelloFragment : Fragment() {

    lateinit var binding: FragmentCarrelloBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var utente: Utente
    private lateinit var prodotto: Prodotto
    private lateinit var carrello: Carrello
    private var prodottoCatalogo =false
    private var isBackFromB = false
    private val viewModelAuth: AuthViewModel by viewModels()
    val viewModelCarrello: CarrelloViewModel by viewModels()
    val viewModel: ProdViewModel by viewModels()
    private val adapter by lazy { CarrelloAdapter() }

    /**
     * Per UI Test commentare riga 66, 67 e aggiungere riga 68.
     */

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCarrelloBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!isOnline(requireContext())) dialogInternet()
        requireActivity().bottomNavigationView.menu.findItem(R.id.carrello).isChecked = true
        setupOnBackPressed(R.id.home)
        binding.txtSpedizione.setOnClickListener {
            toast("La Spedizione ha un prezzo di €5 per ordini inferiori ai €50")
        }
        binding.btnDelete.setOnClickListener {
            toast("Per eliminare dei prodotti scorri da sinistra verso destra")
        }
        getUtente()
        observeRemote()
        viewModelCarrello.getCarrello(utente)
        //     observeLocal()
        val args = this.arguments
        if (args.toString() == "null") {
            prodottoCatalogo = false
        } else {
            prodotto = args?.getSerializable("prodotto") as Prodotto
            prodottoCatalogo = true
        }

        recyclerView = binding.recyclerViewCarrello
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        binding.recyclerViewCarrello.adapter = adapter

        binding.btnCheckOut.setOnClickListener {
            replaceFragment(CheckOutFragment())
        }
    }
    private fun observeRemote() {
        if (isOnline(requireContext())) {
            viewModelCarrello.carrello.observe(viewLifecycleOwner) { state ->
                when (state) {
                    is UiState.Loading -> {
                    }
                    is UiState.Failure -> {
                        toast(state.error)
                    }
                    is UiState.Success -> {
                        this.carrello = state.data
                        if (state.data.id == "") {
                            if (prodottoCatalogo) {
                                aggiornaCarrello(prodotto, utente)
                                funzioneAdapter()
                                viewModelCarrello.updateCarrello(carrello)
                                controlloCoerenzaCarrello()
                            } else {
                                if(adapter.itemCount<= 0) {
                                    binding.layoutCarrello.hide()
                                    binding.textView5.text = "Il tuo carrello è vuoto!"
                                }
                            }
                        } else {
                            if (this.carrello.stato == Carrello.Stato.incompleto) {
                                if (prodottoCatalogo && this.carrello.lista_prodotti != null) {
                                    if (controlloRidondanzaCarrello(prodotto))
                                        toast("il tuo prodotto è gia presente nel carrello")
                                    else {
                                        this.carrello.lista_prodotti?.add(prodotto)
                                        updatePriceCart(this.carrello)
                                        viewModelCarrello.updateCarrello(this.carrello)
                                    }
                                }
                                funzioneAdapter()
                                controlloCoerenzaCarrello()
                                viewModelCarrello.updateCarrello(this.carrello)
                            }
                        }
                    }
                }
            }
        } else {
            observeLocal()
            return
        }
    }

    private fun observeLocal() {
        viewModelCarrello.carrelliLocal.observe(viewLifecycleOwner) { carrelli ->
            carrelli?.apply {
                for (item in carrelli) {
                    if (item.id == utente.id)
                        carrello = item
                }
                if (carrello.id == "") {
                    if (prodottoCatalogo) {
                        aggiornaCarrello(prodotto, utente)
                        controlloCoerenzaCarrello()
                    }
                } else {
                    if (carrello.stato == Carrello.Stato.incompleto) {
                        if (prodottoCatalogo && carrello.lista_prodotti != null) {
                            prodottoCatalogo = if (controlloRidondanzaCarrello(prodotto)) {
                                toast("il tuo prodotto è già presente nel carrello")
                                false
                            } else {
                                carrello.lista_prodotti?.add(prodotto)
                                updatePriceCart(carrello)
                                viewModelCarrello.updateCarrelloLocal(carrello)
                                false
                            }
                        }
                        controlloCoerenzaCarrello()
                    }
                }
            }
        }
    }

    fun getUtente() {
        viewModelAuth.getSession { user ->
            if (user != null) utente = user
        }
    }
    private fun funzioneAdapter(){
        adapter.onItemClick = {
            updateQuantitaOrdine(this.carrello, it)
            updatePriceCart(this.carrello)
            viewModelCarrello.updateCarrello(this.carrello)
        }
    }
    private fun aggiornaCarrello(prodotto: Prodotto, utente: Utente){
        this.carrello.lista_prodotti = arrayListOf()
        this.carrello.lista_prodotti?.add(prodotto)
        this.carrello.id = utente.id
        updatePriceCart(this.carrello)
        this.carrello.stato = Carrello.Stato.incompleto
        this.carrello.indirizzoSpedizione= Indirizzo("","","","","")
        this.carrello.pagamento= Pagamento("","")
    }
    private fun controlloCoerenzaCarrello(){
        swipeDelete(this.carrello)
        updatePriceCart(this.carrello)
        this.carrello.lista_prodotti?.let { adapter.updateList(it)
        requireActivity().bottomNavigationView.getOrCreateBadge(R.id.carrello).number=this.carrello.lista_prodotti!!.size}
        if (adapter.itemCount <= 0) {
            binding.layoutCarrello.hide()
            binding.textView5.text = "Il tuo carrello è vuoto!"
        }
    }
    private fun controlloRidondanzaCarrello(prodotto: Prodotto):Boolean {
        for (elem in this.carrello.lista_prodotti!!)
            if (elem.id == prodotto.id) return true
        return false
    }
    fun calcolaPrezzo(prezzo_totale: Float): String {
        val dec = DecimalFormat("##0.00")
        dec.roundingMode = RoundingMode.DOWN
        return dec.format(prezzo_totale)
    }
    private fun swipeDelete(carrello: Carrello){
        var deleteProduc: Prodotto?
        val swipeToDeleteCallback =  object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position= viewHolder.adapterPosition
                deleteProduc= carrello.lista_prodotti?.get(position)
                carrello.lista_prodotti?.remove(deleteProduc)
                if(adapter.itemCount<= 0){
                    viewModelCarrello.deleteCarrello(carrello)
                    binding.layoutCarrello.hide()
                    binding.textView5.text = "Il tuo carrello è vuoto!"
                    requireActivity().bottomNavigationView
                        .getOrCreateBadge(R.id.carrello).number = 0
                } else {
                    updatePriceCart(carrello)
                    viewModelCarrello.updateCarrello(carrello)
                    carrello.lista_prodotti?.let { adapter.updateList(it) }
                    requireActivity().bottomNavigationView.getOrCreateBadge(R.id.carrello)
                        .number=carrello.lista_prodotti!!.size
                    updatePriceCart(carrello)
                }
                Snackbar.make(recyclerView,
                    deleteProduc!!.nome,Snackbar.LENGTH_LONG)
                    .setAction("Annulla") {
                        carrello.lista_prodotti?.add(deleteProduc!!)
                        updatePriceCart(carrello)
                        viewModelCarrello.updateCarrello(carrello)
                        carrello.lista_prodotti?.let { adapter.updateList(it) }
                        requireActivity().bottomNavigationView.getOrCreateBadge(R.id.carrello).number=carrello.lista_prodotti!!.size
                        //adapter.notifyItemInserted(position)
                        updatePriceCart(carrello)
                    }.show()
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }
    fun updatePriceCart(carrello:Carrello){
        var prezzo = 0.0F
        if(!carrello.lista_prodotti.isNullOrEmpty()) {
            for (elem in carrello.lista_prodotti!!) {
                prezzo += (elem.quantita * elem.unita_ordinate * elem.offerta!! * elem.prezzo_unitario)
            }
            binding.txtPrezzoCarrello.text = "€${calcolaPrezzo(prezzo)}"
            binding.txtTotaleSpesaCarrello.text =  "€${calcolaPrezzo(prezzo+5)}"
            binding.txtPrezzoIva.text = "€${calcolaPrezzo(((prezzo + 5) * 22) / 100)}"
            carrello.prezzo =calcolaPrezzo(prezzo+5)
        }else carrello.prezzo=""
    }

    private fun updateQuantitaOrdine(carrello: Carrello, it:Prodotto){
            var prodotto: Prodotto? =null
            viewModel.updateProduct(it)
            for (product in carrello.lista_prodotti!!) {
                if (product.id == it.id) {
                    prodotto = product
                }
            }
            if (prodotto != null) {
                carrello.lista_prodotti!!.remove(prodotto)
                carrello.lista_prodotti!!.add(it)
                viewModelCarrello.updateCarrello(carrello)
            }
            updatePriceCart(carrello)
    }

    override fun onPause() {
        super.onPause()
        isBackFromB = true
    }
    override fun onResume() {
        super.onResume()
        if (isBackFromB) {
            isBackFromB = false
            reload(CarrelloFragment())
        }
    }
}


