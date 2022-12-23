package com.example.speedmarket.ui.carrello

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.speedmarket.R
import com.example.speedmarket.databinding.FragmentCarrelloBinding
import com.example.speedmarket.databinding.FragmentDettagliProdottoBinding
import com.example.speedmarket.model.Prodotto
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CarrelloFragment : Fragment() {

    lateinit var binding: FragmentCarrelloBinding
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
        val args = this.arguments
        Log.d("Tag",args.toString())
        //var prodotto : Prodotto = args?.getSerializable("prodotto") as Prodotto
        // Log.d("Tag",prodotto.toString())
    }

}