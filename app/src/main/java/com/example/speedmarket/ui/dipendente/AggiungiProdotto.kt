package com.example.speedmarket.ui.dipendente

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.speedmarket.databinding.FragmentAggiungiProdottoBinding
import com.example.speedmarket.model.Prodotto
import com.example.speedmarket.ui.catalogo.ProdViewModel
import com.example.speedmarket.util.UiState
import com.example.speedmarket.util.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AggiungiProdotto : Fragment() {

    lateinit var binding:FragmentAggiungiProdottoBinding
    private  val viewModel: ProdViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAggiungiProdottoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       observer()
        binding.btnSalva.setOnClickListener {
            viewModel.addProduct( addProduct())
        }
    }
    private fun addProduct():Prodotto{
       val prodotto=Prodotto()
       prodotto.id=binding.etId.text.toString()
        prodotto.nome=binding.etNome.text.toString()
        prodotto.produttore=binding.etProduttore.text.toString()
        prodotto.prezzo_unitario=binding.etPrezzo.text.toString().toFloat()
        prodotto.quantita=binding.etQuantita.text.toString().toFloat()
        prodotto.data_scadenza=binding.etScadenza.text.toString()
        prodotto.offerta=binding.etOffera.text.toString().toFloat()
        prodotto.disponibilita=binding.etDisponibilita.text.toString().toInt()
        prodotto.immagine=""
        prodotto.categoria= ""
        prodotto.sub_categoria=""
        prodotto.descrizione=""
        prodotto.unita_ordinate=0
        Log.d("zzz",prodotto.toString())
        return prodotto
    }
    private fun observer() {
        viewModel.addProduct.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                }
                is UiState.Failure -> {
                    toast(state.error)
                }
                is UiState.Success -> {
                    toast(state.data)
                }
            }
        }
    }
}