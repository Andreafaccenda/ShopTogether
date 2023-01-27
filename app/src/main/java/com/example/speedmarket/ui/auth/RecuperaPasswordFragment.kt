package com.example.speedmarket.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.speedmarket.R
import com.example.speedmarket.databinding.FragmentRecuperaPasswordBinding
import com.example.speedmarket.util.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecuperaPasswordFragment : Fragment() {

        lateinit var binding: FragmentRecuperaPasswordBinding
        val viewModel: AuthViewModel by viewModels()

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View {
            binding = FragmentRecuperaPasswordBinding.inflate(layoutInflater)
            return binding.root
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            observer()
            binding.btnInviaEmail.setOnClickListener {
                if (validation()){
                    viewModel.forgotPassword(binding.etEmail.text.toString())
                }
            }
        }

        private fun observer(){
            viewModel.forgotPassword.observe(viewLifecycleOwner) { state ->
                when(state){
                    is UiState.Loading -> {
                        binding.btnInviaEmail.text=""
                        binding.recuperaPasswordProgressBar.show()
                    }
                    is UiState.Failure -> {
                        binding.btnInviaEmail.text="Invia email"
                        binding.recuperaPasswordProgressBar.hide()
                        toast(state.error)
                    }
                    is UiState.Success -> {
                        binding.btnInviaEmail.text="Invia email"
                        binding.recuperaPasswordProgressBar.hide()
                        toast(state.data)
                        view?.findNavController()?.navigate(R.id.action_recuperaPasswordFragment_to_accediFragment)
                    }
                }
            }
        }

        fun validation(): Boolean {
            var isValid = true

            if (binding.etEmail.text.isNullOrEmpty()){
                isValid = false
                toast(getString(R.string.inserisci_email))
            }else{
                if (!binding.etEmail.text.toString().isValidEmail()){
                    isValid = false
                    toast(getString(R.string.email_invalida))
                }
            }

            return isValid
        }


    }