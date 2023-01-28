package com.example.speedmarket.ui.catalogo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.speedmarket.databinding.FragmentDettagliProdottoBinding
import com.example.speedmarket.model.Prodotto
import com.example.speedmarket.model.Utente
import com.example.speedmarket.ui.auth.AuthViewModel
import com.example.speedmarket.ui.carrello.CarrelloFragment
import com.example.speedmarket.util.*
import dagger.hilt.android.AndroidEntryPoint
import java.math.RoundingMode
import java.text.DecimalFormat

@Suppress("DEPRECATION")
@AndroidEntryPoint
class DettagliProdottoFragment : Fragment() {

    lateinit var binding: FragmentDettagliProdottoBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var nomeCategoria :String
    private lateinit var utenteCorrente :Utente
    private lateinit var prodotto: Prodotto
    var isBackFromB = false
    val viewModel: ProdViewModel by viewModels()
    val viewModelAuth: AuthViewModel by viewModels()
    private val adapter by lazy { ProdottoSimileAdapter() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDettagliProdottoBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(!isOnline(requireContext()))dialogInternet()
        setupOnBackPressedFragment(CatalogoFragment())
        val args = this.arguments
        this.prodotto = args?.getSerializable("prodotto") as Prodotto
        this.nomeCategoria=this.prodotto.categoria
        oberver()
        viewModel.getProducts()
        adapter.onItemClick={
            val bundle = Bundle()
            bundle.putSerializable("prodotto",it)
            val fragment = DettagliProdottoFragment()
            fragment.arguments= bundle
            replaceFragment(fragment)
        }
        binding.title.text = this.prodotto.nome
        binding.prezzo.text = "€${
            calcolaPrezzo(
                this.prodotto.prezzo_unitario,
                this.prodotto.quantita,
                this.prodotto.offerta!!
            )
        }"
        bindImage(binding.imageProdotto, this.prodotto.immagine)
        binding.txtDescrizione.text = this.prodotto.descrizione
        binding.txtDataScadenza.text = "Data di scadenza: ${this.prodotto.data_scadenza}"
        binding.tornaIndietro.setOnClickListener {
            replaceFragment(CatalogoFragment())
        }
        binding.txtAggiungiCarrello.setOnClickListener {

                viewModel.updateProduct(this.prodotto)
                val bundle = Bundle()
                bundle.putSerializable("prodotto", this.prodotto)
                val fragment = CarrelloFragment()
                fragment.arguments = bundle
                replaceFragment(fragment)
        }
        recyclerView = binding.recyclerViewProdottiSimili
        recyclerView.layoutManager =  LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter=adapter

        if(this.prodotto.disponibilita == 0){
            binding.imageMeno.hide()
            binding.imagePiu.hide()
            binding.txtQuantitProdotto.text="Non disponibile al momento"
            binding.txtAggiungiCarrello.isClickable=false
        }else {

            binding.imagePiu.setOnClickListener {
                if(checkQuantita(binding.txtQuantitProdotto.text.toString().toInt())){
                        var quantitaOrdinata = binding.txtQuantitProdotto.text.toString().toInt()
                        quantitaOrdinata += 1
                        binding.txtQuantitProdotto.text = quantitaOrdinata.toString()
                        this.prodotto.unita_ordinate = binding.txtQuantitProdotto.text.toString().toInt()
                    }

            }
            binding.imageMeno.setOnClickListener {
                if (binding.txtQuantitProdotto.text.toString().toInt() > 1) {
                    var quantitaOrdinata = binding.txtQuantitProdotto.text.toString().toInt()
                    quantitaOrdinata -= 1
                    binding.txtQuantitProdotto.text = quantitaOrdinata.toString()

                    this.prodotto.unita_ordinate =
                        binding.txtQuantitProdotto.text.toString().toInt()
                }

            }

            if (binding.txtQuantitProdotto.text.toString().toInt() == 1) {
                this.prodotto.unita_ordinate = binding.txtQuantitProdotto.text.toString().toInt()
            }

        }
    }
    private fun oberver() {
        if (isOnline(requireContext())) {
            viewModel.prodotto.observe(viewLifecycleOwner) { state ->
                when (state) {
                    is UiState.Loading -> {
                    }
                    is UiState.Failure -> {
                        toast(state.error)
                    }
                    is UiState.Success -> {
                        adapter.filtraCategoriaAndRemove(this.nomeCategoria,
                            state.data.toMutableList(),
                            this.prodotto)
                    }
                }
            }
        } else {
            viewModel.prodottiLocal.observe(viewLifecycleOwner) { prodotti ->
                prodotti?.apply {
                    adapter.filtraCategoriaAndRemove(nomeCategoria,
                        prodotti.toMutableList(),
                        prodotto)
                }
            }
        }
    }

    private fun calcolaPrezzo(prezzo_unitario:Float, quantita:Float, offerta:Float): String {
        val dec = DecimalFormat("##0.00")
        return if(offerta < 1) {
            dec.roundingMode = RoundingMode.DOWN
            val prezzo = dec.format(prezzo_unitario * quantita * offerta)
            prezzo

        }else{
            dec.roundingMode = RoundingMode.DOWN
            val prezzo = dec.format(prezzo_unitario * quantita)
            prezzo
        }

    }
    private fun bindImage(imgView: ImageView, imgUrl: String?) {
        imgUrl?.let {
            val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
            imgView.load(imgUri)
        }
    }
    private fun checkQuantita(numero:Int):Boolean{

                if(numero<this.prodotto.disponibilita){
                    return true
                }else{
                        binding.txtQuantitProdotto.isEnabled=false
                        binding.imagePiu.isClickable=false
                }
        return false
    }
    override fun onStart() {
        super.onStart()
        viewModelAuth.getSession { user ->
            if (user != null) {
                this.utenteCorrente=user

            }
        }
    }
    override fun onPause() {
        super.onPause()
        isBackFromB = true
    }
    override fun onResume() {
        super.onResume()
        if (isBackFromB) {
            isBackFromB = false
            reload(DettagliProdottoFragment())
        }
    }
}


