package unam.connectsphere.ui.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import unam.connectsphere.R
import unam.connectsphere.ui.theme.Cumin
import unam.connectsphere.ui.theme.CuriousBlue
import unam.connectsphere.ui.theme.EasternBlue
import unam.connectsphere.ui.theme.Ebony
import unam.connectsphere.ui.theme.PaleSlate
import unam.connectsphere.ui.theme.Trout

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit,
    viewModel: RegisterViewModel = viewModel()
) {
    val registerUiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Ebony) // Mismo fondo oscuro que LoginScreen
            .padding(16.dp)
            .padding(bottom = 55.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo
        Image(
            painter = painterResource(id = R.drawable.logo_connectsphere),
            contentDescription = "Logo de ConnectSphere",
            modifier = Modifier.width(280.dp),
            contentScale = ContentScale.Fit
        )

        Text(
            text = "Crear Cuenta",
            style = MaterialTheme.typography.headlineLarge,
            color = PaleSlate // Mismo color de texto
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Nombre
        TextField(
            value = registerUiState.name,
            onValueChange = { viewModel.updateName(it) },
            label = { Text("Nombre", color = PaleSlate) },
            colors = TextFieldDefaults.colors(
                focusedTextColor = PaleSlate,
                unfocusedTextColor = PaleSlate,
                cursorColor = CuriousBlue,
                focusedContainerColor = Ebony,
                unfocusedContainerColor = Ebony,
                focusedIndicatorColor = EasternBlue,
                unfocusedIndicatorColor = Trout
            )
            ,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Email
        TextField(
            value = registerUiState.email,
            onValueChange = { viewModel.updateEmail(it) },
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
        Spacer(modifier = Modifier.height(8.dp))

        // Teléfono
        TextField(
            value = registerUiState.phone,
            onValueChange = { viewModel.updatePhone(it) },
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

        // Contraseña
        TextField(
            value = registerUiState.password,
            onValueChange = { viewModel.updatePassword(it) },
            label = { Text("Contraseña", color = PaleSlate) },
            visualTransformation = PasswordVisualTransformation(),
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

        // Botón de registro
        Button(
            onClick = {
                viewModel.registerUser(
                    onSuccess = onRegisterSuccess,
                    onError = { /* Manejo de error opcional */ }
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = CuriousBlue,         // Color cuando está habilitado
                contentColor = Color.White,           // Color del texto cuando está habilitado
                disabledContainerColor = Color.LightGray, // Color cuando está deshabilitado
                disabledContentColor = Color.Gray     // Color del texto cuando está deshabilitado
            ),
            enabled = registerUiState.isRegisterEnabled && !registerUiState.isLoading
        ) {
            Text("Registrarse")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Enlace a Login
        TextButton(onClick = onNavigateToLogin) {
            Text(
                "¿Ya tienes una cuenta? Inicia sesión aquí",
                color = CuriousBlue // Mismo color que en LoginScreen
            )
        }

        // Mensaje de error
        if (registerUiState.errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = registerUiState.errorMessage,
                color = Cumin // Mismo color de error
            )
        }

        // Cargando
        if (registerUiState.isLoading) {
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator(color = CuriousBlue) // Mismo color de carga
        }
    }
}