package unam.connectsphere.models

data class Contact(
    val id: String = "",
    val name: String = "",
    val phone: String = "",
    val email: String = "",
    val photoUri: String? = null // Ruta local de la imagen
)
