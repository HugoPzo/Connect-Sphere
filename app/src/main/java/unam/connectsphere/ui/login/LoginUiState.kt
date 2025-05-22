package unam.connectsphere.ui.login

data class LoginUiState(
    // Datos de entrada del usuario
    val email: String = "",
    val password: String = "",

    // Estados de la UI
    val isLoading: Boolean = false, // Indica si se está realizando una operación (ej. inicio de sesión)
    val errorMessage: String = "", // Mensaje de error a mostrar al usuario (si ocurre)
    val isLoginEnabled: Boolean = false // Indica si el botón de inicio de sesión está habilitado
)