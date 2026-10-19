package com.example.midiariodeviajes.ui.viajes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.midiariodeviajes.data.model.ViajeDto
import androidx.compose.material3.AlertDialog

private val VerdeOscuro = Color(0xFF263D32)
private val VerdeBoton = Color(0xFF45664A)
private val Arena = Color(0xFFE4C995)
private val BlancoCalido = Color(0xFFFFFCF5)

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
                        text = "♙",
                        fontSize = 26.sp,
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
                            fontSize = 11.sp
                        )
                    }
                }
            }

            // Lista de viajes y botón inferior
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(viajesFiltrados) { viaje ->
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
                            modifier = Modifier.padding(18.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(
                                    text = when {
                                        "roma" in viaje.nombre.lowercase() ||
                                                "italia" in viaje.nombre.lowercase() -> "🇮🇹"

                                        "irlanda" in viaje.nombre.lowercase() -> "🇮🇪"

                                        "barcelona" in viaje.nombre.lowercase() ||
                                                "españa" in viaje.nombre.lowercase() ||
                                                "espana" in viaje.nombre.lowercase() -> "🇪🇸"

                                        "francia" in viaje.nombre.lowercase() ||
                                                "parís" in viaje.nombre.lowercase() ||
                                                "paris" in viaje.nombre.lowercase() -> "🇫🇷"

                                        "japón" in viaje.nombre.lowercase() ||
                                                "japon" in viaje.nombre.lowercase() ||
                                                "tokio" in viaje.nombre.lowercase() -> "🇯🇵"

                                        else -> "🌍"
                                    },
                                    fontSize = 26.sp
                                )

                                Text(
                                    text = viaje.nombre,
                                    modifier = Modifier.weight(1f),
                                    fontSize = 21.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = VerdeOscuro
                                )

                                TextButton(
                                    onClick = { viajePendienteEliminar = viaje }
                                ) {
                                    Text(
                                        text = "🗑️",
                                        fontSize = 20.sp
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "Destino: ${viaje.destino}",
                                color = VerdeOscuro
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "Fechas: ${viaje.fechaInicio} - ${viaje.fechaFin}",
                                color = VerdeOscuro
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = viaje.estado.uppercase(),
                                color = if (viaje.estado.equals("PENDIENTE", ignoreCase = true)) {
                                    VerdeBoton
                                } else {
                                    Color(0xFF8A6A32)
                                },
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Ver detalle del viaje →",
                                color = VerdeBoton,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                item {
                    Button(
                        onClick = onAnadirViajeClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp, bottom = 20.dp)
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
