package unam.connectsphere.ui.home

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import unam.connectsphere.models.Contact
import unam.connectsphere.ui.theme.CuriousBlue
import unam.connectsphere.ui.theme.EasternBlue
import unam.connectsphere.ui.theme.Ebony
import unam.connectsphere.ui.theme.PaleSlate
import unam.connectsphere.ui.theme.Trout

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    contacts: List<Contact>,
    onEditContact: (Contact) -> Unit,
    onAddContact: () -> Unit,
    onDeleteContact: (Contact) -> Unit,
    onLogout: () -> Unit
) {
    var searchQuery by remember { mutableStateOf(TextFieldValue("")) }
    val filteredContacts = contacts.filter {
        it.name.contains(searchQuery.text, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Contactos", color = Color.White) },
                actions = {
                    TextButton(onClick = onLogout) {
                        Text("Cerrar sesión", color = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = CuriousBlue
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddContact,
                containerColor = CuriousBlue,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Agregar contacto")
            }
        },
        containerColor = Ebony
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Buscar contacto", color = PaleSlate) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = PaleSlate,
                    unfocusedTextColor = PaleSlate,
                    cursorColor = CuriousBlue,
                    focusedContainerColor = Ebony,
                    unfocusedContainerColor = Ebony,
                    focusedIndicatorColor = EasternBlue,
                    unfocusedIndicatorColor = Trout
                )
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredContacts, key = { it.id }) { contact ->
                    val dismissState = rememberSwipeToDismissBoxState(
                        confirmValueChange = { value ->
                            if (value == SwipeToDismissBoxValue.StartToEnd) {
                                onDeleteContact(contact)
                                true
                            } else false
                        }
                    )

                    SwipeToDismissBox(
                        state = dismissState,
                        backgroundContent = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(start = 16.dp)
                                    .background(
                                        color = Color.Transparent,
                                        shape = RoundedCornerShape(20.dp),
                                    ),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Eliminar",
                                    tint = Color.White
                                )
                            }
                        },
                        content = {
                            ContactCard(
                                contact = contact,
                                onEditClick = { onEditContact(contact) }
                            )
                        }
                    )
                }

            }
        }
    }
}

@Composable
fun ContactCard(
    contact: Contact,
    onEditClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { expanded = !expanded },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2D3748)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .animateContentSize()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = rememberAsyncImagePainter(contact.photoUri),
                    contentDescription = "Foto de ${contact.name}",
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .padding(end = 12.dp),
                    contentScale = ContentScale.Crop
                )
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = contact.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 18.sp,
                        color = PaleSlate
                    )
                }
                IconButton(onClick = onEditClick) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar",
                        tint = CuriousBlue
                    )
                }
            }

            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(modifier = Modifier.padding(top = 8.dp)) {
                    Text(
                        "📞 ${contact.phone}",
                        fontSize = 14.sp,
                        color = PaleSlate.copy(alpha = 0.8f)
                    )
                    Text(
                        "✉️ ${contact.email}",
                        fontSize = 14.sp,
                        color = PaleSlate.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}
