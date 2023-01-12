package com.example.speedmarket.ui.impostazioni.profile

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.speedmarket.R
import com.example.speedmarket.databinding.FragmentProfileBinding
import com.example.speedmarket.model.Utente
import com.example.speedmarket.ui.MainActivity
import com.example.speedmarket.ui.ProfileManager
import com.example.speedmarket.ui.auth.AuthViewModel
import com.example.speedmarket.ui.impostazioni.Impostazioni
import com.example.speedmarket.util.*
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class Profile : Fragment(), ProfileManager {

    companion object {
        const val IMAGE_PICK_CODE = 100
        const val IMAGE_REQUEST_CODE = 100
        private const val PERMISSION_CODE = 101
    }

    lateinit var binding: FragmentProfileBinding
    private val viewModelAuth: AuthViewModel by viewModels()
    var modify: Boolean = false

    override var utente: Utente? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnBackPressedFragment(Impostazioni())
        getUserSession()
        getUserObserver()
        utente?.let { viewModelAuth.getUtente(it.id) }
        updateUserObserver()
        utente?.let { updateUI() }
        editTextModify(modify)
        binding.layoutUploadImage.setOnClickListener{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_DENIED) {
                    val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permission, PERMISSION_CODE)
                } else { immagineGalleria() }
            } else {
                immagineGalleria()
            }
        }
        binding.password.setOnClickListener {
            binding.etPassword.inputType = InputType.TYPE_CLASS_TEXT
        }
        binding.btnEsci.setOnClickListener{
                viewModelAuth.logout {
                        startActivity(Intent(requireContext(),MainActivity::class.java))}
        }
        binding.layoutPayment.setOnClickListener{
            replaceFragment(DettagliaCartaCreditoFragment())
        }
        binding.btnSave.setOnClickListener{
            binding.btnSave.text = getString(R.string.salva)
            binding.btnSave.setCompoundDrawablesWithIntrinsicBounds(requireContext().resources.getDrawable(R.drawable.salva,context!!.theme), null, null, null)
            modify=true
            editTextModify(true)
            binding.etCitta.requestFocus()
            binding.btnSave.setOnClickListener{
                if(validation()) {
                    utente?.profileCompleted=true
                    utente?.numero_telefono = binding.etTelefono.text.toString().toLong()
                    utente?.residenza!!.citta = binding.etCitta.text.toString()
                    utente?.residenza!!.provincia = binding.etProvincia.text.toString()
                    utente?.residenza!!.cap = binding.etCap.text.toString()
                    utente?.residenza!!.numero_civico = binding.etNumeroCivico.text.toString()
                    utente?.residenza!!.via = binding.etVia.text.toString()
                    if(binding.roundM.isChecked){utente?.genere ="Maschio"}
                    if(binding.roundF.isChecked){utente?.genere ="Femmina"}
                    utente?.let { it1 -> viewModelAuth.updateUserInfo(it1) }
                }
            }
        }

    }

    private fun updateUserObserver() {
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

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            PERMISSION_CODE->{
                if(grantResults.isNotEmpty()
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

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == IMAGE_REQUEST_CODE && resultCode==RESULT_OK){
            val imageUri: Uri? =data?.data
                utente?.immagine_profilo=imageUri.toString()
            if (imageUri != null) {
                uploadImage(imageUri)
            }
        }
    }

    private fun getUserSession() {
        viewModelAuth.getSession { user ->
            if (user != null)
                utente = user
        }
    }

    private fun uploadImage(uri: Uri) {
        viewModelAuth.uploadImage(uri, utente!!) { state ->
            when (state) {
                is UiState.Loading -> {
                    toast("Caricamento...")
                }
                is UiState.Failure -> {
                    toast(state.error)
                }
                is UiState.Success -> {
                    toast("Immagine caricata")
                    binding.imageProfile.setImageURI(uri)
                    utente?.let { viewModelAuth.updateUserInfo(it) }
                }
            }
        }
    }

    private fun getUserObserver() {
        viewModelAuth.utente.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                }
                is UiState.Failure -> {
                    toast(state.error)
                }
                is UiState.Success -> {
                    utente = state.data!!
                    updateUI()
                }
            }
        }
    }

    override fun updateUI() {
        binding.etNome.setText(utente?.nome)
        binding.etCognome.setText(utente?.cognome)
        if(utente?.numero_telefono!! <=0){
            binding.etTelefono.setText("")
        }
        else {
            binding.etTelefono.setText(utente?.numero_telefono.toString())
        }
        if (utente?.genere == "Maschio") {
            binding.roundM.isChecked = true
        }
        else if (utente?.genere == "Femmina") {
            binding.roundF.isChecked = true
        }
        binding.etEmail.setText(utente?.email)
        binding.etPassword.setText(utente?.password)
        binding.etCitta.setText(utente?.residenza!!.citta)
        binding.etProvincia.setText(utente?.residenza!!.provincia)
        binding.etCap.setText(utente?.residenza!!.cap)
        binding.etVia.setText(utente?.residenza!!.via)
        binding.etNumeroCivico.setText(utente?.residenza!!.numero_civico)
        bindImage(binding.imageProfile, utente?.immagine_profilo)
    }

    private fun editTextModify(modify : Boolean){
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
        binding.etProvincia.isEnabled=modify
        binding.etNumeroCivico.isEnabled=modify

    }

    private fun validation(): Boolean {
        return when{
            binding.etTelefono.text.isNullOrEmpty()-> {toast(getString(R.string.messaggio_inserisci_telefono))
                false}
            binding.etCitta.text.isNullOrEmpty() -> {toast(getString(R.string.messaggio_inserisci_citta))
                false}
            utente?.immagine_profilo!!.isEmpty() -> {toast(getString(R.string.messaggio_inserisci_immagine))
                false}
            !binding.roundM.isChecked && !binding.roundF.isChecked -> {toast(getString(R.string.messaggio_seleziona_genere))
                false}
            binding.etCap.text.isNullOrEmpty() -> { toast(getString(R.string.messaggio_cap))
                false}
            binding.etVia.text.isNullOrEmpty() -> {toast(getString(R.string.messaggio_via))
                false}
            binding.etProvincia.text.isNullOrEmpty() -> {toast(getString(R.string.messaggio_provincia))
                false}
            binding.etNumeroCivico.text.isNullOrEmpty()->{toast(getString(R.string.messaggio_numerocivico))
                false}
            else ->{ true}
        }
    }
}