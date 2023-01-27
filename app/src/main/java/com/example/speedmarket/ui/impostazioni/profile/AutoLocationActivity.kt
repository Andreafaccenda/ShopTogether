package com.example.speedmarket.ui.impostazioni.profile

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.os.StrictMode
import android.provider.Settings
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.speedmarket.R
import com.example.speedmarket.databinding.ActivityAutoLocationBinding
import com.example.speedmarket.model.Indirizzo
import com.example.speedmarket.model.Utente
import com.example.speedmarket.ui.ProfileManager
import com.example.speedmarket.ui.auth.AuthViewModel
import com.example.speedmarket.util.UiState
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream
import java.net.URL
@Suppress("DEPRECATION")
@AndroidEntryPoint
class AutoLocationActivity: AppCompatActivity(),ProfileManager {

    //Declaring the needed Variables
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var binding : ActivityAutoLocationBinding
    private var REQUEST_LOCATION_CODE = 101
    private val viewModelAuth: AuthViewModel by viewModels()
    override var utente: Utente? = null
    lateinit var residenza : Indirizzo
    private var policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
    private val key: String = "AjgIV-JLQzwYoMye7R1YrnbSFkY3dp7SFyUNyOZ7cQnliNDeqU45MW2jFdP9aKcJ"
    private var addressList : MutableList<String> = mutableListOf()
    val PERMISSION_ID = 1010

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAutoLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getUserSession()
        observer()
        updateUI(false)
        binding.imageEdit.setOnClickListener {
            updateUI(true)
        }
        binding.salvaResidenza.setOnClickListener{
           if(addressList.isNotEmpty()) {
               utente!!.residenza=residenza
               viewModelAuth.updateUserInfo(utente!!)
               observerUpdate()
               finish()
           }

        }
        binding.btFetchLocation.setOnClickListener {
            requestPermission()
            getLastLocation()
        }
    }
    private fun observerUpdate(){
        viewModelAuth.updateUserInfo.observe(this) { state ->
            when (state) {
                is UiState.Loading -> {
                }
                is UiState.Failure -> {
                }
                is UiState.Success -> {
                    Toast.makeText(this,state.data,Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun observer() {
        viewModelAuth.utente.observe(this) { state ->
            when (state) {
                is UiState.Loading -> {
                }
                is UiState.Failure -> {
                }
                is UiState.Success -> {
                    utente = state.data!!
                    updateUI()
                }
            }
        }
    }
    private fun getUserSession() {
        viewModelAuth.getSession { user ->
            if (user != null)
                utente = user
        }

    }
    fun getLastLocation(){
        if(checkPermission()){
            if(isLocationEnabled()){
                fusedLocationProviderClient.lastLocation.addOnCompleteListener {task->
                    val location: Location? = task.result
                    if(location == null){
                        newLocationData()
                    }else{
                        fromXML(location.latitude,location.longitude)
                    }
                }
            }else{
                showAlert()
            }
        }else{
            requestPermission()
        }
    }


    fun newLocationData(){
        val locationRequest =  LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,locationCallback, Looper.myLooper()
        )
    }


    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {

        }
    }
    fun fromXML(latitude : Double, longitude : Double){
        try {
            StrictMode.setThreadPolicy(policy)
            addressList.clear()
            var selezione = false
            val xmlData: InputStream =
                URL("https://dev.virtualearth.net/REST/v1/Locations/$latitude,$longitude?o=xml&key=$key").openStream()
            val factory: XmlPullParserFactory = XmlPullParserFactory.newInstance()
            val parser: XmlPullParser = factory.newPullParser()
            parser.setInput(xmlData, null)
            var event = parser.eventType
            while (event != XmlPullParser.END_DOCUMENT) {
                val tagName = parser.name
                when (event) {
                    XmlPullParser.START_TAG -> {
                        if (tagName.equals("AddressLine", ignoreCase = true) ||
                            tagName.equals("AdminDistrict2",ignoreCase = true) ||
                            tagName.equals("Locality", ignoreCase = true) ||
                            tagName.equals("PostalCode",ignoreCase = true)) selezione = true
                    }
                    XmlPullParser.TEXT -> {
                        if(selezione)
                            if(parser.text != "") addressList.add(parser.text)
                            else addressList.add("0")
                    }
                    XmlPullParser.END_TAG -> { selezione = false }
                }
                event = parser.next()
            }
            updateLayoutMaps()
        }catch(e:Exception){
            addressList.clear()
            addressList.add("0")
        }
    }

    private fun checkPermission():Boolean{
        //questa funzione ritorna un booleano
        //true: se hai i permessi
        //false
        if(
            ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ){
            return true
        }

        return false

    }

    fun requestPermission(){
        //this function will allows us to tell the user to requesut the necessary permsiion if they are not garented
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }

    fun isLocationEnabled():Boolean{
        //this function will return to us the state of the location service
        //if the gps or the network provider is enabled then it will return true otherwise it will return false
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_LOCATION_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "permission granted", Toast.LENGTH_LONG).show()
                    }
                } else {
                    // permission denied, boo! Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show()
                }
                return
            }
        }
    }
    private fun showAlert() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Attiva la posizione")
            .setMessage("Hai disattivato la tua posizione.\nPer favore attivala ")
            .setPositiveButton(getString(R.string.gps)) { _, _ ->
                val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(myIntent)
            }
            .setNegativeButton("Esci") { _, _ -> }
        dialog.show()
    }
    private fun updateUI(abilita : Boolean){
        binding.etCitta.isEnabled=abilita
        binding.etCap.isEnabled=abilita
        binding.etProvincia.isEnabled=abilita
        binding.etVia.isEnabled=abilita
    }

    private fun getAttributo(indice: Int): String{
        try{
            if(this.addressList[indice] == "0") return ""
            return this.addressList[indice]
        }catch(e:Exception){
            return ""
        }
    }

    private fun updateLayoutMaps(){
        val viaECivico = getAttributo(0).split(" ")
        var via = ""
        for(elem in viaECivico) if(!elem.equals(viaECivico.last(),true)) via+="$elem "
        binding.etCitta.setText(getAttributo(2))
        binding.etCap.setText(getAttributo(3))
        binding.etProvincia.setText(getAttributo(1))
        binding.etVia.setText(via)
        binding.etNumeroCivico.setText(viaECivico.last())
        residenza= Indirizzo(getAttributo(2),getAttributo(1),getAttributo(3),via,viaECivico.last())

    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
    override fun updateUI() {}
}