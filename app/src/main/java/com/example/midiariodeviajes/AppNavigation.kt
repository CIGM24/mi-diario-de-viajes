package com.example.midiariodeviajes

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.midiariodeviajes.ui.login.LoginScreen
import com.example.midiariodeviajes.ui.register.RegisterScreen
import com.example.midiariodeviajes.ui.welcome.WelcomeScreen
import com.example.midiariodeviajes.ui.viajes.MisViajesScreen
import com.example.midiariodeviajes.ui.viajes.formulario.AnadirViajeScreen
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.midiariodeviajes.data.AuthRepository
import kotlinx.coroutines.launch
import com.example.midiariodeviajes.data.model.ViajeDto
import com.example.midiariodeviajes.data.api.RetrofitClient

private object Rutas {
    const val BIENVENIDA = "bienvenida"
    const val LOGIN = "login"
    const val REGISTRO = "registro"
    const val MIS_VIAJES = "mis_viajes"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val authRepository = remember { AuthRepository() }
    val coroutineScope = rememberCoroutineScope()
    var mensajeError by remember { mutableStateOf<String?>(null) }
    var tokenJwt by remember { mutableStateOf<String?>(null) }
    var viajes by remember { mutableStateOf<List<ViajeDto>>(emptyList()) }

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
                mensajeError = mensajeError,
                onIniciarSesionClick = { correo, contrasena ->
                    mensajeError = null

                    coroutineScope.launch {
                        val resultado = authRepository.iniciarSesion(
                            correo,
                            contrasena
                        )

                        resultado
                            .onSuccess { token ->
                                tokenJwt = token

                                val respuestaViajes = RetrofitClient.viajesApi
                                    .obtenerViajes("Bearer $token")

                                if (respuestaViajes.isSuccessful) {
                                    viajes = respuestaViajes.body() ?: emptyList()
                                    navController.navigate(Rutas.MIS_VIAJES)
                                } else {
                                    mensajeError = "No se pudieron cargar los viajes (${respuestaViajes.code()})."
                                }
                            }
                            .onFailure { error ->
                                mensajeError = error.message
                                    ?: "No se pudo iniciar sesión."
                            }
                    }
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
                viajes = viajes,
                onVolverClick = {
                    navController.popBackStack()
                },
                onPerfilClick = {
                    // Más adelante añadiremos el perfil y el cierre de sesión.
                },
                onAnadirViajeClick = {
                    navController.navigate("anadir_viaje")
                },
                onDetalleViajeClick = { pais ->
                    // Más adelante conectaremos esta acción con el detalle del viaje.
                }
            )
        }

        composable("anadir_viaje") {
            AnadirViajeScreen(
                onVolverClick = {
                    navController.popBackStack()
                },
                onGuardarClick = {
                    // Más adelante conectaremos el guardado real.
                }
            )
        }

    }
}
