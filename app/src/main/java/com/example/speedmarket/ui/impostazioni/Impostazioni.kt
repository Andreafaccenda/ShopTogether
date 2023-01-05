package com.example.speedmarket.ui.impostazioni

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.speedmarket.R
import com.example.speedmarket.databinding.FragmentHomeBinding
import com.example.speedmarket.databinding.FragmentImpostazioniBinding
import com.example.speedmarket.ui.impostazioni.assistenzaClienti.AssistenzaClientiFragment
import com.example.speedmarket.ui.impostazioni.profile.Profile
import com.example.speedmarket.util.replaceFragment
import com.example.speedmarket.util.setupOnBackPressed
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Impostazioni : Fragment() {
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


}