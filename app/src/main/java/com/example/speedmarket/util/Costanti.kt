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
object Constants {
    const val SEND_ID = "SEND_ID"
    const val RECEIVE_ID= "RECEIVE_ID"

    const val OPEN_GOOGLE = "Opening Google...."
    const val OPEN_SEARCH= "Searching...."

}
object BotResponse {
    fun basicResponses(_message: String):String{
        val message = _message.toLowerCase()

        return when{
            message.contains("Buongiorno")->  {"Buongiorno"}
            message.contains("open") && message.contains("google")-> {
                OPEN_GOOGLE
            }
            message.contains("search") && message.contains("google")-> {
                OPEN_SEARCH
            }
            message.contains("reso") ->{"le modalità di reso"}

            else -> {"Non ho capito"}
        }

        return "I don't understand"
    }

}


