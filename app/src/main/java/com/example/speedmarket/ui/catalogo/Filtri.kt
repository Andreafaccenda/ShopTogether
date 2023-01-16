package com.example.speedmarket.ui.catalogo

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.SeekBar
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.speedmarket.R
import com.example.speedmarket.databinding.FragmentFiltriBinding
import com.example.speedmarket.util.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_filtri.*

@AndroidEntryPoint
class Filtri : Fragment() {

    private lateinit var binding : FragmentFiltriBinding
    private lateinit var marchi: ArrayList<String>
    private var marchio = "vuoto"
    private var prezzo = "vuoto"
    private lateinit var recyclerView: RecyclerView
    private lateinit var filtri_adapter : FiltriAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFiltriBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnBackPressedFragment(CatalogoFragment())
        marchi = arrayListOf()
        marchi= requireContext().resources.getStringArray(R.array.marchi).toMutableList() as ArrayList<String>
        marchi.sort()
        Ui_recyclerView()
        AdapterFuction()
        hide_layout(binding.layoutPrice)
        binding.layoutMarchioChecked.hide()
        hide_layout(binding.layoutCategoria)
        hide_layout(binding.layoutSubCatFrutta)
        hide_layout(binding.layoutSubFruttaFresca)
        hide_layout(binding.layoutMarchio)
        binding.turnBack.setOnClickListener {
            replaceFragment(CatalogoFragment())
        }
        binding.btnPrice.setOnClickListener {
            show_layout(binding.layoutPrice,binding.btnPrice)
            listenerSeekBar()
        }
        binding.btnMarchio.setOnClickListener{
            show_layout(binding.layoutMarchio,binding.btnMarchio)
        }
        binding.btnCategoria.setOnClickListener{
            show_layout(binding.layoutCategoria,binding.btnCategoria)
        }
        binding.btnFruttaVerdura.setOnClickListener{
            show_layout(binding.layoutSubCatFrutta,binding.btnFruttaVerdura)
        }
        binding.btnFruttaFresca.setOnClickListener{
            show_layout(binding.layoutSubFruttaFresca,binding.btnFruttaFresca)
        }

        filter(radio_mele,layout_categoria)
        filter(radio_arance,layout_categoria)
        filter(radio_banane,layout_categoria)
        filter(radio_altraFrutta,layout_categoria)
        binding.btnMostra.setOnClickListener{
            show_list()
        }
        binding.searchMarchio.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                val testo = query.toString()
                list_text_submit(testo)
               return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                val testo = newText.toString()
                list_text_change(testo)
                return false
            }
        })
    }
    private fun list_text_submit(stringa:String){
        val listaAggiornata: ArrayList<String> = arrayListOf()
        listaAggiornata.clear()
        for(str in marchi){
            if(str.contains(stringa,ignoreCase = true))
                listaAggiornata.add(str)
        }
        if(listaAggiornata.isNullOrEmpty()) {
            toast("Nessun marchio trovato")
            filtri_adapter.updateList(listaAggiornata)
        }
        else filtri_adapter.updateList(listaAggiornata)

    }
    private fun list_text_change(stringa:String){
        val listaAggiornata: ArrayList<String> = arrayListOf()
        listaAggiornata.clear()
        for(str in marchi){
            if(str.startsWith(stringa,ignoreCase = true))
                listaAggiornata.add(str)
        }
        if(listaAggiornata.isNullOrEmpty()) {
            toast("Nessun marchio trovato")
            filtri_adapter.updateList(listaAggiornata)
        }
        else filtri_adapter.updateList(listaAggiornata)

    }

    private fun listenerSeekBar(){
        binding.seekbarPrice.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                binding.txtPriceMax.text=p1.toString()
                prezzo=binding.txtPriceMax.text.toString()
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {
                if(p0!=null){
                    var startPoint = p0.progress
                }
            }
            override fun onStopTrackingTouch(p0: SeekBar?) {
                if(p0!=null){
                    var endPoint = p0.progress
                }
            }
        })
    }
    private fun show_layout(layout:LinearLayout,button: Button){
        if(layout.isShown){
            button.setCompoundDrawablesWithIntrinsicBounds(null, null, requireContext().resources.getDrawable(R.drawable.chiudi,context!!.theme), null)
            layout.hide()
        }else{
            button.setCompoundDrawablesWithIntrinsicBounds(null, null, requireContext().resources.getDrawable(R.drawable.apri,context!!.theme), null)
            layout.show()
        }
    }
    private fun hide_layout(layout: LinearLayout){
        layout.hide()
    }
    private fun filter(radioButton: RadioButton,layout: LinearLayout){
        if(radioButton.isClickable){
            Log.d("selezione",radioButton.text.toString())
            layout.hide()
        }
    }
    private fun Ui_recyclerView(){
        recyclerView = binding.recyclerViewMarchio
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        filtri_adapter = FiltriAdapter()
        recyclerView.adapter = filtri_adapter
        filtri_adapter.updateList(marchi)
    }
    private fun AdapterFuction(){
        filtri_adapter.onItemClick = {
            marchio = it
            binding.layoutMarchioChecked.show()
            binding.filtroMarchio.text="${marchio} "
            binding.rbMarchio.isChecked=true
            show_layout(binding.layoutMarchio,binding.btnMarchio)
            binding.btnMostra.setOnClickListener{
                show_list()
            }
        }
    }
    private fun show_list(){
        val bundle = Bundle()
        var arrayList= arrayListOf(prezzo,marchio
        )
        bundle.putStringArrayList("filtri",arrayList)
        val fragment = CatalogoFragment()
        fragment.arguments= bundle
        replaceFragment(fragment)

    }
}


