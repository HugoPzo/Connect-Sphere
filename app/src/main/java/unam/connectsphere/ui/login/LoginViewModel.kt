package unam.connectsphere.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    private val auth = FirebaseAuth.getInstance()

    fun updateEmail(email: String) {
        _uiState.update { it.copy(email = email, isLoginEnabled = isFormValid(email, it.password)) }
    }

    fun updatePassword(password: String) {
        _uiState.update { it.copy(password = password, isLoginEnabled = isFormValid(it.email, password)) }
    }

    private fun isFormValid(email: String, password: String): Boolean {
        return email.isNotBlank() && password.isNotBlank()
    }

    fun loginUser(onSuccess: () -> Unit, onError: (String) -> Unit) {
        _uiState.update { it.copy(isLoading = true, errorMessage = "") }

        auth.signInWithEmailAndPassword(uiState.value.email, uiState.value.password)
            .addOnCompleteListener { task ->
                viewModelScope.launch {
                    _uiState.update { it.copy(isLoading = false) }

                    if (task.isSuccessful) {
                        onSuccess()
                    } else {
                        val message = task.exception?.localizedMessage ?: "Error al iniciar sesión"
                        _uiState.update { it.copy(errorMessage = message) }
                        onError(message)
                    }
                }
            }
    }
}
