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
import com.example.speedmarket.ui.catalogo.CatalogoFragment
import com.example.speedmarket.ui.catalogo.DettagliProdottoFragment
import com.example.speedmarket.ui.catalogo.ProdViewModel
import com.example.speedmarket.util.*
import com.google.android.material.snackbar.Snackbar
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
    private lateinit var carrello: Carrello
    private var inizializzato by Delegates.notNull<Boolean>()
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
            inizializzato = false
        } else {
            prodotto = args?.getSerializable("prodotto") as Prodotto
            inizializzato = true
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
                    var esiste=false
                    this.carrello=state.data
                    if(!this.carrello.ordine_completato) {
                        if (inizializzato) {
                            for (elem in this.carrello.lista_prodotti!!) {
                                if (elem.id == prodotto.id) {
                                    esiste = true
                                }
                            }
                            if (esiste) {
                                toast("il tuo prodotto è gia inserito nel carrello")
                            } else {

                                this.carrello.lista_prodotti?.add(prodotto)
                                viewModelCarrello.updateCarrello(this.carrello)
                            }
                        }
                        adapter.onItemClick = {
                            update_quantita_ordine(this.carrello,it)
                        }


                        update_price_cart(this.carrello)
                        /** metodo per rimuovere un oggetto prodotto dalla recycler view
                         */
                        swipe_delete(this.carrello)


                        this.carrello.lista_prodotti?.let { adapter.updateList(it) }
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
    private fun swipe_delete(carrello: Carrello){

        var deleteProduc: Prodotto? = null
        val swipeToDeleteCallback =  object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position= viewHolder.adapterPosition
                deleteProduc= carrello.lista_prodotti?.get(position)
                carrello.lista_prodotti?.removeAt(position)
                viewModelCarrello.updateCarrello(carrello)
                adapter.notifyItemRemoved(position)
                update_price_cart(carrello)
                Snackbar.make(recyclerView,
                    deleteProduc!!.nome,Snackbar.LENGTH_LONG)
                    .setAction("Annulla") {
                        carrello.lista_prodotti?.add(position,deleteProduc!!)
                        viewModelCarrello.updateCarrello(carrello)
                        adapter.notifyItemInserted(position)
                        update_price_cart(carrello)
                    }.show()


            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

    }
    fun update_price_cart(carrello:Carrello){
        this.carrello.prezzo=0.0F
        for(elem in this.carrello.lista_prodotti!!){
            this.carrello.prezzo+=(elem.quantita*elem.unita_ordinate*elem.offerta!!*elem.prezzo_unitario)
            binding.txtPrezzoCarrello.text="€${calcolaPrezzo(carrello.prezzo)}"
            binding.txtTotaleSpesaCarrello.text="€${calcolaPrezzo(carrello.prezzo+5)}"
            binding.txtPrezzoIva.text="€${calcolaPrezzo(((carrello.prezzo+5)*22)/100)}"
        }
    }
    fun update_quantita_ordine(carrello: Carrello,it:Prodotto){
            var position = 0
            var prodotto: Prodotto? =null
            viewModel.updateProduct(it)
            for(product in carrello.lista_prodotti!!){
                if(product.id == it.id){
                    prodotto=product
                    position = carrello.lista_prodotti!!.indexOf(product)
                }
            }
            if(prodotto != null){
                carrello.lista_prodotti!!.remove(prodotto)
                //adapter.notifyItemRemoved(position)
                carrello.lista_prodotti!!.add(it)
                //adapter.notifyItemInserted(position)
                viewModelCarrello.updateCarrello(carrello)
            }
        update_price_cart(carrello)
    }
}


