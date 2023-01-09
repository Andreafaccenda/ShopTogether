package com.example.speedmarket.ui.impostazioni.profile

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import com.example.speedmarket.R
import com.example.speedmarket.databinding.FragmentProfileBinding
import com.example.speedmarket.model.Utente
import com.example.speedmarket.ui.auth.AuthViewModel
import com.example.speedmarket.ui.impostazioni.Impostazioni
import com.example.speedmarket.util.*
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Profile : Fragment() {

    companion  object{
        const val IMAGE_PICK_CODE = 100
        const val IMAGE_REQUEST_CODE = 100
        private val PERMISSION_CODE = 101
    }

    lateinit var binding: FragmentProfileBinding
    val viewModelAuth: AuthViewModel by viewModels()
    var modify:Boolean=false

    private lateinit var utente: Utente

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnBackPressedFragment(Impostazioni())
        getUtente()
        oberver()
        load_profile(this.utente)
        editText_modify(modify)
        binding.layoutUploadImage.setOnClickListener{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_DENIED) {
                    val permission = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permission, PERMISSION_CODE)
                } else { immagineGalleria() }
            } else {
                immagineGalleria()
            }
        }
        binding.etPassword.setOnClickListener{
            binding.etPassword.inputType= InputType.TYPE_CLASS_TEXT
        }
        binding.btnSave.setOnClickListener{
            binding.btnSave.setText(getString(R.string.salva))
            binding.btnSave.setCompoundDrawablesWithIntrinsicBounds(requireContext().resources.getDrawable(R.drawable.salva,context!!.theme), null, null, null)
            modify=true
            editText_modify(true)
            binding.etCitta.requestFocus()
            binding.btnSave.setOnClickListener{
                if(validation()) {
                    utente.numero_telefono = binding.etTelefono.text.toString().toLong()
                    utente.residenza = binding.etCitta.text.toString() + "-" +
                            binding.etCap.text.toString() + "-" +
                            binding.etVia.text.toString() + "-" +
                            binding.etNumeroCivico.text.toString()
                    if(binding.roundM.isChecked){utente.genere="Maschio"}
                    if(binding.roundF.isChecked){utente.genere="Femmina"}
                    viewModelAuth.updateUserInfo(utente)
                }
            }
        }

    }
    private fun oberver() {
        viewModelAuth.updateUserInfo.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                }
                is UiState.Failure -> {
                    toast(state.error)
                }
                is UiState.Success -> {
                    toast(state.data)

                }
            }
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            PERMISSION_CODE->{
                if(grantResults.size >0
                    && grantResults[0]
                    == PackageManager.PERMISSION_GRANTED){
                    immagineGalleria()
                }else{
                    toast("Permessi negati")
                }
            }
        }
    }


    private fun immagineGalleria() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type="image/*"
        startActivityForResult(intent,IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == IMAGE_REQUEST_CODE && resultCode==RESULT_OK){
            var imageUri: Uri? =data?.data
                utente.immagine_profilo=imageUri.toString()

        }
    }
    fun getUtente() {
        viewModelAuth.getSession { user ->
            if (user != null) {
                utente = user
            }
        }
    }
    fun load_profile(utente : Utente){

        binding.etNome.setText(utente.nome)
        binding.etCognome.setText(utente.cognome)
        if(utente.numero_telefono <=0){
            binding.etTelefono.setText("")
        }
        else {
            binding.etTelefono.setText(utente.numero_telefono.toString())
        }
        binding.etEmail.setText(utente.email)
        binding.etPassword.setText(utente.password)
        if(!utente.residenza.isNullOrEmpty()){
            binding.etCitta.setText(utente.residenza.split("-").get(0))
            binding.etCap.setText(utente.residenza.split("-").get(1))
            binding.etVia.setText(utente.residenza.split("-").get(2))
            binding.etNumeroCivico.setText(utente.residenza.split("-").get(3))

        }else{
            binding.etCitta.setText(utente.residenza)
            binding.etCap.setText(utente.residenza)
            binding.etVia.setText(utente.residenza)
            binding.etNumeroCivico.setText(utente.residenza)
        }
        //binding.imageProfile.setImageURI(Uri.parse(utente.immagine_profilo))

        /****
         * genera errore Looper
         *
         */
    }
    fun editText_modify(modify : Boolean){
        binding.etNome.isEnabled=false
        binding.etCognome.isEnabled=false
        binding.etTelefono.isEnabled=modify
        binding.etEmail.isEnabled=false
        binding.etPassword.isEnabled=false
        binding.roundM.isEnabled=modify
        binding.roundF.isEnabled=modify
        binding.etCitta.isEnabled=modify
        binding.etCap.isEnabled=modify
        binding.etVia.isEnabled=modify
        binding.etNumeroCivico.isEnabled=modify

    }
    fun validation(): Boolean {

        return when{
            binding.etTelefono.text.isNullOrEmpty()-> {toast(getString(R.string.messaggio_inserisci_telefono))
                false}
            binding.etCitta.text.isNullOrEmpty() -> {toast(getString(R.string.messaggio_inserisci_citta))
                false}
            utente.immagine_profilo.isNullOrEmpty() -> {toast(getString(R.string.messaggio_inserisci_immagine))
                false}
            !binding.roundM.isChecked && !binding.roundF.isChecked -> {toast(getString(R.string.messaggio_seleziona_genere))
                false}
            binding.etCap.text.isNullOrEmpty() -> { toast(getString(R.string.messaggio_cap))
                false}
            binding.etVia.text.isNullOrEmpty() -> {toast(getString(R.string.messaggio_via))
                false}
            binding.etNumeroCivico.text.isNullOrEmpty()->{toast(getString(R.string.messaggio_numerocivico))
                false}
            else ->{ true}
        }
        return true
    }




}