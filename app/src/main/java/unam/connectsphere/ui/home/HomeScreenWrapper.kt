package unam.connectsphere.ui.home

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import unam.connectsphere.models.Contact
import unam.connectsphere.viewmodel.ContactsViewModel

@Composable
fun HomeScreenWrapper(
    userId: String,
    viewModel: ContactsViewModel,
    onEditContact: (Contact) -> Unit,
    onAddContact: () -> Unit,
    onLogout: () -> Unit // ← Agregado
) {
    val contacts by viewModel.contacts.collectAsState()
    val context = LocalContext.current  // ← Añadido aquí

    LaunchedEffect(userId, contacts.isEmpty()) {
        if (contacts.isEmpty()) {
            viewModel.loadContacts(userId)
        }
    }

    HomeScreen(
        contacts = contacts,
        onEditContact = { contact -> onEditContact(contact) },
        onAddContact = onAddContact,
        onDeleteContact = { contact -> viewModel.deleteContact(contact, context) },
        onLogout = onLogout // ← Nuevo
    )

}

