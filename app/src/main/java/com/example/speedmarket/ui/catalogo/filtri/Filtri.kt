package com.example.speedmarket.ui.catalogo.filtri

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
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
import com.example.speedmarket.ui.catalogo.filtri.Scanner.BarcodeScanning
import com.example.speedmarket.util.*
import dagger.hilt.android.AndroidEntryPoint


@Suppress("DEPRECATION")
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
    private lateinit var filtriAdapter : MarchioAdapter
    private lateinit var categoriaAdapter : CategoriaAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFiltriBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnBackPressedFragment(CatalogoFragment())
        binding.layoutScannerBarcode.hide()
        marchi = arrayListOf()
        marchi= requireContext().resources.getStringArray(R.array.marchi).toMutableList() as ArrayList<String>
        marchi.sort()
        uiRecyclerView()
        uiRecyclerViewCategoria()
        adapterFuction()
        adapterCategoryFuction()
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
        hideLayout(binding.layoutPrice)
        binding.layoutMarchioChecked.hide()
        hideLayout(binding.layoutCategoria)
        hideLayout(binding.layoutMarchio)
        hideLayout(binding.layoutCategoryChecked)
        hideLayout(binding.layoutSubCategoria)
        showCategory(binding.btnFruttaVerdura)
        showCategory(binding.btnCarneSalumi)
        showCategory(binding.btnPesceSushi)
        showCategory(binding.btnFormaggiLatteUova)
        showCategory(binding.btnPreparazioneDolci)
        showCategory(binding.btnBiscottiCerealiDolci)
        showCategory(binding.btnCaffeInfusi)
        showCategory(binding.btnCondimentiConserve)
        showCategory(binding.btnPanetteriaSnacksalati)
        showCategory(binding.btnPastaRiso)
        showCategory(binding.btnSurgelatiGelati)
        showCategory(binding.btnPiattiPronti)
        showCategory(binding.btnAnimaliDomestici)
        showCategory(binding.btnVinoBirraAlcolici)
        showCategory(binding.btnBevande)
        binding.turnBack.setOnClickListener {
            replaceFragment(CatalogoFragment())
        }
        binding.btnPrice.setOnClickListener {
            showLayout(binding.layoutPrice,binding.btnPrice)
            hideLayout(binding.layoutCategoria)
            hideLayout(binding.layoutMarchio)
            hideLayout(binding.layoutSubCategoria)
            listenerSeekBar()
        }
        binding.btnMarchio.setOnClickListener{
            showLayout(binding.layoutMarchio,binding.btnMarchio)
            hideLayout(binding.layoutCategoria)
            hideLayout(binding.layoutSubCategoria)
            hideLayout(binding.layoutPrice)
        }
        binding.btnCategoria.setOnClickListener{
            showLayout(binding.layoutCategoria,binding.btnCategoria)
            hideLayout(binding.layoutPrice)
            hideLayout(binding.layoutMarchio)
            hideLayout(binding.layoutSubCategoria)
            hideLayout(binding.layoutCategoryChecked)
        }
        binding.txtCategoria.setOnClickListener{
            showLayout(binding.layoutCategoria,binding.btnCategoria)
            hideLayout(binding.layoutSubCategoria)
            hideLayout(binding.layoutPrice)
            hideLayout(binding.layoutMarchio)
        }

        binding.btnMostra.setOnClickListener{
            showList()
        }
        binding.searchMarchio.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                val testo = query.toString()
                listTextSubmit(testo)
               return false
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                val testo = newText.toString()
                listTextChange(testo)
                return false
            }
        })
    }
    private fun listTextSubmit(stringa:String){
        val listaAggiornata: ArrayList<String> = arrayListOf()
        listaAggiornata.clear()
        for(str in marchi){
            if(str.contains(stringa,ignoreCase = true))
                listaAggiornata.add(str)
        }
        if(listaAggiornata.isEmpty()) {
            toast("Nessun marchio trovato")
            filtriAdapter.updateList(listaAggiornata)
        }
        else filtriAdapter.updateList(listaAggiornata)

    }
    private fun listTextChange(stringa:String){
        val listaAggiornata: ArrayList<String> = arrayListOf()
        listaAggiornata.clear()
        for(str in marchi){
            if(str.startsWith(stringa,ignoreCase = true))
                listaAggiornata.add(str)
        }
        if(listaAggiornata.isEmpty()) {
            toast("Nessun marchio trovato")
            filtriAdapter.updateList(listaAggiornata)
        }
        else filtriAdapter.updateList(listaAggiornata)

    }

    private fun listenerSeekBar(){
        binding.seekbarPrice.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                binding.txtPriceMax.text=p1.toString()
                prezzo=binding.txtPriceMax.text.toString()
            }
            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })
    }
    private fun showLayout(layout:LinearLayout, button: Button?){
        if(layout.isShown){
            button?.setCompoundDrawablesWithIntrinsicBounds(requireContext().resources.getDrawable(R.drawable.chiudi,context!!.theme), null, null, null)
            layout.hide()
        }else{
            button?.setCompoundDrawablesWithIntrinsicBounds(requireContext().resources.getDrawable(R.drawable.apri,context!!.theme), null, null, null)
            layout.show()
        }
    }
    private fun hideLayout(layout: LinearLayout){
        layout.hide()
    }
    private fun uiRecyclerView(){
        recyclerView = binding.recyclerViewMarchio
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        filtriAdapter = MarchioAdapter()
        recyclerView.adapter = filtriAdapter
        filtriAdapter.updateList(marchi)
    }
    private fun uiRecyclerViewCategoria(){
        binding.recyclerViewCategoria.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewCategoria.setHasFixedSize(true)
        categoriaAdapter = CategoriaAdapter()
        binding.recyclerViewCategoria.adapter = categoriaAdapter
    }
    private fun adapterFuction(){
        filtriAdapter.onItemClick = {
            marchio = it
            binding.layoutMarchioChecked.show()
            binding.filtroMarchio.text="$marchio "
            binding.rbMarchio.isChecked=true
            showLayout(binding.layoutMarchio,binding.btnMarchio)
            binding.btnMostra.setOnClickListener{
                showList()
            }
        }
    }
    private fun adapterCategoryFuction(){
        categoriaAdapter.onItemClick = {
            categoria = it
            hideLayout(binding.layoutSubCategoria)
            hideLayout(binding.layoutCategoria)
            binding.subCategoria.text=categoria
            showLayout(binding.layoutCategoryChecked,null)
            binding.btnMostra.setOnClickListener{
                showList()
            }
        }
    }
    private fun showList(){
        val bundle = Bundle()
        if(!binding.qrcode.text.isNullOrEmpty()) qrCode=binding.qrcode.text.toString()
        val arrayList= arrayListOf(prezzo,marchio,categoria,qrCode)
        bundle.putStringArrayList("filtri",arrayList)
        val fragment = CatalogoFragment()
        fragment.arguments= bundle
        replaceFragment(fragment)
    }
    private fun showCategory(button :Button){
        button.setOnClickListener{
            hideLayout(binding.layoutCategoria)
            showLayout(binding.layoutSubCategoria,null)
            binding.txtCategoria.text=button.text.toString()
            when(button.text.toString().lowercase()){
                "frutta e verdura"->{
                    val categoria = requireContext().resources.getStringArray(R.array.Frutta_e_verdura).toMutableList() as ArrayList<String>
                    categoriaAdapter.updateList(categoria)
                }
                "carne e salumi"->{
                    val categoria = requireContext().resources.getStringArray(R.array.Carne_e_salumi).toMutableList() as ArrayList<String>
                    categoriaAdapter.updateList(categoria)
                }
                "pesce e sushi"->{
                    val categoria = requireContext().resources.getStringArray(R.array.Pesce_e_sushi).toMutableList() as ArrayList<String>
                    categoriaAdapter.updateList(categoria)
                }
                "formaggi latte uova"->{
                    val categoria = requireContext().resources.getStringArray(R.array.Formaggi_latte_e_uova).toMutableList() as ArrayList<String>
                    categoriaAdapter.updateList(categoria)
                }
                "preparazione dolci e salati"->{
                    val categoria = requireContext().resources.getStringArray(R.array.Prep_dolci_e_salati).toMutableList() as ArrayList<String>
                    categoriaAdapter.updateList(categoria)
                }
                "biscotti,cereali e dolci"->{
                    val categoria = requireContext().resources.getStringArray(R.array.Biscotti_cereali_e_dolci).toMutableList() as ArrayList<String>
                    categoriaAdapter.updateList(categoria)
                }
                "caffe e infusi"->{
                    val categoria = requireContext().resources.getStringArray(R.array.Caffe_e_infusi).toMutableList() as ArrayList<String>
                    categoriaAdapter.updateList(categoria)
                }
                "condimenti e conserve"->{
                    val categoria = requireContext().resources.getStringArray(R.array.Condimenti_e_conserve).toMutableList() as ArrayList<String>
                    categoriaAdapter.updateList(categoria)
                }
                "panetteria e snack salati"->{
                    val categoria = requireContext().resources.getStringArray(R.array.Panetteria_e_snack_salati).toMutableList() as ArrayList<String>
                    categoriaAdapter.updateList(categoria)
                }
                "pasta e riso"->{
                    val categoria = requireContext().resources.getStringArray(R.array.Pasta_e_riso).toMutableList() as ArrayList<String>
                    categoriaAdapter.updateList(categoria)
                }
                "surgelati e gelati"->{
                    val categoria = requireContext().resources.getStringArray(R.array.Surgelati_e_gelati).toMutableList() as ArrayList<String>
                    categoriaAdapter.updateList(categoria)
                }
                "piatti pronti"->{
                    val categoria = requireContext().resources.getStringArray(R.array.Piattipronti).toMutableList() as ArrayList<String>
                    categoriaAdapter.updateList(categoria)
                }
                "articoli per la casa"->{
                    val categoria = requireContext().resources.getStringArray(R.array.Articoli_casa).toMutableList() as ArrayList<String>
                    categoriaAdapter.updateList(categoria)
                }
                "animali domestici"->{
                    val categoria = requireContext().resources.getStringArray(R.array.Animali_domestici).toMutableList() as ArrayList<String>
                    categoriaAdapter.updateList(categoria)
                }
                "vino,birra e altri alcolici"->{
                    val categoria = requireContext().resources.getStringArray(R.array.Vino_birra_altri_alcolici).toMutableList() as ArrayList<String>
                    categoriaAdapter.updateList(categoria)
                }
                "bevande"->{
                    val categoria = requireContext().resources.getStringArray(R.array.Bevande).toMutableList() as ArrayList<String>
                    categoriaAdapter.updateList(categoria)
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

    @Deprecated("Deprecated in Java")
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


