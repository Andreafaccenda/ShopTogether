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
object Notification {
    val CHANNEL_ID="NOTIFICHE"
    val SPEEDMARKET="SPEED MARKET"
}
object IMAGE {
    const val IMAGE_PICK_CODE = 100
    private val PERMISSION_CODE = 101
}
object BotResponse {
    fun basicResponses(_message: String):String{
        val message = _message.toLowerCase()

        return when{
            message.contains("supporto profilo online")->{"Scegli la tipologia di richiesta oppure torna al menu precedente per selezionare un'altra opzione:(profilo online)"}
            message.contains("torna al menu precedente")-> {"Seleziona una delle seguenti opzioni per richiedere supporto:"}
            message.contains("come mi registro al sito")->{"Per registrarti al sito clicca su Registrati in basso a destra nella finestra di Login e successivamente su Prosegui in fondo alla pagina. Segui i passaggi della procedura guidata e, una volta terminati, riceverai una email per completare la registrazione."}
            message.contains("problemi durante la registrazione")->{"Verifica attentamente di avere inserito l'indirizzo email e la password corretti. Se hai dimenticato la password inserisci l'indirizzo email,nella sezione dedicata, che ci hai fornito al momento della registrazione e riceverai una email per impostare una nuova password."}
            message.contains("modifica account")->      {"Scegli la tematica da approfondire oppure torna al menu precedente per selezionare un'altra opzione:(Modifica account)"}
            message.contains("reset password")->{"Se hai dimenticato la password inserisci nella pagina di login cliccando reset password " +
                    "l'indirizzo email che ci hai fornito al momento della registrazione e riceverai una email per impostare una nuova password."}
            message.contains("modifica dati personali?")->{"Puoi modificare i tuoi dati accedendo alla tua area personale. Nella sezione Profilo, e clicca su Modifica. Attraverso il bottone potrai modificare facilmente tutti i tuoi dati."}
            message.contains("registrazione al sito")->{"Scegli la tematica da approfondire oppure torna al menu precedente per selezionare un'altra opzione:(Registrazione al sito)"}
            message.contains("vorrei parlare con un operatore")->{"Per parlare con un operatore si prega di scrivere un email al seguente sito: speedMarket@gmail.com"}
            message.contains("supporto ad un ordine")->{"Scegli la tipologia di richiesta oppure torna al menu precedente per selezionare un'altra opzione:(ordine)"}
            message.contains("chiudi la chat")->{"Buona giornata,è stato un piacere aiutare un cliente di speedmarket"}
            message.contains("supporto creazione nuovo ordine")->{"Per creare un nuovo ordine potrai selezionare e aggiungere gli articoli al tuo carrello accedendo alla finestra Catalogo. Completato il carrello con tutti i prodotti di cui hai bisogno, sarà possibile procedere al pagamento seguendo i passaggi della procedura guidata."}
            message.contains("creazione dell ordine")->{"Scegli la tematica da approfondire oppure torna al menu precedente per selezionare un'altra opzione:(creazione ordine)"}
            message.contains("contestazione di un ordine")->{"Per contestare un ordine,si prega di inviare un'email al seguente indirizzo:speedmarket@gmail.com indicandone le motivazioni"}
            message.contains("salvataggio carrello e recupero lista  spesa")->{"Accedendo alla sezione profilo,nei tuoi ordini avrai la lista di tutti gli ordini che hai effettuato con la relativa lista della spesa e lo stato dell'ordine"}
            message.contains("ho bisogno di un'altra informazione")->{"Seleziona una delle seguenti opzioni per richiedere supporto:(ordine)"}
            else->{"Mi dispiace, non ho capito la tua richiesta."}
        }

        return "Mi dispiace, non ho capito la tua richiesta."
    }

}


