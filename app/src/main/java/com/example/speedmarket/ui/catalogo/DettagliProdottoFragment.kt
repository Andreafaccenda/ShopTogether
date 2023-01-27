package com.example.speedmarket.ui.catalogo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.speedmarket.R
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
    private lateinit var nome_categoria :String
    private lateinit var utente_corrente :Utente
    private lateinit var prodotto: Prodotto
    val viewModel: ProdViewModel by viewModels()
    val viewModelAuth: AuthViewModel by viewModels()
    private val adapter by lazy { ProdottoSimileAdapter() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDettagliProdottoBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnBackPressed()
        val args = this.arguments
       /*viewModelAuth.prodottiLocal.observe(viewLifecycleOwner) { products ->
            products?.apply {
                for (i in products)
                    Log.d("lista", i.disponibilita.toString())
            }
        }*/
        this.prodotto = args?.getSerializable("prodotto") as Prodotto
        this.nome_categoria=this.prodotto.categoria
        oberver()
        viewModel.getProducts()
        binding.title.text = this.prodotto.nome
        binding.prezzo.text = "€${
            calcolaPrezzo(
                this.prodotto.prezzo_unitario,
                this.prodotto.quantita,
                this.prodotto.offerta!!
            )
        }"
        bindImage(binding.immagineProdotto, this.prodotto.immagine)
        binding.txtDescrizione.text = this.prodotto.descrizione
        binding.txtDataScadenza.text = "Data di scadenza: ${this.prodotto.data_scadenza}"
        binding.txtTornaIndietro.setOnClickListener {
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame_layout, CatalogoFragment())
            transaction?.commit()
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
                if(check_quantita(binding.txtQuantitProdotto.text.toString().toInt())){
                        var quantita_ordinata = binding.txtQuantitProdotto.text.toString().toInt()
                        quantita_ordinata += 1
                        binding.txtQuantitProdotto.text = quantita_ordinata.toString()
                        this.prodotto.unita_ordinate = binding.txtQuantitProdotto.text.toString().toInt()
                    }

            }
            binding.imageMeno.setOnClickListener {
                if (binding.txtQuantitProdotto.text.toString().toInt() > 1) {
                    var quantita_ordinata = binding.txtQuantitProdotto.text.toString().toInt()
                    quantita_ordinata -= 1
                    binding.txtQuantitProdotto.text = quantita_ordinata.toString()

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
        viewModel.prodotto.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                }
                is UiState.Failure -> {
                    toast(state.error)
                }
                is UiState.Success -> {
                    adapter.filtraListaCategoria(this.nome_categoria,state.data.toMutableList())
                }
            }
        }
    }

    private fun calcolaPrezzo(prezzo_unitario:Float, quantita:Float, offerta:Float): String {
        val dec = DecimalFormat("#.##")
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
    private fun check_quantita(numero:Int):Boolean{

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
                this.utente_corrente=user

            }
        }
    }
    private fun setupOnBackPressed(){
        val callback=object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                val fragment = CatalogoFragment()
                replaceFragment(fragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)
    }
}


