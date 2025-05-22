package unam.connectsphere.navigation

import LoginScreen
import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import unam.connectsphere.ui.register.RegisterScreen
import unam.connectsphere.ui.home.HomeScreenWrapper
import unam.connectsphere.ui.addcontact.AddContactScreen
import unam.connectsphere.ui.editcontact.EditContactScreen
import unam.connectsphere.ui.screens.splash.SplashScreen
import unam.connectsphere.viewmodel.ContactsViewModel



@Composable
fun MainScreen() {
    val navController = rememberNavController()

    // ViewModel compartido
    val contactsViewModel: ContactsViewModel = viewModel()

    ConnectSphereNavGraph(
        navController = navController,
        contactsViewModel = contactsViewModel
    )
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun ConnectSphereNavGraph(
    navController: NavHostController,
    contactsViewModel: ContactsViewModel
) {
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(navController = navController)
        }
        composable("login") {
            LoginScreen(
                onNavigateToRegister = { navController.navigate("register") },
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        composable("register") {
            RegisterScreen(
                onNavigateToLogin = { navController.popBackStack() },
                onRegisterSuccess = {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        composable("home") {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId != null) {
                HomeScreenWrapper(
                    userId = userId,
                    viewModel = contactsViewModel,
                    onEditContact = { contact ->
                        navController.navigate("edit/${contact.id}")
                    },
                    onAddContact = {
                        navController.navigate("add")
                    },
                    onLogout = {
                        FirebaseAuth.getInstance().signOut() // ← Cierra sesión
                        navController.navigate("login") {    // ← Va a login
                            popUpTo("home") { inclusive = true }
                        }
                    }
                )
            } else {
                navController.navigate("login") {
                    popUpTo("home") { inclusive = true }
                }
            }
        }
        composable("add") {
            AddContactScreen(
                viewModel = contactsViewModel,
                onContactAdded = { contact ->
                    navController.popBackStack() // Volver a home y refrescar contactos
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(
            route = "edit/{contactId}",
            arguments = listOf(navArgument("contactId") { type = NavType.StringType })
        ) { backStackEntry ->
            val contactId = backStackEntry.arguments?.getString("contactId")
            val contact = contactsViewModel.contacts.value.find { it.id == contactId }

            if (contact != null) {
                EditContactScreen(
                    contact = contact,
                    viewModel = contactsViewModel,
                    onContactUpdated = {
                        navController.popBackStack() // Regresa a HomeScreen
                    },
                    onBack = {
                        navController.popBackStack()
                    }
                )
            } else {
                // Si no se encuentra el contacto, podrías mostrar un mensaje o redirigir
                navController.popBackStack()
            }
        }
    }
}
