package com.example.speedmarket.util

import com.example.speedmarket.util.Constants.OPEN_GOOGLE
import com.example.speedmarket.util.Constants.OPEN_SEARCH

object FireStoreCollection{
    val UTENTI = "Utenti"
    val PRODOTTI = "Prodotti"
    val CARRELLI = "Carrelli"
}
object SharedPrefConstants {
    val LOCAL_SHARED_PREF = "local_shared_pref"
    val USER_SESSION = "user_session"
}
object FireStoreDocumentField {
    val ID = "id"
}
object Constants {
    const val SEND_ID = "SEND_ID"
    const val RECEIVE_ID= "RECEIVE_ID"

    const val OPEN_GOOGLE = "Opening Google...."
    const val OPEN_SEARCH= "Searching...."

}
object IMAGE {
    const val IMAGE_PICK_CODE = 100
    private val PERMISSION_CODE = 101
}
object BotResponse {
    fun basicResponses(_message: String):String{
        val message = _message.toLowerCase()

        return when{
            message.toLowerCase().contains("buongiorno")->  {"Buongiorno"}
            message.contains("open") && message.contains("google")-> {
                OPEN_GOOGLE
            }
            message.contains("search") && message.contains("google")-> {
                OPEN_SEARCH
            }
            message.toLowerCase().contains("acquistare") ->{"D"}
            message.toLowerCase().contains("reso") ->{"le modalità di reso"}

            else->{"Scusami,non ho capito il tuo problema potresti ripetere"}
        }

        return "Scusami,non ho capito il tuo problema potresti ripetere"
    }

}


