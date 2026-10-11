package com.example.midiariodeviajes

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.midiariodeviajes.ui.login.LoginScreen
import com.example.midiariodeviajes.ui.register.RegisterScreen
import com.example.midiariodeviajes.ui.welcome.WelcomeScreen
import com.example.midiariodeviajes.ui.viajes.MisViajesScreen

private object Rutas {
    const val BIENVENIDA = "bienvenida"
    const val LOGIN = "login"
    const val REGISTRO = "registro"
    const val MIS_VIAJES = "mis_viajes"
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
                onIniciarSesionClick = { _, _ ->
                    // TEMPORAL: acceso de prueba sin autenticar.
                    // Al conectar la API, sustituir por el resultado
                    // del inicio de sesión real.
                    navController.navigate(Rutas.MIS_VIAJES)
                },
                onCrearCuentaClick = {
                    navController.navigate(Rutas.REGISTRO)
                }
            )
        }

        composable(Rutas.REGISTRO) {
            RegisterScreen(
                onIniciarSesionClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(Rutas.MIS_VIAJES) {
            MisViajesScreen(
                onVolverClick = {
                    navController.popBackStack()
                },
                onPerfilClick = {
                    // Más adelante añadiremos el perfil y el cierre de sesión.
                },
                onAnadirViajeClick = {
                    // Más adelante añadiremos el formulario para crear viajes.
                },
                onDetalleViajeClick = { pais ->
                    // Más adelante conectaremos esta acción con el detalle del viaje.
                }
            )
        }

    }
}
