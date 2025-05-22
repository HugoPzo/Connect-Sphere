import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import unam.connectsphere.R
import unam.connectsphere.ui.login.LoginViewModel
import unam.connectsphere.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    viewModel: LoginViewModel = viewModel(),
    onLoginSuccess: () -> Unit
) {
    val loginUiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Ebony) // Fondo oscuro
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
            text = "ConnectSphere",
            style = MaterialTheme.typography.headlineLarge,
            color = PaleSlate
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Email
        TextField(
            value = loginUiState.email,
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

        // Contraseña
        TextField(
            value = loginUiState.password,
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

        // Botón de login
        Button(
            onClick = {
                viewModel.loginUser(
                    onSuccess = onLoginSuccess,
                    onError = { /* Manejo de error opcional */ }
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = CuriousBlue,         // Color cuando está habilitado
                contentColor = Color.White,             // Color del texto cuando está habilitado
                disabledContainerColor = Color.LightGray, // Color cuando está deshabilitado
                disabledContentColor = Color.Gray        // Color del texto cuando está deshabilitado
            ),
            enabled = loginUiState.isLoginEnabled,
        ) {
            Text("Iniciar Sesión")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Registro
        TextButton(onClick = onNavigateToRegister) {
            Text("¿No tienes una cuenta? Regístrate aquí", color = CuriousBlue)
        }

        // Mensaje de error
        if (loginUiState.errorMessage.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = loginUiState.errorMessage, color = Cumin)
        }

        // Cargando
        if (loginUiState.isLoading) {
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator(color = CuriousBlue)
        }
    }
}
