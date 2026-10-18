package com.example.midiariodeviajes

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.midiariodeviajes.data.AuthRepository
import com.example.midiariodeviajes.data.RegistroRepository
import com.example.midiariodeviajes.data.api.RetrofitClient
import com.example.midiariodeviajes.data.model.ViajeDto
import com.example.midiariodeviajes.ui.login.LoginScreen
import com.example.midiariodeviajes.ui.register.RegisterScreen
import com.example.midiariodeviajes.ui.viajes.MisViajesScreen
import com.example.midiariodeviajes.ui.viajes.formulario.AnadirViajeScreen
import com.example.midiariodeviajes.ui.welcome.WelcomeScreen
import kotlinx.coroutines.launch
import androidx.compose.material3.Text
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.midiariodeviajes.data.SesionUsuario
import com.example.midiariodeviajes.ui.viajes.DetalleViajeScreen

private object Rutas {
    const val BIENVENIDA = "bienvenida"
    const val LOGIN = "login"
    const val REGISTRO = "registro"
    const val MIS_VIAJES = "mis_viajes"
    const val DETALLE_VIAJE = "detalle_viaje"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val authRepository = remember { AuthRepository() }
    val registroRepository = remember { RegistroRepository() }
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
                                SesionUsuario.tokenJwt = token

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
            var mensajeErrorRegistro by remember {
                mutableStateOf<String?>(null)
            }
            var mensajeExitoRegistro by remember {
                mutableStateOf<String?>(null)
            }

            RegisterScreen(
                onRegistrarseClick = { nombre, correo, contrasena ->
                    mensajeErrorRegistro = null
                    mensajeExitoRegistro = null

                    coroutineScope.launch {
                        val resultado = registroRepository.registrarUsuario(
                            nombre = nombre,
                            email = correo,
                            password = contrasena
                        )

                        resultado
                            .onSuccess {
                                mensajeExitoRegistro =
                                    "¡Cuenta creada! Ya puedes iniciar sesión."
                            }
                            .onFailure { error ->
                                mensajeErrorRegistro =
                                    error.message ?: "No se pudo crear la cuenta."
                            }
                    }
                },
                onIniciarSesionClick = {
                    navController.popBackStack()
                },
                mensajeError = mensajeErrorRegistro,
                mensajeExito = mensajeExitoRegistro

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
                onDetalleViajeClick = { idViaje ->
                    navController.navigate("${Rutas.DETALLE_VIAJE}/$idViaje")
                }
            )
        }

        composable("${Rutas.DETALLE_VIAJE}/{idViaje}") { backStackEntry ->

            val idViaje = backStackEntry.arguments?.getString("idViaje")

            val viajeSeleccionado = viajes.find {
                it.id == idViaje
            }

            if (viajeSeleccionado != null) {
                DetalleViajeScreen(
                    viaje = viajeSeleccionado,
                    onVolverClick = {
                        navController.popBackStack()
                    },
                    onEditarClick = {
                        navController.navigate("editar_viaje/${viajeSeleccionado.id}")
                    }
                )
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFFFFCF5))
                        .padding(24.dp)
                ) {
                    Text(
                        text = "No se ha encontrado el viaje seleccionado.",
                        color = Color(0xFF263D32)
                    )

                    Button(
                        onClick = {
                            navController.popBackStack()
                        }
                    ) {
                        Text("Volver a mis viajes")
                    }
                }
            }
        }


        composable("anadir_viaje") {
            var mensajeErrorGuardar by remember {
                mutableStateOf<String?>(null)
            }

            AnadirViajeScreen(
                mensajeErrorGuardar = mensajeErrorGuardar,
                onVolverClick = {
                    navController.popBackStack()
                },
                onGuardarClick = {
                        nombre,
                        destino,
                        fechaInicio,
                        fechaFin,
                        presupuesto,
                        descripcion,
                        estado ->

                    mensajeErrorGuardar = null

                    coroutineScope.launch {
                        val token = tokenJwt

                        if (token.isNullOrBlank()) {
                            mensajeErrorGuardar =
                                "Tu sesión ha caducado. Inicia sesión de nuevo."
                            return@launch
                        }

                        try {
                            val nuevoViaje = ViajeDto(
                                nombre = nombre,
                                destino = destino,
                                fechaInicio = fechaInicio,
                                fechaFin = fechaFin,
                                presupuesto = presupuesto,
                                descripcion = descripcion,
                                estado = estado
                            )

                            val respuesta = RetrofitClient.viajesApi.crearViaje(
                                token = "Bearer $token",
                                viaje = nuevoViaje
                            )

                            if (respuesta.isSuccessful) {
                                val respuestaViajes =
                                    RetrofitClient.viajesApi.obtenerViajes(
                                        "Bearer $token"
                                    )

                                if (respuestaViajes.isSuccessful) {
                                    viajes = respuestaViajes.body() ?: emptyList()
                                    navController.popBackStack()
                                } else {
                                    mensajeErrorGuardar =
                                        "El viaje se guardó, pero no se pudo actualizar la lista."
                                }
                            } else {
                                mensajeErrorGuardar =
                                    "No se pudo guardar el viaje (${respuesta.code()})."
                            }
                        } catch (e: Exception) {
                            mensajeErrorGuardar =
                                "No se pudo conectar con el servidor. Comprueba la conexión."
                        }
                    }
                }
            )
        }

        composable("editar_viaje/{idViaje}") { backStackEntry ->
            val idViaje = backStackEntry.arguments?.getString("idViaje")
            val viajeEditar = viajes.find { it.id == idViaje }

            if (viajeEditar != null) {
                var mensajeErrorEditar by remember {
                    mutableStateOf<String?>(null)
                }

                AnadirViajeScreen(
                    viajeEditar = viajeEditar,
                    mensajeErrorGuardar = mensajeErrorEditar,
                    onVolverClick = {
                        navController.popBackStack()
                    },
                    onGuardarClick = {
                            nombre,
                            destino,
                            fechaInicio,
                            fechaFin,
                            presupuesto,
                            descripcion,
                            estado ->

                        mensajeErrorEditar = null

                        coroutineScope.launch {
                            val token = tokenJwt

                            if (token.isNullOrBlank()) {
                                mensajeErrorEditar =
                                    "Tu sesión ha caducado. Inicia sesión de nuevo."
                                return@launch
                            }

                            try {
                                val viajeActualizado = viajeEditar.copy(
                                    nombre = nombre,
                                    destino = destino,
                                    fechaInicio = fechaInicio,
                                    fechaFin = fechaFin,
                                    presupuesto = presupuesto,
                                    descripcion = descripcion,
                                    estado = estado
                                )

                                val respuesta =
                                    RetrofitClient.viajesApi.actualizarViaje(
                                        token = "Bearer $token",
                                        id = idViaje!!,
                                        viaje = viajeActualizado
                                    )

                                if (respuesta.isSuccessful) {
                                    val respuestaViajes =
                                        RetrofitClient.viajesApi.obtenerViajes(
                                            "Bearer $token"
                                        )

                                    if (respuestaViajes.isSuccessful) {
                                        viajes = respuestaViajes.body() ?: emptyList()
                                        navController.popBackStack()
                                    } else {
                                        mensajeErrorEditar =
                                            "El viaje se actualizó, pero no se pudo refrescar la lista."
                                    }
                                } else {
                                    mensajeErrorEditar =
                                        "No se pudo actualizar el viaje (${respuesta.code()})."
                                }
                            } catch (e: Exception) {
                                mensajeErrorEditar =
                                    "No se pudo conectar con el servidor. Comprueba la conexión."
                            }
                        }
                    }
                )
            } else {
                Text("No se ha encontrado el viaje que quieres editar.")
            }
        }


    }
}
