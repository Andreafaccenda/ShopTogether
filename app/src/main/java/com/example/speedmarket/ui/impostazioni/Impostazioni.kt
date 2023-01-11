package com.example.speedmarket.ui.impostazioni

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import coil.load
import com.example.speedmarket.databinding.FragmentImpostazioniBinding
import com.example.speedmarket.ui.auth.AuthViewModel
import com.example.speedmarket.ui.impostazioni.assistenzaClienti.AssistenzaClientiFragment
import com.example.speedmarket.ui.impostazioni.profile.Profile
import com.example.speedmarket.util.replaceFragment
import com.example.speedmarket.util.setupOnBackPressed
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Impostazioni : Fragment() {
    private val viewModelAuth: AuthViewModel by viewModels()
    lateinit var binding: FragmentImpostazioniBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentImpostazioniBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnBackPressed()
        binding.assistenzaClientiLayout.setOnClickListener{
            replaceFragment(AssistenzaClientiFragment())
        }
        binding.accountLayout.setOnClickListener {
            replaceFragment(Profile())
        }
    }

    override fun onStart() {
        super.onStart()
        viewModelAuth.getSession { user ->
            if (user != null) {
                binding.txtEmailUser.text = user.email
                binding.txtNome.text = "${user.nome} ${user.cognome}"
                bindImage(binding.imageProfile, user.immagine_profilo)
            }
        }
    }

    private fun bindImage(imgView: ImageView, imgUrl: String?) {
        imgUrl?.let {
            val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
            imgView.load(imgUri)
        }
    }

}