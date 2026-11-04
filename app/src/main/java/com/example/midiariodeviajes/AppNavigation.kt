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
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import okhttp3.MultipartBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import androidx.compose.runtime.LaunchedEffect
import com.example.midiariodeviajes.data.model.LugarDto
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.midiariodeviajes.ui.login.LoginViewModel
import com.example.midiariodeviajes.ui.login.LoginViewModelFactory
import com.example.midiariodeviajes.ui.register.RegisterViewModel
import com.example.midiariodeviajes.ui.register.RegisterViewModelFactory
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import com.example.midiariodeviajes.data.ViajesRepository
import com.example.midiariodeviajes.ui.viajes.ViajesViewModel
import com.example.midiariodeviajes.ui.viajes.ViajesViewModelFactory
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.midiariodeviajes.data.LugaresRepository
import com.example.midiariodeviajes.ui.viajes.LugaresViewModel
import com.example.midiariodeviajes.ui.viajes.LugaresViewModelFactory

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
    val contexto = LocalContext.current

    val authRepository = remember { AuthRepository() }
    val registroRepository = remember { RegistroRepository() }
    val coroutineScope = rememberCoroutineScope()
    var mensajeError by remember { mutableStateOf<String?>(null) }
    var tokenJwt by remember { mutableStateOf<String?>(null) }
    var viajes by remember { mutableStateOf<List<ViajeDto>>(emptyList()) }
    val viajesRepository = remember { ViajesRepository() }
    val viajesViewModel: ViajesViewModel = viewModel(
        factory = ViajesViewModelFactory(viajesRepository)
    )
    val viajesUiState by viajesViewModel.uiState.collectAsState()

    LaunchedEffect(viajesUiState.viajes) {
        if (viajesUiState.cargaCompletada) {
            viajes = viajesUiState.viajes
        }
    }

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
            val loginViewModel: LoginViewModel = viewModel(
                factory = LoginViewModelFactory(authRepository)
            )
            val loginUiState by loginViewModel.uiState.collectAsState()

            LaunchedEffect(loginUiState.token) {
                val token = loginUiState.token ?: return@LaunchedEffect

                tokenJwt = token
                SesionUsuario.tokenJwt = token
                mensajeError = null

                viajesViewModel.cargarViajes(token)
            }

            LaunchedEffect(loginUiState.token, viajesUiState.cargaCompletada) {
                val token = loginUiState.token ?: return@LaunchedEffect
                if (viajesUiState.cargaCompletada) {
                    viajes = viajesUiState.viajes

                    navController.navigate(Rutas.MIS_VIAJES) {
                        popUpTo(Rutas.LOGIN) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            }
            LoginScreen(
                mensajeError = loginUiState.mensajeError
                    ?: viajesUiState.mensajeError
                    ?: mensajeError,
                /*mensajeError = loginUiState.mensajeError ?: mensajeError,*/
                onIniciarSesionClick = { correo, contrasena ->
                    mensajeError = null
                    loginViewModel.iniciarSesion(correo, contrasena)
                },
                onCrearCuentaClick = {
                    navController.navigate(Rutas.REGISTRO)
                }
            )
        }

        composable(Rutas.REGISTRO) {
            val registerViewModel: RegisterViewModel = viewModel(
                factory = RegisterViewModelFactory(registroRepository)
            )
            val registerUiState by registerViewModel.uiState.collectAsState()

            RegisterScreen(
                onRegistrarseClick = { nombre, correo, contrasena ->
                    registerViewModel.registrar(
                        nombre = nombre,
                        email = correo,
                        password = contrasena
                    )
                },
                onIniciarSesionClick = {
                    navController.popBackStack()
                },
                mensajeError = registerUiState.mensajeError,
                mensajeExito = registerUiState.mensajeExito
            )
        }

        composable(Rutas.MIS_VIAJES) {
            MisViajesScreen(
                viajes = viajes,
                onVolverClick = {
                    navController.popBackStack()
                },
                onPerfilClick = {
                    tokenJwt = null
                    SesionUsuario.tokenJwt = null
                    viajes = emptyList()
                    mensajeError = null

                    navController.navigate(Rutas.BIENVENIDA) {
                        popUpTo(Rutas.BIENVENIDA) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                onAnadirViajeClick = {
                    navController.navigate("anadir_viaje")
                },
                onDetalleViajeClick = { idViaje ->
                    navController.navigate("${Rutas.DETALLE_VIAJE}/$idViaje")
                },
                onEliminarViajeClick = { idViaje ->
                    val token = tokenJwt ?: SesionUsuario.tokenJwt

                    if (token.isNullOrBlank()) {
                        Toast.makeText(
                            contexto,
                            "La sesión ha caducado. Inicia sesión de nuevo.",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        viajesViewModel.eliminarViaje(
                            token = token,
                            viajeId = idViaje
                        )
                    }
                }
            )
        }

        composable("${Rutas.DETALLE_VIAJE}/{idViaje}") { backStackEntry ->
            val idViaje = backStackEntry.arguments?.getString("idViaje")

            val lugaresRepository = remember(idViaje) {
                LugaresRepository()
            }
            val lugaresViewModel: LugaresViewModel = viewModel(
                factory = LugaresViewModelFactory(lugaresRepository)
            )
            val lugaresUiState by lugaresViewModel.uiState.collectAsState()

            var lugares by remember(idViaje) {
                mutableStateOf<List<LugarDto>>(emptyList())
            }
            var cargandoLugares by remember(idViaje) {
                mutableStateOf(true)
            }
            var errorLugares by remember(idViaje) {
                mutableStateOf<String?>(null)
            }
            var cargaLugaresIniciada by remember(idViaje) {
                mutableStateOf(false)
            }

            LaunchedEffect(idViaje, tokenJwt) {
                if (idViaje.isNullOrBlank()) {
                    errorLugares = "No se ha encontrado el viaje."
                    cargandoLugares = false
                    return@LaunchedEffect
                }

                val token = tokenJwt ?: SesionUsuario.tokenJwt

                if (token.isNullOrBlank()) {
                    errorLugares = "Tu sesión ha caducado. Inicia sesión de nuevo."
                    cargandoLugares = false
                    return@LaunchedEffect
                }

                cargaLugaresIniciada = true
                cargandoLugares = true
                errorLugares = null

                lugaresViewModel.cargarLugares(
                    token = token,
                    viajeId = idViaje
                )
            }

            LaunchedEffect(lugaresUiState) {
                if (cargaLugaresIniciada) {
                    lugares = lugaresUiState.lugares
                    cargandoLugares = lugaresUiState.cargando
                    errorLugares = lugaresUiState.mensajeError
                }
            }

            LaunchedEffect(lugaresUiState.mensajeExito) {
                val mensaje = lugaresUiState.mensajeExito ?: return@LaunchedEffect

                Toast.makeText(
                    contexto,
                    mensaje,
                    Toast.LENGTH_SHORT
                ).show()

                lugaresViewModel.limpiarMensajeExito()
            }

            val viajeSeleccionado = viajes.find { it.id == idViaje }

            if (viajeSeleccionado != null) {
                DetalleViajeScreen(
                    viaje = viajeSeleccionado,
                    lugares = lugares,
                    cargandoLugares = cargandoLugares,
                    errorLugares = errorLugares,
                    onVolverClick = {
                        navController.popBackStack()
                    },
                    onEditarClick = {
                        navController.navigate("editar_viaje/${viajeSeleccionado.id}")
                    },
                    onSubirFotoClick = { uri ->
                        coroutineScope.launch {
                            val token = tokenJwt ?: SesionUsuario.tokenJwt

                            if (token.isNullOrBlank()) {
                                Toast.makeText(
                                    contexto,
                                    "Tu sesión ha caducado. Inicia sesión de nuevo.",
                                    Toast.LENGTH_LONG
                                ).show()
                                return@launch
                            }

                            try {
                                val tipoMime = contexto.contentResolver.getType(uri)
                                    ?: "image/jpeg"

                                val extension = when (tipoMime) {
                                    "image/png" -> ".png"
                                    "image/webp" -> ".webp"
                                    else -> ".jpg"
                                }

                                val bytes = contexto.contentResolver
                                    .openInputStream(uri)
                                    ?.use { it.readBytes() }
                                    ?: throw Exception("No se pudo leer la fotografía.")

                                if (bytes.size > 5 * 1024 * 1024) {
                                    Toast.makeText(
                                        contexto,
                                        "La fotografía no puede superar los 5 MB.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    return@launch
                                }

                                val cuerpo = bytes.toRequestBody(
                                    tipoMime.toMediaTypeOrNull()
                                )

                                val archivo = MultipartBody.Part.createFormData(
                                    "archivo",
                                    "foto$extension",
                                    cuerpo
                                )

                                val respuesta = RetrofitClient.viajesApi.subirFoto(
                                    token = "Bearer $token",
                                    id = viajeSeleccionado.id ?: return@launch,
                                    archivo = archivo
                                )

                                if (respuesta.isSuccessful) {
                                    val respuestaViajes =
                                        RetrofitClient.viajesApi.obtenerViajes(
                                            "Bearer $token"
                                        )

                                    if (respuestaViajes.isSuccessful) {
                                        viajes = respuestaViajes.body() ?: emptyList()

                                        Toast.makeText(
                                            contexto,
                                            "Fotografía guardada correctamente.",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        Toast.makeText(
                                            contexto,
                                            "La foto se guardó, pero no se pudo actualizar el viaje.",
                                            Toast.LENGTH_LONG
                                        ).show()
                                    }
                                } else {
                                    Toast.makeText(
                                        contexto,
                                        "No se pudo subir la foto (${respuesta.code()}).",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(
                                    contexto,
                                    "Error al subir la fotografía: ${e.message}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                    },
                    onAñadirLugarClick = { lugar ->
                        val token = tokenJwt ?: SesionUsuario.tokenJwt

                        if (token.isNullOrBlank()) {
                            Toast.makeText(
                                contexto,
                                "Tu sesión ha caducado. Inicia sesión de nuevo.",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            val viajeIdActual = viajeSeleccionado.id

                            if (viajeIdActual.isNullOrBlank()) {
                                Toast.makeText(
                                    contexto,
                                    "No se ha encontrado el viaje.",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                lugaresViewModel.crearLugar(
                                    token = token,
                                    viajeId = viajeIdActual,
                                    lugar = lugar
                                )
                            }
                        }
                    },
                    onEditarLugarClick = { lugar ->
                        val token = tokenJwt ?: SesionUsuario.tokenJwt
                        val lugarId = lugar.id

                        when {
                            token.isNullOrBlank() -> {
                                Toast.makeText(
                                    contexto,
                                    "Tu sesión ha caducado. Inicia sesión de nuevo.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                            lugarId.isNullOrBlank() -> {
                                Toast.makeText(
                                    contexto,
                                    "No se ha encontrado el lugar.",
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                            else -> {
                                lugaresViewModel.actualizarLugar(
                                    token = token,
                                    lugarId = lugarId,
                                    lugar = lugar
                                )
                            }
                        }
                    },
                    onEliminarLugarClick = { lugarId ->
                        val token = tokenJwt ?: SesionUsuario.tokenJwt

                        if (token.isNullOrBlank()) {
                            Toast.makeText(
                                contexto,
                                "Tu sesión ha caducado. Inicia sesión de nuevo.",
                                Toast.LENGTH_LONG
                            ).show()
                        } else if (lugarId.isBlank()) {
                            Toast.makeText(
                                contexto,
                                "No se ha encontrado el lugar.",
                                Toast.LENGTH_LONG
                            ).show()
                        } else {
                            lugaresViewModel.eliminarLugar(
                                token = token,
                                lugarId = lugarId
                            )
                        }
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

            LaunchedEffect(viajesUiState.creacionCompletada) {
                if (viajesUiState.creacionCompletada) {
                    viajesViewModel.limpiarCreacionCompletada()
                    navController.popBackStack()
                }
            }

            AnadirViajeScreen(
                mensajeErrorGuardar =
                mensajeErrorGuardar ?: viajesUiState.mensajeError,
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

                    val token = tokenJwt ?: SesionUsuario.tokenJwt

                    if (token.isNullOrBlank()) {
                        mensajeErrorGuardar =
                            "Tu sesión ha caducado. Inicia sesión de nuevo."
                    } else {
                        val nuevoViaje = ViajeDto(
                            nombre = nombre,
                            destino = destino,
                            fechaInicio = fechaInicio,
                            fechaFin = fechaFin,
                            presupuesto = presupuesto,
                            descripcion = descripcion,
                            estado = estado
                        )

                        viajesViewModel.crearViaje(
                            token = token,
                            viaje = nuevoViaje
                        )
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

                LaunchedEffect(viajesUiState.actualizacionCompletada) {
                    if (viajesUiState.actualizacionCompletada) {
                        viajesViewModel.limpiarActualizacionCompletada()
                        navController.popBackStack()
                    }
                }

                AnadirViajeScreen(
                    viajeEditar = viajeEditar,
                    mensajeErrorGuardar =
                    mensajeErrorEditar ?: viajesUiState.mensajeError,
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

                        val token = tokenJwt ?: SesionUsuario.tokenJwt

                        if (token.isNullOrBlank()) {
                            mensajeErrorEditar =
                                "Tu sesión ha caducado. Inicia sesión de nuevo."
                        } else if (idViaje.isNullOrBlank()) {
                            mensajeErrorEditar = "No se ha encontrado el viaje."
                        } else {
                            val viajeActualizado = viajeEditar.copy(
                                nombre = nombre,
                                destino = destino,
                                fechaInicio = fechaInicio,
                                fechaFin = fechaFin,
                                presupuesto = presupuesto,
                                descripcion = descripcion,
                                estado = estado
                            )

                            viajesViewModel.actualizarViaje(
                                token = token,
                                viajeId = idViaje,
                                viaje = viajeActualizado
                            )
                        }
                    }
                )
            } else {
                Text("No se ha encontrado el viaje que quieres editar.")
            }
        }

    }
}
