package com.example.speedmarket.ui.catalogo

import android.os.Bundle
import android.util.Log
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
import com.example.speedmarket.R
import com.example.speedmarket.databinding.FragmentDettagliProdottoBinding
import com.example.speedmarket.model.Prodotto
import com.example.speedmarket.util.UiState
import com.example.speedmarket.util.toast
import dagger.hilt.android.AndroidEntryPoint
import java.math.RoundingMode
import java.text.DecimalFormat

@Suppress("DEPRECATION")
@AndroidEntryPoint
class DettagliProdottoFragment : Fragment() {

    lateinit var binding: FragmentDettagliProdottoBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var nome_categoria :String
    val viewModel: ProdViewModel by viewModels()
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
        val args = this.arguments
        viewModel.prodottiLocal.observe(viewLifecycleOwner) { products ->
            products?.apply {
                for (i in products)
                    Log.d("lista", i.nome)
            }
        }
        val prodotto : Prodotto = args?.getSerializable("prodotto") as Prodotto
        this.nome_categoria=prodotto.categoria
        oberver()
        viewModel.getProducts()
        binding.title.text= prodotto.nome
        binding.prezzo.text = "€${calcolaPrezzo(prodotto.prezzo_unitario,prodotto.quantita,prodotto.offerta!!)}"
        bindImage(binding.immagineProdotto,prodotto.immagine)
        binding.txtDescrizione.text= prodotto.descrizione
        binding.txtDataScadenza.text = "Data di scadenza: ${prodotto.data_scadenza}"
        binding.txtTornaIndietro.setOnClickListener{
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame_layout, CatalogoFragment())
            transaction?.commit()
        }
        recyclerView = binding.recyclerViewProdottiSimili
        recyclerView.layoutManager =  LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter=adapter
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


}