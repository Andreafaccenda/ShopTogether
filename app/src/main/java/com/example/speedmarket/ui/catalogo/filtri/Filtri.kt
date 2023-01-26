package com.example.speedmarket.ui.catalogo.filtri

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.SeekBar
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.speedmarket.R
import com.example.speedmarket.databinding.FragmentFiltriBinding
import com.example.speedmarket.ui.catalogo.CatalogoFragment
import com.example.speedmarket.ui.catalogo.DettagliProdottoFragment
import com.example.speedmarket.ui.catalogo.filtri.Scanner.BarcodeScanning
import com.example.speedmarket.util.*
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class Filtri : Fragment() {


    private lateinit var binding : FragmentFiltriBinding
    private lateinit var marchi: ArrayList<String>
    private var marchio = "vuoto"
    private var prezzo = "vuoto"
    private var categoria = "vuoto"
    private var qrCode ="vuoto"
    private val cameraPermissionRequestCode = 1
    private var selectedScanningSDK = BarcodeScanning.ScannerSDK.ZXING
    private lateinit var recyclerView: RecyclerView
    private lateinit var filtri_adapter : MarchioAdapter
    private lateinit var categoria_adapter : CategoriaAdapter

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
        setupOnBackPressedFragment(CatalogoFragment(),Filtri())
        binding.layoutScannerBarcode.hide()
        marchi = arrayListOf()
        marchi= requireContext().resources.getStringArray(R.array.marchi).toMutableList() as ArrayList<String>
        marchi.sort()
        Ui_recyclerView()
        Ui_recyclerView_Cat()
        AdapterFuction()
        AdapterCategoryFuction()
        binding.btnScan.setOnClickListener {
            selectedScanningSDK = BarcodeScanning.ScannerSDK.ZXING
            startScanning()
            if(binding.layoutScanner.isShown){
                binding.layoutScanner.hide()
                binding.layoutScannerBarcode.show()
                binding.btnScanner.setOnClickListener{
                    selectedScanningSDK = BarcodeScanning.ScannerSDK.ZXING
                    startScanning()
                }
            }
        }
        hide_layout(binding.layoutPrice)
        binding.layoutMarchioChecked.hide()
        hide_layout(binding.layoutCategoria)
        hide_layout(binding.layoutMarchio)
        hide_layout(binding.layoutCategoryChecked)
        hide_layout(binding.layoutSubCategoria)
        show_category(binding.btnFruttaVerdura)
        show_category(binding.btnCarneSalumi)
        show_category(binding.btnPesceSushi)
        show_category(binding.btnFormaggiLatteUova)
        show_category(binding.btnPreparazioneDolci)
        show_category(binding.btnBiscottiCerealiDolci)
        show_category(binding.btnCaffeInfusi)
        show_category(binding.btnCondimentiConserve)
        show_category(binding.btnPanetteriaSnacksalati)
        show_category(binding.btnPastaRiso)
        show_category(binding.btnSurgelatiGelati)
        show_category(binding.btnPiattiPronti)
        show_category(binding.btnAnimaliDomestici)
        show_category(binding.btnVinoBirraAlcolici)
        show_category(binding.btnBevande)
        binding.turnBack.setOnClickListener {
            replaceFragment(CatalogoFragment())
        }
        binding.btnPrice.setOnClickListener {
            show_layout(binding.layoutPrice,binding.btnPrice)
            hide_layout(binding.layoutCategoria)
            hide_layout(binding.layoutMarchio)
            hide_layout(binding.layoutSubCategoria)
            listenerSeekBar()
        }
        binding.btnMarchio.setOnClickListener{
            show_layout(binding.layoutMarchio,binding.btnMarchio)
            hide_layout(binding.layoutCategoria)
            hide_layout(binding.layoutSubCategoria)
            hide_layout(binding.layoutPrice)
        }
        binding.btnCategoria.setOnClickListener{
            show_layout(binding.layoutCategoria,binding.btnCategoria)
            hide_layout(binding.layoutPrice)
            hide_layout(binding.layoutMarchio)
            hide_layout(binding.layoutSubCategoria)
            hide_layout(binding.layoutCategoryChecked)
        }
        binding.txtCategoria.setOnClickListener{
            show_layout(binding.layoutCategoria,binding.btnCategoria)
            hide_layout(binding.layoutSubCategoria)
            hide_layout(binding.layoutPrice)
            hide_layout(binding.layoutMarchio)
        }

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
    private fun show_layout(layout:LinearLayout, button: Button?){
        if(layout.isShown){
            button?.setCompoundDrawablesWithIntrinsicBounds(requireContext().resources.getDrawable(R.drawable.chiudi,context!!.theme), null, null, null)
            layout.hide()
        }else{
            button?.setCompoundDrawablesWithIntrinsicBounds(requireContext().resources.getDrawable(R.drawable.apri,context!!.theme), null, null, null)
            layout.show()
        }
    }
    private fun hide_layout(layout: LinearLayout){
        layout.hide()
    }
    private fun Ui_recyclerView(){
        recyclerView = binding.recyclerViewMarchio
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        filtri_adapter = MarchioAdapter()
        recyclerView.adapter = filtri_adapter
        filtri_adapter.updateList(marchi)
    }
    private fun Ui_recyclerView_Cat(){
        binding.recyclerViewCategoria.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewCategoria.setHasFixedSize(true)
        categoria_adapter = CategoriaAdapter()
        binding.recyclerViewCategoria.adapter = categoria_adapter
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
    private fun AdapterCategoryFuction(){
        categoria_adapter.onItemClick = {
            categoria = it
            hide_layout(binding.layoutSubCategoria)
            hide_layout(binding.layoutCategoria)
            binding.subCategoria.text=categoria
            show_layout(binding.layoutCategoryChecked,null)
            binding.btnMostra.setOnClickListener{
                show_list()
            }
        }
    }
    private fun show_list(){
        val bundle = Bundle()
        if(!binding.qrcode.text.isNullOrEmpty()) qrCode=binding.qrcode.text.toString()
        var arrayList= arrayListOf(prezzo,marchio,categoria,qrCode)
        bundle.putStringArrayList("filtri",arrayList)
        val fragment = CatalogoFragment()
        fragment.arguments= bundle
        replaceFragment(fragment)

    }
    private fun show_category(button :Button){
        button.setOnClickListener{
            hide_layout(binding.layoutCategoria)
            show_layout(binding.layoutSubCategoria,null)
            binding.txtCategoria.text=button.text.toString()
            when(button.text.toString().toLowerCase()){
                "frutta e verdura"->{
                    var categoria = requireContext().resources.getStringArray(R.array.Frutta_e_verdura).toMutableList() as ArrayList<String>
                    categoria_adapter.updateList(categoria)
                }
                "carne e salumi"->{
                    var categoria = requireContext().resources.getStringArray(R.array.Carne_e_salumi).toMutableList() as ArrayList<String>
                    categoria_adapter.updateList(categoria)
                }
                "pesce e sushi"->{
                    var categoria = requireContext().resources.getStringArray(R.array.Pesce_e_sushi).toMutableList() as ArrayList<String>
                    categoria_adapter.updateList(categoria)
                }
                "formaggi latte uova"->{
                    var categoria = requireContext().resources.getStringArray(R.array.Formaggi_latte_e_uova).toMutableList() as ArrayList<String>
                    categoria_adapter.updateList(categoria)
                }
                "preparazione dolci e salati"->{
                    var categoria = requireContext().resources.getStringArray(R.array.Prep_dolci_e_salati).toMutableList() as ArrayList<String>
                    categoria_adapter.updateList(categoria)
                }
                "biscotti,cereali e dolci"->{
                    var categoria = requireContext().resources.getStringArray(R.array.Biscotti_cereali_e_dolci).toMutableList() as ArrayList<String>
                    categoria_adapter.updateList(categoria)
                }
                "caffe e infusi"->{
                    var categoria = requireContext().resources.getStringArray(R.array.Caffe_e_infusi).toMutableList() as ArrayList<String>
                    categoria_adapter.updateList(categoria)
                }
                "condimenti e conserve"->{
                    var categoria = requireContext().resources.getStringArray(R.array.Condimenti_e_conserve).toMutableList() as ArrayList<String>
                    categoria_adapter.updateList(categoria)
                }
                "panetteria e snack salati"->{
                    var categoria = requireContext().resources.getStringArray(R.array.Panetteria_e_snack_salati).toMutableList() as ArrayList<String>
                    categoria_adapter.updateList(categoria)
                }
                "pasta e riso"->{
                    var categoria = requireContext().resources.getStringArray(R.array.Pasta_e_riso).toMutableList() as ArrayList<String>
                    categoria_adapter.updateList(categoria)
                }
                "surgelati e gelati"->{
                    var categoria = requireContext().resources.getStringArray(R.array.Surgelati_e_gelati).toMutableList() as ArrayList<String>
                    categoria_adapter.updateList(categoria)
                }
                "piatti pronti"->{
                    var categoria = requireContext().resources.getStringArray(R.array.Piattipronti).toMutableList() as ArrayList<String>
                    categoria_adapter.updateList(categoria)
                }
                "articoli per la casa"->{
                    var categoria = requireContext().resources.getStringArray(R.array.Articoli_casa).toMutableList() as ArrayList<String>
                    categoria_adapter.updateList(categoria)
                }
                "animali domestici"->{
                    var categoria = requireContext().resources.getStringArray(R.array.Animali_domestici).toMutableList() as ArrayList<String>
                    categoria_adapter.updateList(categoria)
                }
                "vino,birra e altri alcolici"->{
                    var categoria = requireContext().resources.getStringArray(R.array.Vino_birra_altri_alcolici).toMutableList() as ArrayList<String>
                    categoria_adapter.updateList(categoria)
                }
                "bevande"->{
                    var categoria = requireContext().resources.getStringArray(R.array.Bevande).toMutableList() as ArrayList<String>
                    categoria_adapter.updateList(categoria)
                }
            }

        }
    }

    private fun startScanning() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            openCameraWithScanner()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                cameraPermissionRequestCode
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == cameraPermissionRequestCode && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCameraWithScanner()
            } else if (!ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.CAMERA
                )
            ) {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri: Uri = Uri.fromParts("package", requireActivity().packageName, null)
                intent.data = uri
                startActivityForResult(intent, cameraPermissionRequestCode)
            }
        }
    }

    private fun openCameraWithScanner() {
        BarcodeScanning.start(requireContext(), selectedScanningSDK)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == cameraPermissionRequestCode) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                openCameraWithScanner()
            }
        }
    }
}


