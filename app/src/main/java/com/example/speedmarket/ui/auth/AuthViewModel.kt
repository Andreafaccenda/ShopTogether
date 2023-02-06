package com.example.speedmarket.ui.auth


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.speedmarket.model.Utente
import com.example.speedmarket.repository.AuthRepository
import com.example.speedmarket.util.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import android.net.Uri
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


@HiltViewModel
class AuthViewModel @Inject constructor(
    val repository: AuthRepository
): ViewModel() {

    private val _register = MutableLiveData<UiState<String>>()
    val register: LiveData<UiState<String>>
        get() = _register

   private val _login = MutableLiveData<UiState<String>>()
    val login: LiveData<UiState<String>>
        get() = _login

    private val _forgotPassword = MutableLiveData<UiState<String>>()
    val forgotPassword: LiveData<UiState<String>>
        get() = _forgotPassword

    private val _updateUserInfo = MutableLiveData<UiState<String>>()
    val updateUserInfo: LiveData<UiState<String>>
        get() = _updateUserInfo

    private val _utente = MutableLiveData<UiState<Utente?>>()
    val utente: LiveData<UiState<Utente?>>
        get() = _utente

    private val _utenti = MutableLiveData<UiState<MutableList<Utente?>>>()
    val utenti: LiveData<UiState<MutableList<Utente?>>>
        get() = _utenti

    fun getUtente(id: String) {
        _utente.value = UiState.Loading
        repository.getUser(id) {
            _utente.value = it
        }
    }
    fun getListUser() {
        _utenti.value = UiState.Loading
        repository.getListUser {
            _utenti.value = it
        }
    }

    fun register(email: String,password: String, utente: Utente) {
        _register.value = UiState.Loading
        repository.registerUser(
            email = email,
            password = password,
            utente = utente
        ) { _register.value = it }
    }

    fun login(email: String, password: String) {
        _login.value = UiState.Loading
        repository.loginUser(
            email,
            password
        ){
            _login.value = it
        }
    }

    fun forgotPassword(email: String) {
        _forgotPassword.value = UiState.Loading
        repository.forgotPassword(email){
            _forgotPassword.value = it
        }
    }

    fun logout(result: () -> Unit){
        repository.logout(result)
    }

    fun getSession(result: (Utente?) -> Unit){
        repository.getSession(result)
    }
    fun updateUserInfo(utente:Utente){
        _updateUserInfo.value = UiState.Loading
        repository.updateUserInfo(utente) { _updateUserInfo.value = it }
    }

    fun uploadImage(uri: Uri, utente: Utente, result: (UiState<Uri>) -> Unit) {
        result.invoke(UiState.Loading)
        viewModelScope.launch {
            repository.uploadImage(uri, utente,result)
        }
    }

}
