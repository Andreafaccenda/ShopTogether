package com.example.speedmarket.ui.dipendente

import android.content.ClipData
import android.content.ClipboardManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
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
import com.example.speedmarket.ui.impostazioni.profile.OrdiniFragment
import com.example.speedmarket.util.*
import dagger.hilt.android.AndroidEntryPoint
import java.math.RoundingMode
import java.text.DecimalFormat
@AndroidEntryPoint
class OrdineDetailsFragment : Fragment(),ProfileManager{

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
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOrdineDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        carrello=args.carrello
        setupOnBackPressedFragment(StaffHomeFragment())
        binding.turnBack.setOnClickListener{
            view.findNavController().navigate(R.id.action_ordineDetailsFragment_to_staffHomeFragment)
        }
        binding.layoutCarta.hide()
        binding.layoutSpedizione.hide()
        binding.btnInformationCarta.setOnClickListener {
            show_layout_carta()
        }
        binding.btnInformationSpedizione.setOnClickListener {
            show_layout_spedizione()
        }
        recyclerView = binding.recyclerViewRiepilogoCarrello
        recyclerView.layoutManager =  LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter=adapter
        updateUI()
        adapter.onItemClick={
            var bottomSheetDialog = bottomSheetDialog(it)
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

        binding.idOrdine.text=this.carrello.id
        this.carrello.lista_prodotti?.let { adapter.updateList(it) }
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
    private fun show_layout_carta(){
        if(binding.layoutCarta.isShown){
            binding.btnInformationCarta.setCompoundDrawablesWithIntrinsicBounds(requireContext().resources.getDrawable(R.drawable.chiudi,context!!.theme), null, null, null)
            binding.layoutCarta.hide()
        }else{
            binding.btnInformationCarta.setCompoundDrawablesWithIntrinsicBounds(requireContext().resources.getDrawable(R.drawable.apri,context!!.theme), null, null, null)
            binding.layoutCarta.show()
        }
    }

    private fun show_layout_spedizione(){
        if(binding.layoutSpedizione.isShown){
            binding.btnInformationSpedizione.setCompoundDrawablesWithIntrinsicBounds(requireContext().resources.getDrawable(R.drawable.chiudi,context!!.theme), null, null, null)
            binding.layoutSpedizione.hide()
        }else{
            binding.btnInformationSpedizione.setCompoundDrawablesWithIntrinsicBounds(requireContext().resources.getDrawable(R.drawable.apri,context!!.theme), null, null, null)
            binding.layoutSpedizione.show()
        }
    }
}