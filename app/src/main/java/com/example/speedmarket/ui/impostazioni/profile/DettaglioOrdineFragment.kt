package com.example.speedmarket.ui.impostazioni.profile

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.speedmarket.R
import com.example.speedmarket.databinding.FragmentDettaglioOrdineBinding
import com.example.speedmarket.databinding.FragmentRiepilogoBinding
import com.example.speedmarket.model.Carrello
import com.example.speedmarket.model.Utente
import com.example.speedmarket.ui.ProfileManager
import com.example.speedmarket.ui.auth.AuthViewModel
import com.example.speedmarket.ui.carrello.CarrelloFragment
import com.example.speedmarket.ui.carrello.checkOut.RiepilogoAdapter
import com.example.speedmarket.util.UiState
import com.example.speedmarket.util.replaceFragment
import com.example.speedmarket.util.setupOnBackPressedFragment
import com.example.speedmarket.util.toast
import dagger.hilt.android.AndroidEntryPoint
import java.math.RoundingMode
import java.text.DecimalFormat

@AndroidEntryPoint
class DettaglioOrdineFragment : Fragment(),ProfileManager{

    private lateinit var binding:FragmentDettaglioOrdineBinding
    private lateinit var recyclerView:RecyclerView
    private val adapter by lazy { RiepilogoAdapter() }
    override var utente: Utente? = null
    private lateinit var carrello : Carrello
    private lateinit var args : Bundle
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDettaglioOrdineBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnBackPressedFragment(OrdiniFragment())
        args = this.arguments!!
        binding.turnBack.setOnClickListener{
            replaceFragment(OrdiniFragment())
        }
        recyclerView = binding.recyclerViewRiepilogoCarrello
        recyclerView.layoutManager =  LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter=adapter
        updateUI()
    }

    override fun updateUI() {
        if(args.getSerializable("carrello") != "null"){
            this.carrello = args.getSerializable("carrello") as Carrello
            this.carrello.lista_prodotti?.let { adapter.updateList(it) }
            }
        var tot = 0.0F
        for(product in this.carrello.lista_prodotti!!){
            tot+=(product.unita_ordinate* product.offerta!! *product.quantita*product.prezzo_unitario)
        }
        binding.prezzo.text="€${calcolaPrezzo(tot)}"
        binding.totale.text="€${this.carrello.prezzo}"
        binding.spedizione.text="€5"
        }
    private fun calcolaPrezzo(prezzo :Float): String {
        val dec = DecimalFormat("#.##")
            dec.roundingMode = RoundingMode.DOWN
            val prezzo = dec.format(prezzo)

        return prezzo

    }


}