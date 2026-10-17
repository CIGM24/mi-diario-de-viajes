package com.example.midiariodeviajes.ui.viajes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.midiariodeviajes.data.model.ViajeDto

private val FondoCrema = Color(0xFFFFFCF5)
private val VerdeOscuro = Color(0xFF263D32)
private val VerdeSalvia = Color(0xFFE4EBDD)
private val VerdeTexto = Color(0xFF45664A)
private val Terracota = Color(0xFFB56F4D)
private val FondoTerracota = Color(0xFFF5E5D9)

@Composable
fun DetalleViajeScreen(
    viaje: ViajeDto,
    onVolverClick: () -> Unit,
    onEditarClick: () -> Unit = {},
    onEliminarClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoCrema)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {


        // Cabecera del viaje: imagen compacta y datos debajo
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(24.dp)
                )
        ) {
            // Zona reservada para la imagen del destino
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
                    .background(
                        color = VerdeSalvia,
                        shape = RoundedCornerShape(24.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "🌍",
                        fontSize = 64.sp
                    )

                    Text(
                        text = "Imagen del destino",
                        fontSize = 13.sp,
                        color = VerdeTexto
                    )
                }
            }

            // Datos del viaje
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "✈️ MI DIARIO DE VIAJES",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = VerdeTexto
                )

                Text(
                    text = viaje.nombre,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = VerdeOscuro,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "📍 ${viaje.destino}",
                    fontSize = 17.sp,
                    color = VerdeTexto
                )

                Text(
                    text = "📅 ${viaje.fechaInicio} — ${viaje.fechaFin}",
                    fontSize = 14.sp,
                    color = VerdeTexto
                )
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
                    .padding(18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = "💰",
                    fontSize = 30.sp
                )

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Presupuesto del viaje",
                        fontSize = 14.sp,
                        color = Terracota
                    )

                    Text(
                        text = viaje.presupuesto
                            ?.let { "$it €" }
                            ?: "No indicado",
                        fontSize = 23.sp,
                        fontWeight = FontWeight.Bold,
                        color = VerdeOscuro
                    )
                }

                Text(
                    text = "🧳 ${viaje.estado.lowercase()}",
                    fontSize = 12.sp,
                    color = VerdeTexto
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

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 1.dp
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = "🗺️",
                        fontSize = 34.sp
                    )

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
                        text = "Aquí aparecerán tus lugares de interés.",
                        fontSize = 13.sp,
                        color = VerdeTexto
                    )
                }
            }

            // Preparado visualmente para la futura función
            OutlinedButton(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = VerdeTexto
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
            OutlinedButton(
                onClick = onEditarClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = VerdeTexto
                )
            ) {
                Text("✏️ Editar viaje")
            }

            OutlinedButton(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFFB45345)
                )
            ) {
                Text("🗑️ Eliminar viaje")
            }

            Spacer(modifier = Modifier.height(4.dp))

            Button(
                onClick = onVolverClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = VerdeTexto
                )
            ) {
                Text(
                    text = "← Volver a mis viajes",
                    fontSize = 16.sp
                )
            }
        }
    }
}

