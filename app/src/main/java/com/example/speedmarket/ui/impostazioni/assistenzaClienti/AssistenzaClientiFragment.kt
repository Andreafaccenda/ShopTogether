package com.example.speedmarket.ui.impostazioni.assistenzaClienti

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.toSpanned
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.speedmarket.R
import com.example.speedmarket.databinding.FragmentAssistenzaClientiBinding
import com.example.speedmarket.model.Messaggio
import com.example.speedmarket.ui.impostazioni.Impostazioni
import com.example.speedmarket.util.*
import com.example.speedmarket.util.Constants.CLICK_ID
import com.example.speedmarket.util.Constants.OPEN_GOOGLE
import com.example.speedmarket.util.Constants.OPEN_SEARCH
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
    private fun customMessage(messaggio :String){
        GlobalScope.launch {
            delay(1000)
            withContext(Dispatchers.Main){

                adapter.insertMessage(Messaggio(messaggio,RECEIVE_ID))
                binding.rvMessage.scrollToPosition(adapter.itemCount-1)
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
    private fun botResponse(message: String){
        GlobalScope.launch {
            delay(1000)
            withContext(Dispatchers.Main){

                val response = BotResponse.basicResponses(message)
                adapter.insertMessage(Messaggio(response, RECEIVE_ID))
                binding.rvMessage.scrollToPosition(adapter.itemCount-1)
                when(response){
                        requireContext().getString(R.string.assistenza_modifica_profilo)->{
                            adapter.insertMessage(Messaggio(requireContext().getString(R.string.default_ass), RECEIVE_ID))
                        }
                    requireContext().getString(R.string.assistenza_registrazione_profilo)-> {
                        adapter.insertMessage(Messaggio(requireContext().getString(R.string.default_ass), RECEIVE_ID))
                    }
                    "Se hai dimenticato la password recati alla pagina di Login e clicca il bottone Reset password, inserisci l'indirizzo email che ci hai fornito al momento della registrazione e riceverai una email per impostare una nuova password."-> {
                        adapter.insertMessage(Messaggio(requireContext().getString(R.string.default_ass), RECEIVE_ID))
                    }
                    requireContext().getString(R.string.chiudi_la_chat)->{
                        binding.btnSend.text="Chiudi la chat"
                        binding.etMessage.hide()
                        binding.btnSend.setOnClickListener{
                            replaceFragment(Impostazioni())
                        }
                    }
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