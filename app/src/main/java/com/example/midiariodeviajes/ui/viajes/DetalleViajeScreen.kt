package com.example.midiariodeviajes.ui.viajes

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.midiariodeviajes.data.ApiConfig
import com.example.midiariodeviajes.data.SesionUsuario
import com.example.midiariodeviajes.data.model.LugarDto
import com.example.midiariodeviajes.data.model.ViajeDto
import com.example.midiariodeviajes.ui.viajes.formulario.AnadirLugarDialog
import androidx.compose.material3.TextButton
import androidx.compose.material3.AlertDialog
import android.content.Intent
import android.net.Uri

private val FondoCrema = Color(0xFFFFFCF5)
private val VerdeOscuro = Color(0xFF263D32)
private val VerdeSalvia = Color(0xFFE4EBDD)
private val VerdeTexto = Color(0xFF45664A)
private val Terracota = Color(0xFFB56F4D)
private val FondoTerracota = Color(0xFFF5E5D9)
private val ArenaSuave = Color(0xFFE8D5B5)
private val mesesCortos = listOf(
    "ene", "feb", "mar", "abr", "may", "jun",
    "jul", "ago", "sep", "oct", "nov", "dic"
)

private data class FechaViajeDetalle(
    val dia: Int,
    val mes: Int,
    val anio: Int
)

private fun parsearFechaDetalle(fecha: String): FechaViajeDetalle? {
    val partes = fecha.split("-")
    if (partes.size != 3) return null

    val anio = partes[0].toIntOrNull() ?: return null
    val mes = partes[1].toIntOrNull() ?: return null
    val dia = partes[2].toIntOrNull() ?: return null

    return if (mes in 1..12 && dia in 1..31) {
        FechaViajeDetalle(dia, mes, anio)
    } else {
        null
    }
}
private fun formatearRangoFechasDetalle(inicio: String, fin: String): String {
    val fechaInicio = parsearFechaDetalle(inicio) ?: return "$inicio — $fin"
    val fechaFin = parsearFechaDetalle(fin) ?: return "$inicio — $fin"
    val mesInicio = mesesCortos[fechaInicio.mes - 1]
    val mesFin = mesesCortos[fechaFin.mes - 1]

    return when {
        fechaInicio == fechaFin -> "${fechaInicio.dia} $mesInicio ${fechaInicio.anio}"
        fechaInicio.mes == fechaFin.mes && fechaInicio.anio == fechaFin.anio ->
            "${fechaInicio.dia}–${fechaFin.dia} $mesInicio ${fechaInicio.anio}"

        fechaInicio.anio == fechaFin.anio ->
            "${fechaInicio.dia} $mesInicio – ${fechaFin.dia} $mesFin ${fechaFin.anio}"

        else ->
            "${fechaInicio.dia} $mesInicio ${fechaInicio.anio} — " +
                    "${fechaFin.dia} $mesFin ${fechaFin.anio}"
    }
}

@Composable
fun DetalleViajeScreen(
    viaje: ViajeDto,
    onVolverClick: () -> Unit,
    onEditarClick: () -> Unit = {},
    onSubirFotoClick: (android.net.Uri) -> Unit = {},
    onAñadirLugarClick: (LugarDto) -> Unit = {},
    onEditarLugarClick: (LugarDto) -> Unit = {},
    onEliminarLugarClick: (String) -> Unit = {},
    lugares: List<LugarDto> = emptyList(),
    cargandoLugares: Boolean = false,
    errorLugares: String? = null
) {
    val contexto = LocalContext.current
    val nombreFoto = viaje.fotoUrl
    val token = SesionUsuario.tokenJwt

    var fotoSeleccionada by remember {
        mutableStateOf<android.net.Uri?>(null)
    }

    var mostrarDialogoLugar by remember(viaje.id) {
        mutableStateOf(false)
    }

    var lugarEnEdicion by remember(viaje.id) {
        mutableStateOf<LugarDto?>(null)
    }

    var lugarPendienteEliminar by remember(viaje.id) {
        mutableStateOf<LugarDto?>(null)
    }

    val selectorFoto = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            fotoSeleccionada = uri
            onSubirFotoClick(uri)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoCrema)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Cabecera del viaje
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(24.dp)
                    )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(190.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(VerdeSalvia),
                    contentAlignment = Alignment.Center
                ) {
                    if (!nombreFoto.isNullOrBlank() && !token.isNullOrBlank()) {
                        AsyncImage(
                            model = ImageRequest.Builder(contexto)
                                .data("${ApiConfig.BASE_URL}fotos/$nombreFoto")
                                .addHeader("Authorization", "Bearer $token")
                                .crossfade(true)
                                .build(),
                            contentDescription = "Fotografía de ${viaje.nombre}",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(190.dp)
                        )
                    } else {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(text = "🌍", fontSize = 64.sp)
                            Text(
                                text = "Imagen del destino",
                                fontSize = 13.sp,
                                color = VerdeTexto
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        VerdeOscuro.copy(alpha = 0.88f)
                                    )
                                )
                            )
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "📍 ${viaje.destino}",
                            color = Color.White,
                            fontSize = 19.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = formatearRangoFechasDetalle(
                                viaje.fechaInicio,
                                viaje.fechaFin
                            ),
                            color = Color.White.copy(alpha = 0.92f),
                            fontSize = 15.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    IconButton(
                        onClick = {
                            selectorFoto.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )
                        },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(10.dp)
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.84f))
                    ) {
                        Text("📷", fontSize = 18.sp)
                    }
                }
            }

            // Presupuesto destacado
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = FondoTerracota
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 1.dp
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "💰",
                        fontSize = 20.sp
                    )

                    Text(
                        text = "Presupuesto",
                        fontSize = 12.sp,
                        color = Terracota
                    )

                    Text(
                        text = viaje.presupuesto
                            ?.let { "$it €" }
                            ?: "No indicado",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = VerdeOscuro
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    val viajePendiente = viaje.estado.equals("PENDIENTE", ignoreCase = true)

                    Text(
                        text = "🧳 ${viaje.estado.lowercase()}",
                        modifier = Modifier
                            .background(
                                color = if (viajePendiente) VerdeSalvia else FondoCrema,
                                shape = RoundedCornerShape(50)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (viajePendiente) VerdeTexto else Terracota
                    )
                }
            }

            // Itinerario
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "📍 Mi itinerario",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = VerdeOscuro
                )

                Text(
                    text = "Organiza los lugares que quieres visitar.",
                    fontSize = 14.sp,
                    color = VerdeTexto
                )

                if (cargandoLugares) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(18.dp)
                    ) {
                        Text(
                            text = "Cargando lugares de interés...",
                            modifier = Modifier.padding(20.dp),
                            color = VerdeTexto
                        )
                    }
                } else if (errorLugares != null) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = FondoTerracota
                        ),
                        shape = RoundedCornerShape(18.dp)
                    ) {
                        Text(
                            text = errorLugares.orEmpty(),
                            modifier = Modifier.padding(20.dp),
                            color = VerdeOscuro
                        )
                    }
                } else if (lugares.isEmpty()) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Text(text = "🗺️", fontSize = 34.sp)
                            Text(
                                text = "Tu próxima aventura empieza aquí",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = VerdeOscuro
                            )
                            Text(
                                text = "Todavía no hay lugares para mostrar.",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                            Text(
                                text = "Añade los lugares que quieres visitar.",
                                fontSize = 13.sp,
                                color = VerdeTexto
                            )
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        lugares.forEachIndexed { indice, lugar ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(18.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = VerdeSalvia
                                ),
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 1.dp
                                )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "${indice + 1}. ${lugar.nombre}",
                                            modifier = Modifier.weight(1f),
                                            fontSize = 17.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = VerdeOscuro
                                        )

                                        TextButton(
                                            onClick = {
                                                lugarEnEdicion = lugar
                                            }
                                        ) {
                                            Text("Editar", color = VerdeTexto)
                                        }

                                        TextButton(
                                            onClick = {
                                                lugarPendienteEliminar = lugar
                                            }
                                        ) {
                                            Text("Eliminar", color = Color(0xFFB45345))
                                        }
                                    }

                                    if (!lugar.descripcion.isNullOrBlank()) {
                                        Text(
                                            text = lugar.descripcion,
                                            fontSize = 14.sp,
                                            color = VerdeTexto
                                        )
                                    }

                                    if (!lugar.direccion.isNullOrBlank()) {
                                        Text(
                                            text = "📍 ${lugar.direccion}",
                                            fontSize = 13.sp,
                                            color = VerdeTexto
                                        )
                                    }

                                    val consultaMapa = when {
                                        lugar.latitud != null && lugar.longitud != null ->
                                            "${lugar.latitud},${lugar.longitud}"

                                        !lugar.direccion.isNullOrBlank() ->
                                            "${lugar.nombre}, ${lugar.direccion}"

                                        else -> null
                                    }

                                    if (consultaMapa != null) {
                                        TextButton(
                                            onClick = {
                                                val mapsUri = Uri.parse(
                                                    "https://www.google.com/maps/search/"
                                                ).buildUpon()
                                                    .appendQueryParameter("api", "1")
                                                    .appendQueryParameter("query", consultaMapa)
                                                    .build()

                                                contexto.startActivity(
                                                    Intent(Intent.ACTION_VIEW, mapsUri)
                                                )
                                            }
                                        ) {
                                            Text("Abrir en Google Maps", color = VerdeTexto)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Button(
                    onClick = {
                        if (!viaje.id.isNullOrBlank()) {
                            mostrarDialogoLugar = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ArenaSuave,
                        contentColor = VerdeOscuro
                    )
                ) {
                    Text(
                        text = "＋ Añadir lugar de interés",
                        fontSize = 15.sp
                    )
                }
            }

            // Acciones del viaje
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onEditarClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Terracota,
                        contentColor = Color.White
                    )
                ) {
                    Text("✏️ Editar viaje")
                }

            }
        }

        Button(
            onClick = onVolverClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp, bottom = 12.dp)
                .height(52.dp),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = VerdeTexto
            )
        ) {
            Text(
                text = "← Volver a mis viajes",
                color = Color.White,
                fontSize = 16.sp
            )
        }
    }

    val viajeId = viaje.id

    if (
        (mostrarDialogoLugar || lugarEnEdicion != null) &&
        !viajeId.isNullOrBlank()
    ) {
        val lugarEditado = lugarEnEdicion

        AnadirLugarDialog(
            viajeId = viajeId,
            lugarEditar = lugarEditado,
            onDismiss = {
                mostrarDialogoLugar = false
                lugarEnEdicion = null
            },
            onGuardar = { lugar ->
                if (lugarEditado == null) {
                    onAñadirLugarClick(lugar)
                } else {
                    onEditarLugarClick(lugar)
                }

                mostrarDialogoLugar = false
                lugarEnEdicion = null
            }
        )
    }

    lugarPendienteEliminar?.let { lugar ->
        AlertDialog(
            onDismissRequest = {
                lugarPendienteEliminar = null
            },
            title = {
                Text("Eliminar lugar")
            },
            text = {
                Text("¿Seguro que quieres eliminar \"${lugar.nombre}\"?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val lugarId = lugar.id

                        if (!lugarId.isNullOrBlank()) {
                            onEliminarLugarClick(lugarId)
                        }

                        lugarPendienteEliminar = null
                    }
                ) {
                    Text("Eliminar", color = Color(0xFFB45345))
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        lugarPendienteEliminar = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}
