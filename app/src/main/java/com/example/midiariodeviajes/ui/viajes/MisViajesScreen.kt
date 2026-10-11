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
    onVolverClick: () -> Unit = {},
    onPerfilClick: () -> Unit = {},
    onAnadirViajeClick: () -> Unit = {},
    onDetalleViajeClick: (String) -> Unit = {}
) {
    val viajes = remember {
        listOf(
            ViajePrueba(
                pais = "Italia",
                bandera = "🇮🇹",
                ciudad = "Roma",
                fechas = "10/06/2027 - 17/06/2027",
                estado = "Pendiente"
            ),
            ViajePrueba(
                pais = "Francia",
                bandera = "🇫🇷",
                ciudad = "París",
                fechas = "12/04/2026 - 16/04/2026",
                estado = "Realizado"
            ),
            ViajePrueba(
                pais = "Japón",
                bandera = "🇯🇵",
                ciudad = "Tokio",
                fechas = "05/09/2027 - 20/09/2027",
                estado = "Pendiente"
            )
        )
    }

    var filtroSeleccionado by rememberSaveable {
        mutableStateOf("Todos")
    }

    val viajesFiltrados = viajes.filter { viaje ->
        when (filtroSeleccionado) {
            "Pendientes" -> viaje.estado == "Pendiente"
            "Realizados" -> viaje.estado == "Realizado"
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
                            onDetalleViajeClick(viaje.pais)
                        }
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Text(
                                    text = viaje.bandera,
                                    fontSize = 26.sp
                                )

                                Text(
                                    text = viaje.pais,
                                    fontSize = 21.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = VerdeOscuro
                                )
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "Ciudad: ${viaje.ciudad}",
                                color = VerdeOscuro
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "Fechas: ${viaje.fechas}",
                                color = VerdeOscuro
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = viaje.estado.uppercase(),
                                color = if (viaje.estado == "Pendiente") {
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
}
