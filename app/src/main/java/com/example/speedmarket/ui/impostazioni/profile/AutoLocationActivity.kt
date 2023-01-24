package com.example.speedmarket.ui.impostazioni.profile

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.os.StrictMode
import android.provider.Settings
import android.provider.Telephony.Mms.Addr
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.example.speedmarket.R
import com.example.speedmarket.databinding.ActivityAppBinding
import com.example.speedmarket.databinding.ActivityAutoLocationBinding
import com.example.speedmarket.model.Indirizzo
import com.example.speedmarket.model.Utente
import com.example.speedmarket.ui.ProfileManager
import com.example.speedmarket.ui.auth.AuthViewModel
import com.example.speedmarket.util.UiState
import com.example.speedmarket.util.toast
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream
import java.net.URL
import java.util.*
@AndroidEntryPoint
class AutoLocationActivity: AppCompatActivity(),ProfileManager {

    //Declaring the needed Variables
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var binding : ActivityAutoLocationBinding
    private var REQUEST_LOCATION_CODE = 101
    private val viewModelAuth: AuthViewModel by viewModels()
    override var utente: Utente? = null
    var policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
    lateinit var locationRequest: LocationRequest
    private val key: String = "AjgIV-JLQzwYoMye7R1YrnbSFkY3dp7SFyUNyOZ7cQnliNDeqU45MW2jFdP9aKcJ"
    var AddressList : MutableList<String> = mutableListOf()
    val PERMISSION_ID = 1010

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAutoLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        getUserSession()
        observer()
        update_UI(false)
        binding.imageEdit.setOnClickListener {
            update_UI(true)
        }
        binding.salvaResidenza.setOnClickListener{
           if(AddressList.isNotEmpty()) {
               var residenza = Indirizzo(AddressList[2],AddressList[1],AddressList[3],AddressList[0],"")
               utente!!.residenza=residenza
               viewModelAuth.updateUserInfo(utente!!)
               observer_update()
               finish()
           }

        }
        binding.btFetchLocation.setOnClickListener {
            RequestPermission()
            getLastLocation()
        }
    }
    private fun observer_update(){
        viewModelAuth.updateUserInfo.observe(this) { state ->
            when (state) {
                is UiState.Loading -> {
                }
                is UiState.Failure -> {
                }
                is UiState.Success -> {
                    Toast.makeText(this,state.data.toString(),Toast.LENGTH_SHORT).show()
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
        if(CheckPermission()){
            if(isLocationEnabled()){
                fusedLocationProviderClient.lastLocation.addOnCompleteListener {task->
                    var location: Location? = task.result
                    if(location == null){
                        NewLocationData()
                    }else{
                        fromXML(location.latitude,location.longitude)
                    }
                }
            }else{
                showAlert()
            }
        }else{
            RequestPermission()
        }
    }


    fun NewLocationData(){
        var locationRequest =  LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 0
        locationRequest.fastestInterval = 0
        locationRequest.numUpdates = 1
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient!!.requestLocationUpdates(
            locationRequest,locationCallback, Looper.myLooper()
        )
    }


    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(locationResult: LocationResult) {
            var lastLocation: Location = locationResult.lastLocation

        }
    }
    fun fromXML(latitude : Double, longitude : Double){
        try {
            StrictMode.setThreadPolicy(policy)
            AddressList.clear()
            var selezione = false
            val xml_data: InputStream =
                URL("https://dev.virtualearth.net/REST/v1/Locations/$latitude,$longitude?o=xml&key=$key").openStream()
            val factory: XmlPullParserFactory = XmlPullParserFactory.newInstance()
            val parser: XmlPullParser = factory.newPullParser()
            parser.setInput(xml_data, null)
            var event = parser.eventType
            while (event != XmlPullParser.END_DOCUMENT) {
                val tag_name = parser.name
                when (event) {
                    XmlPullParser.START_TAG -> {
                        if (tag_name.equals("AddressLine", ignoreCase = true) ||
                            tag_name.equals("AdminDistrict2",ignoreCase = true) ||
                            tag_name.equals("Locality", ignoreCase = true) ||
                            tag_name.equals("PostalCode",ignoreCase = true)) selezione = true
                    }
                    XmlPullParser.TEXT -> {
                        if(selezione)
                            if(parser.text != "") AddressList.add(parser.text)
                            else AddressList.add("0")
                    }
                    XmlPullParser.END_TAG -> { selezione = false }
                }
                event = parser.next()
            }
            update_layoutMaps()
        }catch(e:Exception){
            AddressList.clear()
            AddressList.add("-1")
        }
    }

    private fun CheckPermission():Boolean{
        //questa funzione ritorna un booleano
        //true: se hai i permessi
        //false
        if(
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
            ActivityCompat.checkSelfPermission(this,android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ){
            return true
        }

        return false

    }

    fun RequestPermission(){
        //this function will allows us to tell the user to requesut the necessary permsiion if they are not garented
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION,android.Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }

    fun isLocationEnabled():Boolean{
        //this function will return to us the state of the location service
        //if the gps or the network provider is enabled then it will return true otherwise it will return false
        var locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
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
            .setPositiveButton(getString(R.string.gps)) { paramDialogInterface, paramInt ->
                val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(myIntent)
            }
            .setNegativeButton("Esci") { paramDialogInterface, paramInt -> }
        dialog.show()
    }
    private fun update_UI(abilita : Boolean){
        binding.etCitta.isEnabled=abilita
        binding.etCap.isEnabled=abilita
        binding.etProvincia.isEnabled=abilita
        binding.etVia.isEnabled=abilita
    }
    private fun update_layoutMaps(){

        binding.etCitta.setText(this.AddressList[2])
        binding.etCap.setText(this.AddressList[3])
        binding.etProvincia.setText(AddressList[1])
        binding.etVia.setText(AddressList[0])

    }



    override fun updateUI() {}
}