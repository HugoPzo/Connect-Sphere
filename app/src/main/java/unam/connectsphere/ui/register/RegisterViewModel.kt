package unam.connectsphere.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun updateName(name: String) {
        _uiState.update {
            it.copy(name = name, isRegisterEnabled = isFormValid(name, it.email, it.phone, it.password))
        }
    }

    fun updateEmail(email: String) {
        _uiState.update {
            it.copy(email = email, isRegisterEnabled = isFormValid(it.name, email, it.phone, it.password))
        }
    }

    fun updatePhone(phone: String) {
        _uiState.update {
            it.copy(phone = phone, isRegisterEnabled = isFormValid(it.name, it.email, phone, it.password))
        }
    }

    fun updatePassword(password: String) {
        _uiState.update {
            it.copy(password = password, isRegisterEnabled = isFormValid(it.name, it.email, it.phone, password))
        }
    }

    private fun isFormValid(name: String, email: String, phone: String, password: String): Boolean {
        return name.isNotBlank() && email.isNotBlank() && phone.isNotBlank() && password.isNotBlank()
    }

    fun registerUser(onSuccess: () -> Unit, onError: (String) -> Unit) {
        _uiState.update { it.copy(isLoading = true, errorMessage = "") }

        val email = uiState.value.email
        val password = uiState.value.password
        val name = uiState.value.name
        val phone = uiState.value.phone

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                viewModelScope.launch {
                    if (task.isSuccessful) {
                        // Usuario creado, guarda datos adicionales en Firestore
                        val uid = auth.currentUser?.uid ?: return@launch
                        val userMap = mapOf(
                            "uid" to uid,
                            "name" to name,
                            "email" to email,
                            "phone" to phone
                        )
                        db.collection("users").document(uid).set(userMap)
                            .addOnSuccessListener {
                                _uiState.update { it.copy(isLoading = false) }
                                onSuccess()
                            }
                            .addOnFailureListener { e ->
                                _uiState.update { it.copy(isLoading = false, errorMessage = e.localizedMessage ?: "Error al guardar usuario") }
                                onError(e.localizedMessage ?: "Error al guardar usuario")
                            }
                    } else {
                        _uiState.update { it.copy(isLoading = false, errorMessage = task.exception?.localizedMessage ?: "Error al registrar usuario") }
                        onError(task.exception?.localizedMessage ?: "Error al registrar usuario")
                    }
                }
            }
    }
}
