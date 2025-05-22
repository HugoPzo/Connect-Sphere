package unam.connectsphere.ui.screens.splash

import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import unam.connectsphere.R

@Composable
fun SplashScreen(navController: NavController) {
    // Retraso para simular una pantalla de carga
    LaunchedEffect(true) {
        delay(2500) // 2.5 segundos
        navController.navigate("login") {
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_connectsphere),
            contentDescription = "Splash Logo",
            modifier = Modifier
                .size(450.dp)
                .padding(16.dp)
        )
    }
}
