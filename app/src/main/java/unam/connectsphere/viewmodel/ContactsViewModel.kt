package unam.connectsphere.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import unam.connectsphere.models.Contact
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.UUID

class ContactsViewModel : ViewModel() {
    private val _contacts = MutableStateFlow<List<Contact>>(emptyList())
    val contacts: StateFlow<List<Contact>> get() = _contacts

    private var userId: String? = null
    private val db = FirebaseFirestore.getInstance()

    fun loadContacts(uid: String) {
        if (userId != uid) {
            userId = uid
            viewModelScope.launch {
                db.collection("users")
                    .document(uid)
                    .collection("contacts")
                    .get()
                    .addOnSuccessListener { result ->
                        val contactList = result.mapNotNull { doc ->
                            doc.toObject(Contact::class.java).copy(id = doc.id)
                        }
                        _contacts.value = contactList
                    }
                    .addOnFailureListener {
                        _contacts.value = emptyList()
                    }
            }
        }
    }

    fun addContact(contact: Contact) {
        userId?.let { uid ->
            val contactData = contact.copy(id = "") // Firebase asigna ID
            db.collection("users")
                .document(uid)
                .collection("contacts")
                .add(contactData)
                .addOnSuccessListener { docRef ->
                    val savedContact = contactData.copy(id = docRef.id)
                    _contacts.value = _contacts.value + savedContact
                }
        }
    }

    fun updateContact(updatedContact: Contact) {
        userId?.let { uid ->
            val contactId = updatedContact.id
            db.collection("users")
                .document(uid)
                .collection("contacts")
                .document(contactId)
                .set(updatedContact)
                .addOnSuccessListener {
                    _contacts.value = _contacts.value.map {
                        if (it.id == contactId) updatedContact else it
                    }
                }
        }
    }

    fun deleteContact(contact: Contact, context: Context, onFailure: (() -> Unit)? = null) {
        userId?.let { uid ->
            db.collection("users")
                .document(uid)
                .collection("contacts")
                .document(contact.id)
                .delete()
                .addOnSuccessListener {
                    _contacts.value = _contacts.value.filter { it.id != contact.id }
                }
                .addOnFailureListener { e ->
                    Log.e("ContactsViewModel", "Error al eliminar el contacto: ${e.message}", e)
                    Toast.makeText(context, "No se pudo eliminar el contacto", Toast.LENGTH_SHORT).show()
                    onFailure?.invoke()
                }
        } ?: run {
            Log.e("ContactsViewModel", "userId es nulo, no se puede eliminar el contacto")
            Toast.makeText(context, "Usuario no autenticado", Toast.LENGTH_SHORT).show()
            onFailure?.invoke()
        }
    }



    /**
     * Guarda una imagen localmente y devuelve su ruta absoluta como String
     */
    fun saveImageLocally(
        context: Context,
        imageUri: Uri,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri)
            if (inputStream == null) {
                onFailure(IOException("No se pudo abrir el input stream"))
                return
            }

            val fileName = "contact_${UUID.randomUUID()}.jpg"
            val imageFile = File(context.filesDir, fileName)
            val outputStream = FileOutputStream(imageFile)

            inputStream.copyTo(outputStream)
            inputStream.close()
            outputStream.close()

            onSuccess(imageFile.absolutePath)
        } catch (e: Exception) {
            onFailure(e)
        }
    }
}
