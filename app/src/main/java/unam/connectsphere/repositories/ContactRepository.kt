package unam.connectsphere.repositories

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import unam.connectsphere.models.Contact

class ContactRepository {

    private val db = FirebaseFirestore.getInstance()

    fun getContactsForUser(userId: String): Flow<List<Contact>> = callbackFlow {
        val listener = db.collection("users")
            .document(userId)
            .collection("contacts")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val contacts = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Contact::class.java)?.copy(id = doc.id)
                } ?: emptyList()

                trySend(contacts)
            }

        awaitClose { listener.remove() }
    }

    fun addContact(userId: String, contact: Contact) {

    }
}
