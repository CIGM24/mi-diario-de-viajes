package com.example.midiariodeviajes.ui.viajes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.midiariodeviajes.data.ApiConfig
import com.example.midiariodeviajes.data.SesionUsuario
import com.example.midiariodeviajes.data.model.ViajeDto
import com.example.midiariodeviajes.R
import androidx.compose.material3.AlertDialog
import java.text.Normalizer
import java.util.Locale

private val VerdeOscuro = Color(0xFF263D32)
private val VerdeBoton = Color(0xFF45664A)
private val VerdeSuave = Color(0xFFE4EBDD)
private val Arena = Color(0xFFE4C995)
private val BlancoCalido = Color(0xFFF3EFE5)

private val codigosIsoPais = Locale.getISOCountries().toSet()
private val codigoPaisPorNombre by lazy {
    val idiomas = listOf(Locale.forLanguageTag("es"), Locale.ENGLISH)

    codigosIsoPais.flatMap { codigo ->
        val localePais = Locale("", codigo)
        idiomas.mapNotNull { idioma ->
            localePais.getDisplayCountry(idioma)
                .takeIf { it.isNotBlank() && it != codigo }
                ?.let { normalizarNombrePais(it) to codigo }
        }
    }.toMap()
}

private fun normalizarNombrePais(nombre: String): String =
    Normalizer.normalize(nombre, Normalizer.Form.NFD)
        .replace(Regex("\\p{Mn}+"), "")
        .trim()
        .lowercase(Locale.ROOT)

private fun banderaDesdeDestino(destino: String): String? {
    val posiblePais = destino.split(Regex("[,;|]")).lastOrNull()?.trim()
        ?.takeIf { it.isNotBlank() }
        ?: return null

    val codigoPais = codigoPaisPorNombre[normalizarNombrePais(posiblePais)]
        ?: posiblePais.uppercase(Locale.ROOT).takeIf { it in codigosIsoPais }
        ?: return null

    return codigoPais.map { letra ->
        String(Character.toChars(0x1F1E6 + letra.code - 'A'.code))
    }.joinToString("")
}

private data class FechaTarjeta(
    val dia: Int,
    val mes: Int,
    val anio: Int
)

private val mesesCortos = listOf(
    "ene", "feb", "mar", "abr", "may", "jun",
    "jul", "ago", "sep", "oct", "nov", "dic"
)

private fun parsearFechaTarjeta(fecha: String): FechaTarjeta? {
    val partes = fecha.split("-")
    if (partes.size != 3) return null

    val anio = partes[0].toIntOrNull() ?: return null
    val mes = partes[1].toIntOrNull() ?: return null
    val dia = partes[2].toIntOrNull() ?: return null

    return if (mes in 1..12 && dia in 1..31) {
        FechaTarjeta(dia, mes, anio)
    } else {
        null
    }
}

private fun formatearRangoFechas(inicio: String, fin: String): String {
    val fechaInicio = parsearFechaTarjeta(inicio) ?: return "$inicio – $fin"
    val fechaFin = parsearFechaTarjeta(fin) ?: return "$inicio – $fin"
    val mesInicio = mesesCortos[fechaInicio.mes - 1]
    val mesFin = mesesCortos[fechaFin.mes - 1]

    return when {
        fechaInicio == fechaFin -> "${fechaInicio.dia} $mesInicio ${fechaInicio.anio}"
        fechaInicio.mes == fechaFin.mes && fechaInicio.anio == fechaFin.anio ->
            "${fechaInicio.dia}–${fechaFin.dia} $mesInicio ${fechaInicio.anio}"

        fechaInicio.anio == fechaFin.anio ->
            "${fechaInicio.dia} $mesInicio – ${fechaFin.dia} $mesFin ${fechaFin.anio}"

        else ->
            "${fechaInicio.dia} $mesInicio ${fechaInicio.anio} – " +
                    "${fechaFin.dia} $mesFin ${fechaFin.anio}"
    }
}

private data class ViajePrueba(
    val pais: String,
    val ciudad: String,
    val bandera: String,
    val fechas: String,
    val estado: String
)

@Composable
fun MisViajesScreen(
    viajes: List<ViajeDto> = emptyList(),
    onVolverClick: () -> Unit = {},
    onPerfilClick: () -> Unit = {},
    onAnadirViajeClick: () -> Unit = {},
    onDetalleViajeClick: (String) -> Unit = {},
    onEliminarViajeClick: (String) -> Unit = {}
) {

    var filtroSeleccionado by rememberSaveable {
        mutableStateOf("Todos")
    }

    var viajePendienteEliminar by remember { mutableStateOf<ViajeDto?>(null) }
    val contexto = LocalContext.current
    val token = SesionUsuario.tokenJwt

    val viajesFiltrados = viajes.filter { viaje ->
        when (filtroSeleccionado) {
            "Pendientes" -> viaje.estado.equals("PENDIENTE", ignoreCase = true)
            "Realizados" -> viaje.estado.equals("REALIZADO", ignoreCase = true)
            else -> true
        }
    }

    Scaffold(
        containerColor = BlancoCalido
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BlancoCalido)
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {

            // Cabecera
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onVolverClick) {
                    Text(
                        text = "←",
                        fontSize = 28.sp,
                        color = VerdeOscuro
                    )
                }

                Text(
                    text = "Mis viajes",
                    style = MaterialTheme.typography.headlineSmall,
                    color = VerdeOscuro,
                    fontWeight = FontWeight.Bold
                )

                TextButton(onClick = onPerfilClick) {
                    Text(
                        text = "Cerrar sesión",
                        fontSize = 12.sp,
                        color = VerdeOscuro
                    )
                }
            }

            // Filtros
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Todos", "Pendientes", "Realizados").forEach { filtro ->
                    val seleccionado = filtroSeleccionado == filtro

                    Button(
                        onClick = { filtroSeleccionado = filtro },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(
                            horizontal = 4.dp,
                            vertical = 10.dp
                        ),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (seleccionado) {
                                VerdeBoton
                            } else {
                                Color.White
                            },
                            contentColor = if (seleccionado) {
                                Color.White
                            } else {
                                VerdeOscuro
                            }
                        )
                    ) {
                        Text(
                            text = filtro,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            // Lista de viajes y botón inferior
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (viajesFiltrados.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 48.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = if (viajes.isEmpty()) {
                                    "Tu diario empieza aquí"
                                } else {
                                    "No hay viajes en esta categoría"
                                },
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = VerdeOscuro
                            )
                            Text(
                                text = if (viajes.isEmpty()) {
                                    "Añade tu primer viaje para guardar tus recuerdos."
                                } else {
                                    "Prueba otro filtro para ver tus viajes."
                                },
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                fontSize = 14.sp,
                                color = VerdeBoton
                            )
                        }
                    }
                }

                items(viajesFiltrados) { viaje ->
                    val iconoDestino = banderaDesdeDestino(viaje.destino) ?: "✈️"
                    val viajePendiente = viaje.estado.equals(
                        "PENDIENTE",
                        ignoreCase = true
                    )

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.White
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 2.dp
                        ),
                        onClick = {
                            onDetalleViajeClick(viaje.id ?: viaje.nombre)
                        }
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(100.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(VerdeSuave),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = iconoDestino,
                                        fontSize = 30.sp
                                    )

                                    if (!viaje.fotoUrl.isNullOrBlank() && !token.isNullOrBlank()) {
                                        AsyncImage(
                                            model = ImageRequest.Builder(contexto)
                                                .data("${ApiConfig.BASE_URL}fotos/${viaje.fotoUrl}")
                                                .addHeader("Authorization", "Bearer $token")
                                                .crossfade(true)
                                                .build(),
                                            contentDescription = "Fotografía de ${viaje.nombre}",
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(100.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.fillMaxSize(),
                                        verticalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = viaje.destino,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(end = 48.dp),
                                            fontSize = 19.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = VerdeOscuro
                                        )

                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(end = 48.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            Icon(
                                                painter = painterResource(R.drawable.ic_calendar_today),
                                                contentDescription = null,
                                                tint = VerdeBoton,
                                                modifier = Modifier.size(17.dp)
                                            )
                                            Text(
                                                text = formatearRangoFechas(
                                                    viaje.fechaInicio,
                                                    viaje.fechaFin
                                                ),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                color = VerdeOscuro,
                                                fontSize = 15.sp,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = viaje.estado.uppercase(),
                                                modifier = Modifier
                                                    .background(
                                                        color = if (viajePendiente) {
                                                            VerdeSuave
                                                        } else {
                                                            Color(0xFFF4E8D1)
                                                        },
                                                        shape = RoundedCornerShape(50)
                                                    )
                                                    .padding(horizontal = 9.dp, vertical = 4.dp),
                                                color = if (viajePendiente) {
                                                    VerdeBoton
                                                } else {
                                                    Color(0xFF8A6A32)
                                                },
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 12.sp
                                            )
                                            Text(
                                                text = "→",
                                                color = VerdeBoton,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 18.sp
                                            )
                                        }
                                    }

                                    IconButton(
                                        onClick = { viajePendienteEliminar = viaje },
                                        modifier = Modifier.align(Alignment.TopEnd)
                                    ) {
                                        Icon(
                                            painter = painterResource(R.drawable.ic_delete),
                                            contentDescription = "Eliminar viaje ${viaje.nombre}",
                                            tint = Color(0xFFB45345)
                                        )
                                    }
                                }
                            }

                        }
                    }
                }
            }

            Button(
                onClick = onAnadirViajeClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = 8.dp)
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = VerdeBoton
                )
            ) {
                Text(
                    text = "+ Añadir viaje",
                    color = Color.White,
                    fontSize = 16.sp
                )
            }
        }
    }

    viajePendienteEliminar?.let { viaje ->
        AlertDialog(
            onDismissRequest = {
                viajePendienteEliminar = null
            },
            title = {
                Text("Eliminar viaje")
            },
            text = {
                Text(
                    "¿Seguro que quieres eliminar \"${viaje.nombre}\"? " +
                            "Esta acción no se puede deshacer."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val idViaje = viaje.id

                        if (idViaje != null) {
                            onEliminarViajeClick(idViaje)
                        }

                        viajePendienteEliminar = null
                    }
                ) {
                    Text(
                        "Eliminar",
                        color = Color.Red
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        viajePendienteEliminar = null
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}
