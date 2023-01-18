package com.example.speedmarket.ui.carrello

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.speedmarket.databinding.FragmentCarrelloBinding
import com.example.speedmarket.model.Carrello
import com.example.speedmarket.model.Prodotto
import com.example.speedmarket.model.Utente
import com.example.speedmarket.ui.auth.AuthViewModel
import com.example.speedmarket.ui.carrello.checkOut.CheckOutFragment
import com.example.speedmarket.ui.catalogo.ProdViewModel
import com.example.speedmarket.util.*
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.math.RoundingMode
import java.text.DecimalFormat

@AndroidEntryPoint
class CarrelloFragment : Fragment() {

    lateinit var binding: FragmentCarrelloBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var utente: Utente
    private lateinit var prodotto: Prodotto
    private lateinit var carrello: Carrello
    private var prodotto_catalogo =false
    val viewModelAuth: AuthViewModel by viewModels()
    val viewModelCarrello: CarrelloViewModel by viewModels()
    val viewModel: ProdViewModel by viewModels()
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
        if(adapter.itemCount<= 0){
            binding.btnCheckOut.isClickable=false
        }
        binding.txtSpedizione.setOnClickListener{
            toast("La spedizione ha un prezzo di €5 per ordini inferiori ai €50")
        }
        binding.btnDelete.setOnClickListener{
            toast("Per eliminare dei prodotti scorri da sinistra verso destra")
        }
        getUtente()
        oberver()
        viewModelCarrello.getCarrello(utente)
        val args = this.arguments
        if (args.toString() == "null") {
            prodotto_catalogo = false
        } else {
            prodotto = args?.getSerializable("prodotto") as Prodotto
            prodotto_catalogo = true
        }

        recyclerView = binding.recyclerViewCarrello
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        binding.recyclerViewCarrello.adapter = adapter

        binding.btnCheckOut.setOnClickListener{
            replaceFragment(CheckOutFragment())
        }
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
                    this.carrello=state.data
                    if(state.data.id == ""){
                        if(prodotto_catalogo){
                                aggiornaCarrello(prodotto, utente)
                                FunzioneAdapter()
                                viewModelCarrello.updateCarrello(carrello)
                                controlloCoerenzaCarrello()
                                Log.d("carrello",carrello.toString())
                        }
                    }else{
                        if(!this.carrello.ordine_completato){
                            if(prodotto_catalogo && this.carrello.lista_prodotti != null) {
                                if (controlloRidondanzaCarrello(prodotto))
                                    toast("il tuo prodotto è gia inserito nel carrello")
                                else {
                                    this.carrello.lista_prodotti?.add(prodotto)
                                    viewModelCarrello.updateCarrello(this.carrello)
                                }
                            }
                            FunzioneAdapter()
                            controlloCoerenzaCarrello()
                            viewModelCarrello.updateCarrello(this.carrello)
                        }
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
    fun FunzioneAdapter(){
        adapter.onItemClick = {
            update_quantita_ordine(this.carrello, it)
        }
    }
    fun aggiornaCarrello(prodotto: Prodotto, utente: Utente){
        this.carrello.lista_prodotti = arrayListOf()
        this.carrello.lista_prodotti?.add(prodotto)
        this.carrello.id = utente.id
        this.carrello.ordine_completato = false
    }
    fun controlloCoerenzaCarrello(){
        update_price_cart(this.carrello)
        swipe_delete(this.carrello)
        this.carrello.lista_prodotti?.let { adapter.updateList(it) }
    }
    fun controlloRidondanzaCarrello(prodotto: Prodotto):Boolean {
        for (elem in this.carrello.lista_prodotti!!)
            if (elem.id == prodotto.id) return true
        return false
    }
    fun calcolaPrezzo(prezzo_totale: Float): String {
        val dec = DecimalFormat("#.##")
        dec.roundingMode = RoundingMode.DOWN
        return dec.format(prezzo_totale)
    }
    private fun swipe_delete(carrello: Carrello){
        var deleteProduc: Prodotto? = null
        val swipeToDeleteCallback =  object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position= viewHolder.adapterPosition
                deleteProduc= carrello.lista_prodotti?.get(position)
                carrello.lista_prodotti?.remove(deleteProduc)
                viewModelCarrello.updateCarrello(carrello)
                carrello.lista_prodotti?.let { adapter.updateList(it) }
                //adapter.notifyItemRemoved(position)
                update_price_cart(carrello)
                Snackbar.make(recyclerView,
                    deleteProduc!!.nome,Snackbar.LENGTH_LONG)
                    .setAction("Annulla") {
                        carrello.lista_prodotti?.add(deleteProduc!!)
                        viewModelCarrello.updateCarrello(carrello)
                        carrello.lista_prodotti?.let { adapter.updateList(it) }
                        //adapter.notifyItemInserted(position)
                        update_price_cart(carrello)
                    }.show()
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

    }
    fun update_price_cart(carrello:Carrello){
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

    fun update_quantita_ordine(carrello: Carrello,it:Prodotto){
            var prodotto: Prodotto? =null
            viewModel.updateProduct(it)
            for (product in carrello.lista_prodotti!!) {
                if (product.id == it.id) {
                    prodotto = product
                }
            }
            if (prodotto != null) {
                carrello.lista_prodotti!!.remove(prodotto)
                //adapter.notifyItemRemoved(position)
                carrello.lista_prodotti!!.add(it)
                //adapter.notifyItemInserted(position)
                viewModelCarrello.updateCarrello(carrello)
            }
            update_price_cart(carrello)
    }
}


