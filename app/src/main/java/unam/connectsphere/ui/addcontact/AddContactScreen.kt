package unam.connectsphere.ui.addcontact

import android.Manifest
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import coil.compose.rememberAsyncImagePainter
import unam.connectsphere.R
import unam.connectsphere.models.Contact
import unam.connectsphere.ui.theme.Cumin
import unam.connectsphere.ui.theme.CuriousBlue
import unam.connectsphere.ui.theme.EasternBlue
import unam.connectsphere.ui.theme.Ebony
import unam.connectsphere.ui.theme.PaleSlate
import unam.connectsphere.ui.theme.Trout
import unam.connectsphere.viewmodel.ContactsViewModel
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddContactScreen(
    viewModel: ContactsViewModel,
    onContactAdded: (Contact) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    var showPhotoOptions by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    var cameraImageUri by remember { mutableStateOf<Uri?>(null) }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        photoUri = uri
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            photoUri = cameraImageUri
        }
    }

    fun createImageUri(context: Context): Uri {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val directory = File(context.filesDir, "photos")
        if (!directory.exists()) {
            directory.mkdirs()
        }
        val file = File(directory, "IMG_${timeStamp}.jpg")
        return FileProvider.getUriForFile(context, "${context.packageName}.provider", file)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val uri = createImageUri(context)
            cameraImageUri = uri
            cameraLauncher.launch(uri)
        } else {
            errorMessage = "Permiso de cámara denegado"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Ebony)
            .padding(16.dp)
            .padding(bottom = 55.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Foto del contacto
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
                        .aspectRatio(1f)
                        .clip(CircleShape)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.default_photo),
                    contentDescription = "Foto por defecto",
                    modifier = Modifier
                        .size(120.dp)
                        .aspectRatio(1f)
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

        // Nombre
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nombre", color = PaleSlate) },
            colors = TextFieldDefaults.colors(
                focusedTextColor = PaleSlate,
                unfocusedTextColor = PaleSlate,
                cursorColor = CuriousBlue,
                focusedContainerColor = Ebony,
                unfocusedContainerColor = Ebony,
                focusedIndicatorColor = EasternBlue,
                unfocusedIndicatorColor = Trout
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Teléfono
        TextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Teléfono", color = PaleSlate) },
            colors = TextFieldDefaults.colors(
                focusedTextColor = PaleSlate,
                unfocusedTextColor = PaleSlate,
                cursorColor = CuriousBlue,
                focusedContainerColor = Ebony,
                unfocusedContainerColor = Ebony,
                focusedIndicatorColor = EasternBlue,
                unfocusedIndicatorColor = Trout
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Correo
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo Electrónico", color = PaleSlate) },
            colors = TextFieldDefaults.colors(
                focusedTextColor = PaleSlate,
                unfocusedTextColor = PaleSlate,
                cursorColor = CuriousBlue,
                focusedContainerColor = Ebony,
                unfocusedContainerColor = Ebony,
                focusedIndicatorColor = EasternBlue,
                unfocusedIndicatorColor = Trout
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Botón de guardar
        Button(
            onClick = {
                if (name.isBlank()) {
                    errorMessage = "El nombre es requerido"
                    return@Button
                }

                val newContact = Contact(
                    id = UUID.randomUUID().toString(),
                    name = name,
                    phone = phone,
                    email = email,
                    photoUri = photoUri?.toString() ?: ""
                )
                viewModel.addContact(newContact)
                onContactAdded(newContact)

            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = CuriousBlue,
                contentColor = Color.White,
                disabledContainerColor = Color.LightGray,
                disabledContentColor = Color.Gray
            )
        ) {
            Text("Guardar contacto")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón de volver
        TextButton(onClick = onBack) {
            Text(
                "Volver",
                color = CuriousBlue
            )
        }

        // Mensaje de error
        if (errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = errorMessage,
                color = Cumin
            )
        }
    }

    if (showPhotoOptions) {
        AlertDialog(
            onDismissRequest = { showPhotoOptions = false },
            title = { Text("Seleccionar imagen", color = PaleSlate) },
            text = {
                Column {
                    Text("Elige cómo deseas añadir una imagen.", color = PaleSlate)
                }
            },
            containerColor = Ebony,
            confirmButton = {
                TextButton(onClick = {
                    showPhotoOptions = false
                    permissionLauncher.launch(Manifest.permission.CAMERA)
                }) {
                    Text("Tomar foto", color = CuriousBlue)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showPhotoOptions = false
                    galleryLauncher.launch("image/*")
                }) {
                    Text("Elegir de galería", color = CuriousBlue)
                }
            }
        )
    }
}