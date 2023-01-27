package com.example.speedmarket.ui.impostazioni.assistenzaClienti

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.speedmarket.R
import com.example.speedmarket.databinding.FragmentAssistenzaClientiBinding
import com.example.speedmarket.model.Messaggio
import com.example.speedmarket.model.Utente
import com.example.speedmarket.ui.ProfileManager
import com.example.speedmarket.ui.auth.AuthViewModel
import com.example.speedmarket.ui.impostazioni.Impostazioni
import com.example.speedmarket.util.*
import com.example.speedmarket.util.Constants.CLICK_ID
import com.example.speedmarket.util.Constants.RECEIVE_ID
import com.example.speedmarket.util.Constants.SEND_ID
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*

@AndroidEntryPoint
class AssistenzaClientiFragment : Fragment() {

    lateinit var binding:FragmentAssistenzaClientiBinding
    private lateinit var adapter: MessaggioAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAssistenzaClientiBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnBackPressedFragment(Impostazioni())
        recycleView()
        clickEvents()
        customMessage(getString(R.string.message_default))
        adapter.onItemClick = {
            adapter.insertMessage(Messaggio(it, SEND_ID))
            binding.rvMessage.scrollToPosition(adapter.itemCount-1)
            botResponse(it)
        }

    }

    private fun clickEvents() {
        binding.btnSend.setOnClickListener{
            sendMessage()
        }
        binding.etMessage.setOnClickListener{
            GlobalScope.launch {
                delay(1000)
                withContext(Dispatchers.Main){

                    binding.rvMessage.scrollToPosition(adapter.itemCount-1)
                }

            }
        }
    }

    private fun recycleView(){
        adapter = MessaggioAdapter()
        binding.rvMessage.adapter = adapter
        binding.rvMessage.layoutManager= LinearLayoutManager(requireContext())
    }
    private fun customMessage(messaggio :String) {
        GlobalScope.launch {
            delay(1000)
            withContext(Dispatchers.Main) {

                adapter.insertMessage(Messaggio(messaggio, RECEIVE_ID))
                adapter.insertMessage(Messaggio("Supporto profilo online", CLICK_ID))
                adapter.insertMessage(Messaggio("Supporto ad un ordine", CLICK_ID))
                binding.rvMessage.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }


    private fun sendMessage(){
        val message = binding.etMessage.text.toString()
        if(message.isNotEmpty()){
            binding.etMessage.text.clear()
            adapter.insertMessage(Messaggio(message, SEND_ID))
            binding.rvMessage.scrollToPosition(adapter.itemCount-1)

            botResponse(message)
        }

    }
    private fun default_message(){
        adapter.insertMessage(Messaggio("Supporto profilo online", CLICK_ID))
        adapter.insertMessage(Messaggio("Supporto ad un ordine", CLICK_ID))
    }
    private fun second_default_message(){
        adapter.insertMessage(Messaggio("Hai bisogno di ulteriore assistenza o preferisci terminare la nostra conversazione?",
            RECEIVE_ID))
        adapter.insertMessage(Messaggio("Vorrei parlare con un operatore", CLICK_ID))
        adapter.insertMessage(Messaggio("Ho bisogno di un'altra informazione", CLICK_ID))
        adapter.insertMessage(Messaggio("Chiudi la chat", CLICK_ID))
    }
    private fun botResponse(message: String){
        GlobalScope.launch {
            delay(1000)
            withContext(Dispatchers.Main){

                val response = BotResponse.basicResponses(message)
                adapter.insertMessage(Messaggio(response, RECEIVE_ID))
                binding.rvMessage.scrollToPosition(adapter.itemCount-1)
                when(response){
                    "Scegli la tipologia di richiesta oppure torna al menu precedente per selezionare un'altra opzione:(profilo online)"->{
                        adapter.insertMessage(Messaggio("Registrazione al sito", CLICK_ID))
                        adapter.insertMessage(Messaggio("Modifica account", CLICK_ID))
                        adapter.insertMessage(Messaggio("Reset password", CLICK_ID))
                        adapter.insertMessage(Messaggio("Torna al menu precedente", CLICK_ID))
                    }
                    "Scegli la tematica da approfondire oppure torna al menu precedente per selezionare un'altra opzione:(Registrazione al sito)"->{
                        adapter.insertMessage(Messaggio("Come mi registro al sito?", CLICK_ID))
                        adapter.insertMessage(Messaggio("Problemi durante la registrazione", CLICK_ID))
                        adapter.insertMessage(Messaggio("Torna al menu precedente", CLICK_ID))
                    }
                    "Scegli la tematica da approfondire oppure torna al menu precedente per selezionare un'altra opzione:(Modifica account)"->{
                        adapter.insertMessage(Messaggio("Modifica dati personali?", CLICK_ID))
                        adapter.insertMessage(Messaggio("Torna al menu precedente", CLICK_ID))
                    }
                    "Se hai dimenticato la password inserisci nella pagina di login cliccando reset password " +
                            "l'indirizzo email che ci hai fornito al momento della registrazione e " +
                            "riceverai una email per impostare una nuova password."->{second_default_message()}
                    "Puoi modificare i tuoi dati accedendo alla tua area personale. Nella sezione Profilo, e clicca su Modifica. " +
                            "Attraverso il bottone potrai modificare facilmente tutti i tuoi dati."->{second_default_message()}
                    "Per registrarti al sito clicca su Registrati in basso a destra nella finestra di Login e successivamente su " +
                            "Prosegui in fondo alla pagina. Segui i passaggi della procedura guidata e, una volta terminati, " +
                            "riceverai una email per completare la registrazione."->{second_default_message()}
                    "Verifica attentamente di avere inserito l'indirizzo email e la password corretti. Se hai dimenticato la password inserisci l'indirizzo email,nella sezione dedicata, " +
                            "che ci hai fornito al momento della registrazione e riceverai una email per impostare una nuova password."->{second_default_message()}
                    "Seleziona una delle seguenti opzioni per richiedere supporto:"->{default_message()}
                    "Mi dispiace, non ho capito la tua richiesta."->{default_message()}
                    "Buona giornata,è stato un piacere aiutare un cliente di speedmarket"->{ binding.btnSend.text="Chiudi"
                        binding.btnSend.setOnClickListener{
                            replaceFragment(Impostazioni())
                        }}
                    "Scegli la tipologia di richiesta oppure torna al menu precedente per selezionare un'altra opzione:(ordine)"->{
                        adapter.insertMessage(Messaggio("Creazione dell ordine", CLICK_ID))
                        adapter.insertMessage(Messaggio("Contestazione di un ordine", CLICK_ID))
                        adapter.insertMessage(Messaggio("Torna al menu precedente", CLICK_ID))
                    }
                    "Scegli la tematica da approfondire oppure torna al menu " +
                            "precedente per selezionare un'altra opzione:(creazione ordine)"->{
                        adapter.insertMessage(Messaggio("Supporto creazione nuovo ordine", CLICK_ID))
                        adapter.insertMessage(Messaggio("Salvataggio carrello e recupero lista spesa ", CLICK_ID))
                        adapter.insertMessage(Messaggio("Torna al menu precedente", CLICK_ID)) }
                    "Per creare un nuovo ordine potrai selezionare e aggiungere gli articoli al tuo carrello accedendo alla finestra Catalogo. Completato il carrello con tutti " +
                            "i prodotti di cui hai bisogno, sarà possibile procedere al pagamento seguendo i passaggi della procedura guidata."->{second_default_message()}
                    "Accedendo alla sezione profilo,nei tuoi ordini avrai la lista di tutti gli ordini che hai effettuato con la relativa lista della spesa e lo stato dell'ordine"->{second_default_message()}
                    "Per contestare un ordine,si prega di inviare un'email al seguente indirizzo:speedmarket@gmail.com indicandone le motivazioni"->{second_default_message()}
                }



            }
        }
    }


    override fun onStart() {
        super.onStart()
        GlobalScope.launch {
            delay(1000)
            withContext(Dispatchers.Main){

                binding.rvMessage.scrollToPosition(adapter.itemCount-1)
            }
        }

    }





}