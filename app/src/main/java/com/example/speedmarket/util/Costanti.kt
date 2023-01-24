package com.example.speedmarket.util

import android.util.Log
import com.example.speedmarket.R

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
object FirebaseStorageConstants {
    val ROOT_DIRECTORY = "immagini"
}
object Constants {
    const val SEND_ID = "SEND_ID"
    const val RECEIVE_ID= "RECEIVE_ID"
    const val CLICK_ID= "CLICK_ID"
}
object IMAGE {
    const val IMAGE_PICK_CODE = 100
    private val PERMISSION_CODE = 101
}
object BotResponse {
    fun basicResponses(_message: String):String{
        val message = _message.toLowerCase()

        return when{
            message.contains("supporto profilo online") ->{"Digita la tipologia di richiesta:\n -Registrazione al sito" +
                                         "\n -Modifica account \n -Recupero/Reset passsword"}
            message.contains("recupero/reset password")->{"Se hai dimenticato la password recati alla pagina di Login e clicca il bottone Reset password, inserisci l'indirizzo email che ci hai fornito al momento della registrazione e riceverai una email per impostare una nuova password."}
            message.contains("modifica account")->{"Puoi modificare i tuoi dati accedendo alla tua area personale.Nella sezione Impostazioni, seleziona la pagina Account. Attraverso il bottone Modifica potrai modificare facilmente tutti i tuoi dati. Clicca per accedere direttamente alla tua area personale."}
            message.contains("registrazione al sito")->{"Per registrarti al sito clicca su \"Registrati\" in basso a destra e successivamente prosegui fino in fondo alla pagina. Segui i passaggi della procedura guidata e, una volta terminati, riceverai una email per completare la registrazione."}
            message.contains("chiudi la chat")->{"Buona giornata,é stato un piacere poterti aiutare!"}
            message.contains("ho bisogno di un altra informazione")->{"Digita una di queste tematiche:\n" +
                    "-Supporto per profilo online\n"+
                    "-Supporto per un'informazione\n"+
                    "-Supporto ad un ordine"}
            message.contains("vorrei parlare con un operatore")->{"Per parlare con un operatore si prega di scrivere un email al seguente sito: speedMarket@gmail.com"}

            else->{"Scusami,non ho capito il tuo problema potresti ripetere"}
        }

        return "Scusami,non ho capito il tuo problema potresti ripetere"
    }

}


