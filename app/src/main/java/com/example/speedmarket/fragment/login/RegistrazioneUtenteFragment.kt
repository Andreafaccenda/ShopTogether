package com.example.speedmarket.fragment.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.speedmarket.R
import com.example.speedmarket.databinding.FragmentAccediBinding
import com.example.speedmarket.databinding.FragmentRegistrazioneUtenteBinding


class RegistrazioneUtenteFragment : Fragment() {

    lateinit var binding: FragmentRegistrazioneUtenteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegistrazioneUtenteBinding.inflate(inflater, container, false)
        return binding.root
    }

}