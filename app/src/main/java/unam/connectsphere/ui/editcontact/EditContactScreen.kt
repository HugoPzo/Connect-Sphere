package unam.connectsphere.ui.editcontact

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.rememberAsyncImagePainter
import unam.connectsphere.R
import unam.connectsphere.models.Contact
import unam.connectsphere.ui.theme.CuriousBlue
import unam.connectsphere.ui.theme.EasternBlue
import unam.connectsphere.ui.theme.Ebony
import unam.connectsphere.ui.theme.PaleSlate
import unam.connectsphere.ui.theme.Trout
import unam.connectsphere.viewmodel.ContactsViewModel
import java.io.File
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults



private fun createImageUri(context: Context): Uri? {
    return try {
        val directory = File(context.filesDir, "photos")
        if (!directory.exists()) {
            directory.mkdirs()
        }

        val file = File(directory, "photo_${System.currentTimeMillis()}.jpg")
        FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    } catch (e: Exception) {
        Log.e("EditContactScreen", "Error creando URI de imagen: ${e.message}", e)
        null
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditContactScreen(
    contact: Contact,
    viewModel: ContactsViewModel,
    onContactUpdated: () -> Unit,
    onBack: () -> Unit
) {
    var name by remember { mutableStateOf(contact.name) }
    var phone by remember { mutableStateOf(contact.phone) }
    var email by remember { mutableStateOf(contact.email) }
    var photoUri by remember {
        mutableStateOf(contact.photoUri?.takeIf { it.isNotEmpty() }?.let { Uri.parse(it) })
    }

    val context = LocalContext.current
    var showPhotoOptions by remember { mutableStateOf(false) }
    var cameraImageUri by remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            photoUri = uri
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && cameraImageUri != null) {
            photoUri = cameraImageUri
        } else {
            Toast.makeText(context, "La foto no se tomó correctamente",
                Toast.LENGTH_SHORT).show()
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            val uri = createImageUri(context)
            if (uri != null) {
                cameraImageUri = uri
                cameraLauncher.launch(uri)
            } else {
                Toast.makeText(context, "No se pudo preparar la imagen para la cámara",
                    Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar contacto", color = PaleSlate) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_back),
                            contentDescription = "Volver",
                            tint = PaleSlate
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Ebony
                )
            )
        },
        containerColor = Ebony
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clickable { showPhotoOptions = true },
                contentAlignment = Alignment.Center
            ) {
                if (photoUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(photoUri),
                        contentDescription = "Foto del contacto",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.default_photo),
                        contentDescription = "Foto por defecto",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                    )
                }
            }

            Text(
                text = "Toca para agregar foto",
                color = CuriousBlue,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre", color = PaleSlate) },
                modifier = Modifier.fillMaxWidth(),
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

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Teléfono", color = PaleSlate) },
                modifier = Modifier.fillMaxWidth(),
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

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo", color = PaleSlate) },
                modifier = Modifier.fillMaxWidth(),
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

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    val updatedContact = contact.copy(
                        name = name,
                        phone = phone,
                        email = email,
                        photoUri = photoUri?.toString() ?: ""
                    )
                    viewModel.updateContact(updatedContact)
                    onContactUpdated()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CuriousBlue,
                    contentColor = Color.White
                )
            ) {
                Text("Guardar cambios")
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = {
                    viewModel.deleteContact(contact, context) {
                        // Acciones adicionales si falla (opcional)
                    }
                    onBack()
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White
                )
            ) {
                Text("Eliminar contacto")
            }


        }
    }

    if (showPhotoOptions) {
        AlertDialog(
            onDismissRequest = { showPhotoOptions = false },
            title = { Text("Cambiar foto", color = PaleSlate) },
            text = { Text("Selecciona cómo deseas actualizar la imagen.", color = PaleSlate) },
            containerColor = Ebony,
            confirmButton = {
                TextButton(
                    onClick = {
                        showPhotoOptions = false
                        permissionLauncher.launch(android.Manifest.permission.CAMERA)
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = CuriousBlue)
                ) {
                    Text("Tomar foto")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showPhotoOptions = false
                        galleryLauncher.launch("image/*")
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = CuriousBlue)
                ) {
                    Text("Elegir de galería")
                }
            }
        )
    }
}
