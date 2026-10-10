package com.example.midiariodeviajes

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.midiariodeviajes.ui.login.LoginScreen
import com.example.midiariodeviajes.ui.welcome.WelcomeScreen

private object Rutas {
    const val BIENVENIDA = "bienvenida"
    const val LOGIN = "login"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Rutas.BIENVENIDA
    ) {
        composable(Rutas.BIENVENIDA) {
            WelcomeScreen(
                onComenzarClick = {
                    navController.navigate(Rutas.LOGIN)
                }
            )
        }

        composable(Rutas.LOGIN) {
            LoginScreen(
                onCrearCuentaClick = {
                    // Añadiremos la pantalla de registro más adelante.
                }
            )
        }
    }
}
