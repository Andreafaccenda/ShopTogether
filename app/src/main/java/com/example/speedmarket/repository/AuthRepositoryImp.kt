package com.example.speedmarket.repository


import android.content.SharedPreferences
import android.net.Uri
import com.example.speedmarket.model.Utente
import com.example.speedmarket.util.FireStoreCollection
import com.example.speedmarket.util.SharedPrefConstants
import com.example.speedmarket.util.UiState
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class AuthRepositoryImp(
    val auth: FirebaseAuth,
    val database: FirebaseFirestore,
    private val storage: StorageReference,
    private val appPreferences: SharedPreferences,
    private val gson: Gson,
) : AuthRepository {

    override fun registerUser(
        email: String,
        password: String,
        utente: Utente,
        result: (UiState<String>) -> Unit,
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    utente.id = it.result.user?.uid ?: ""
                    updateUserInfo(utente) { state ->
                        when (state) {
                            is UiState.Success -> {
                                result.invoke(
                                    UiState.Success("Utente registrato correttamente!")
                                )

                            }
                            is UiState.Failure -> {
                                result.invoke(UiState.Failure(state.error))
                            }
                        }
                    }
                } else {
                    try {
                        throw it.exception ?: java.lang.Exception("Autentificazione fallita")
                    } catch (e: FirebaseAuthWeakPasswordException) {
                        result.invoke(UiState.Failure("Autentificazione fallita,password dovrebbe essere lunga almeno 8 caratteri"))
                    } catch (e: FirebaseAuthInvalidCredentialsException) {
                        result.invoke(UiState.Failure("Autentificazione fallita,email inserita non valida"))
                    } catch (e: FirebaseAuthUserCollisionException) {
                        result.invoke(UiState.Failure("Autentificazione fallita,email già registrata."))
                    } catch (e: Exception) {
                        result.invoke(UiState.Failure(e.message))
                    }
                }
            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(
                        it.localizedMessage
                    )
                )
            }
    }

    override fun updateUserInfo(utente: Utente, result: (UiState<String>) -> Unit) {
        val document = database.collection(FireStoreCollection.UTENTI).document(utente.id)
        document
            .set(utente)
            .addOnSuccessListener {
                result.invoke(
                    UiState.Success("Utente modificato correttamente")
                )
            }
            .addOnFailureListener {
                result.invoke(
                    UiState.Failure(
                        it.localizedMessage
                    )
                )
            }
    }

    override fun getUser(id: String, result: (UiState<Utente?>) -> Unit) {
        database.collection(FireStoreCollection.UTENTI).document(id)
            .get()
            .addOnSuccessListener {
                val utente = it.toObject(Utente::class.java)
                result.invoke(UiState.Success(utente))
            }.addOnFailureListener {
                UiState.Failure(it.localizedMessage)
            }
    }

    override fun getListUser(result: (UiState<MutableList<Utente?>>) -> Unit) {

        val db = database.collection(FireStoreCollection.UTENTI)
        db.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val utenti = arrayListOf<Utente>()
                    for (field in document) {
                        val user = field.toObject(Utente::class.java)
                        utenti.add(user)
                    }
                    result.invoke(
                        UiState.Success(utenti.toMutableList())
                    )
                }
                else {
                    result.invoke(UiState.Failure("Non abbastanza utenti"))
                }
            }.addOnFailureListener {
                result.invoke(UiState.Failure(
                    it.localizedMessage))
            }
    }
    override fun loginUser(email: String, password: String, result: (UiState<String>) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    storeSession(id = task.result.user?.uid ?: "") {
                        if (it == null) {
                            result.invoke(UiState.Failure("Fallito"))
                        } else {
                            if(it.email == "staffspeedmarket@gmail.com"){result.invoke(UiState.Success("Login staff conad effettuato con successo!"))}
                            else{result.invoke(UiState.Success("Login effettuato con successo!"))}
                        }
                    }
                }
            }.addOnFailureListener {
                result.invoke(UiState.Failure("Autentificazione fallita,controlla email e password"))
            }
    }

    override fun forgotPassword(email: String, result: (UiState<String>) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    result.invoke(UiState.Success("Email inviata"))

                } else {
                    result.invoke(UiState.Failure(task.exception?.message))
                }
            }.addOnFailureListener {
                result.invoke(UiState.Failure("Autentificazione fallita, controlla email"))
            }
    }
    override fun logout(result: () -> Unit) {
        auth.signOut()
        appPreferences.edit().putString(SharedPrefConstants.USER_SESSION, null).apply()
        appPreferences.edit().putString(SharedPrefConstants.LOCAL_SHARED_PREF, null).apply()
        result.invoke()
    }

    override fun storeSession(id: String, result: (Utente?) -> Unit) {
        database.collection(FireStoreCollection.UTENTI).document(id)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful){
                    val user = it.result.toObject(Utente::class.java)
                    appPreferences.edit().putString(SharedPrefConstants.USER_SESSION,gson.toJson(user)).apply()
                    result.invoke(user)
                }else{
                    result.invoke(null)
                }
            }
            .addOnFailureListener {
                result.invoke(null)
            }
    }

    override fun getSession(result: (Utente?) -> Unit) {
        val userStr = appPreferences.getString(SharedPrefConstants.USER_SESSION,null)
        if (userStr == null){
            result.invoke(null)
        }else{
            val user = gson.fromJson(userStr,Utente::class.java)
            result.invoke(user)
        }
    }

    override suspend fun uploadImage(uri: Uri, utente: Utente, result: (UiState<Uri>) -> Unit) {
        try {
            val uriFile: Uri = withContext(Dispatchers.IO) {
                storage
                    .child(utente.id)
                    .putFile(uri)
                    .await()
                    .storage
                    .downloadUrl
                    .await()
            }
            utente.immagine_profilo = uriFile.toString()
            result.invoke(UiState.Success(uriFile))
        } catch (e: FirebaseException){
            result.invoke(UiState.Failure(e.message))
        }catch (e: Exception){
            result.invoke(UiState.Failure(e.message))
        }
    }

  /*  override suspend fun getImage(utente: Utente, result: (Bitmap?) -> Unit) {
        val localFile = withContext(Dispatchers.IO) {
            File.createTempFile("tempImage", "jpg")
        }
        storage
            .child(utente.id)
            .getFile(localFile).addOnSuccessListener {
                val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                result.invoke(bitmap)
        }.addOnFailureListener {
            result.invoke(null)
        }
    } */

    override suspend fun getImage(utente: Utente, result: (String?) -> Unit) {
        database.collection(FireStoreCollection.UTENTI).document(utente.id)
            .get().addOnSuccessListener {
                val user: Utente = it.toObject(Utente::class.java)!!
                result.invoke(user.immagine_profilo)
            }.addOnSuccessListener {
                result.invoke(null)
            }
    }

}