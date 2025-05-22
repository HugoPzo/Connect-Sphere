package unam.connectsphere.ui.register

data class RegisterUiState(
    // Datos de entrada del usuario
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val password: String = "",

    // Estados de la UI
    val isLoading: Boolean = false, // Indica si se está realizando la operación de registro
    val errorMessage: String = "", // Mensaje de error a mostrar durante el registro
    val isRegisterEnabled: Boolean = false // Indica si el botón de "Registrarse" está habilitado
)