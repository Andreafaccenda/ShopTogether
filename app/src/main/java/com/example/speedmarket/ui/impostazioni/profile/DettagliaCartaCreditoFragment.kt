package com.example.speedmarket.ui.impostazioni.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.speedmarket.R
import com.example.speedmarket.databinding.FragmentCatalogoBinding
import com.example.speedmarket.databinding.FragmentDettagliProdottoBinding
import com.example.speedmarket.databinding.FragmentDettagliaCartaCreditoBinding
import com.example.speedmarket.util.setupOnBackPressed
import com.example.speedmarket.util.setupOnBackPressedFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DettagliaCartaCreditoFragment : Fragment() {

    lateinit var binding: FragmentDettagliaCartaCreditoBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDettagliaCartaCreditoBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnBackPressedFragment(Profile())

    }

}