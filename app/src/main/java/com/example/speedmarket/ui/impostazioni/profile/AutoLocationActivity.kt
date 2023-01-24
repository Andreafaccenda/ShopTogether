

package com.example.speedmarket.ui.impostazioni.profile

import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.os.StrictMode
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.speedmarket.R
import com.google.android.gms.location.*
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.InputStream
import java.net.URL
import java.util.*

class AutoLocationActivity: AppCompatActivity() {

    //Declaring the needed Variables
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    var policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
    lateinit var locationRequest: LocationRequest
    private val key: String = "AjgIV-JLQzwYoMye7R1YrnbSFkY3dp7SFyUNyOZ7cQnliNDeqU45MW2jFdP9aKcJ"
    var AddressList : MutableList<String> = mutableListOf()
    val PERMISSION_ID = 1010

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auto_location)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        findViewById<Button>(R.id.btFetchLocation).setOnClickListener {
            Log.d("Debug:",CheckPermission().toString())
            Log.d("Debug:",isLocationEnabled().toString())
            RequestPermission()
            /* fusedLocationProviderClient.lastLocation.addOnSuccessListener{location: Location? ->
                 textView.text = location?.latitude.toString() + "," + location?.longitude.toString()
             }*/
            getLastLocation()
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
                        Log.d("Location:" ,"Your Location:"+ location.longitude)
                        findViewById<TextView>(R.id.tvLatitude).text = "${location.latitude}"
                        findViewById<TextView>(R.id.tvLongitude).text = "${location.longitude}"
                        fromXML(location.latitude,location.longitude)
                    }
                }
            }else{
                Toast.makeText(this,"Please Turn on Your device Location",Toast.LENGTH_SHORT).show()
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
            Log.d("Debug:","your last last location: "+ lastLocation.longitude.toString())
            findViewById<TextView>(R.id.tvLatitude).text = "${lastLocation.latitude}"
            findViewById<TextView>(R.id.tvLongitude).text = "${lastLocation.longitude}"
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
            Log.d("Risultato",AddressList.toString())
        }catch(e:Exception){
            AddressList.clear()
            AddressList.add("-1")
        }
    }

    private fun CheckPermission():Boolean{
        //this function will return a boolean
        //true: if we have permission
        //false if not
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


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == PERMISSION_ID){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d("Debug:","You have the Permission")
            }
        }
    }

    private fun getCityName(lat: Double,long: Double):String{
        var cityName:String = ""
        var countryName = ""
        var geoCoder = Geocoder(this, Locale.getDefault())
        var Adress = geoCoder.getFromLocation(lat,long,3)

        cityName = Adress!!.get(0).locality
        countryName = Adress[0].countryName
        Log.d("Debug:","Your City: " + cityName + " ; your Country " + countryName)
        return cityName
    }

}