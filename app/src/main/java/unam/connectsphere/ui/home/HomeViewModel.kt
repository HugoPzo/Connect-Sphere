import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import unam.connectsphere.models.Contact
import unam.connectsphere.repositories.ContactRepository

class HomeViewModel(
    private val userId: String,
    private val repository: ContactRepository = ContactRepository()
) : ViewModel() {

    private val _contacts = MutableStateFlow<List<Contact>>(emptyList())
    val contacts: StateFlow<List<Contact>> = _contacts.asStateFlow()

    init {
        loadContacts()
    }

    private fun loadContacts() {
        viewModelScope.launch {
            repository.getContactsForUser(userId)
                .catch { e -> e.printStackTrace() } // Puedes emitir errores al UI si deseas
                .collect { contactList ->
                    _contacts.value = contactList
                }
        }
    }

}
