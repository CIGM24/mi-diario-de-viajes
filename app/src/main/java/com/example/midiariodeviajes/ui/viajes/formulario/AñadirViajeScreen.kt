package com.example.midiariodeviajes.ui.viajes.formulario

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.unit.dp

private val VerdeOscuro = Color(0xFF263D32)
private val VerdeBoton = Color(0xFF45664A)
private val BlancoCalido = Color(0xFFFFFCF5)

private fun fechaEsValida(fecha: String): Boolean {
    val formato = Regex("""\d{4}-\d{2}-\d{2}""")

    if (!formato.matches(fecha)) {
        return false
    }

    val partes = fecha.split("-")
    val anio = partes[0].toInt()
    val mes = partes[1].toInt()
    val dia = partes[2].toInt()

    if (mes !in 1..12) return false
    if (dia !in 1..31) return false

    val diasPorMes = when (mes) {
        4, 6, 9, 11 -> 30
        2 -> if (
            anio % 400 == 0 ||
            (anio % 4 == 0 && anio % 100 != 0)
        ) 29 else 28
        else -> 31
    }

    return dia <= diasPorMes
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnadirViajeScreen(
    onVolverClick: () -> Unit = {},
    onGuardarClick: () -> Unit = {}
) {
    var nombre by remember { mutableStateOf("") }
    var destino by remember { mutableStateOf("") }
    var fechaInicio by remember { mutableStateOf("") }
    var fechaFin by remember { mutableStateOf("") }
    var presupuesto by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var estado by remember { mutableStateOf("PENDIENTE") }

    var errorValidacion by remember { mutableStateOf<String?>(null) }

    Scaffold(
        containerColor = BlancoCalido,


        topBar = {
            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .padding(top = 25.dp, bottom = 8.dp)
            ) {
                TextButton(
                    onClick = onVolverClick,
                    modifier = Modifier.align(
                        androidx.compose.ui.Alignment.CenterStart
                    )
                ) {
                    Text("← Volver", color = VerdeOscuro)
                }

                Text(
                    text = "Añadir viaje",
                    modifier = Modifier.align(
                        androidx.compose.ui.Alignment.Center
                    ),
                    color = VerdeOscuro,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }


    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "¡Prepara tu próxima aventura!",
                style = MaterialTheme.typography.titleLarge,
                color = VerdeOscuro,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "Completa los datos de tu viaje.",
                color = VerdeOscuro
            )

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre del viaje *") },
                placeholder = { Text("Ej. Vacaciones de verano") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = destino,
                onValueChange = { destino = it },
                label = { Text("Destino *") },
                placeholder = { Text("Ej. Roma, Italia") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = fechaInicio,
                onValueChange = { fechaInicio = it },
                label = { Text("Fecha de inicio *") },
                placeholder = { Text("AAAA-MM-DD") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = fechaFin,
                onValueChange = { fechaFin = it },
                label = { Text("Fecha de fin *") },
                placeholder = { Text("AAAA-MM-DD") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = presupuesto,
                onValueChange = { presupuesto = it },
                label = { Text("Presupuesto (€)") },
                placeholder = { Text("Ej. 1200.50") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                )
            )

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                placeholder = { Text("Anota tus planes e ideas...") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5
            )

            Text(
                text = "Estado del viaje *",
                color = VerdeOscuro,
                fontWeight = FontWeight.SemiBold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = { estado = "PENDIENTE" },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (estado == "PENDIENTE")
                            VerdeBoton else Color.LightGray,
                        contentColor = if (estado == "PENDIENTE")
                            Color.White else VerdeOscuro
                    )
                ) {
                    Text("Pendiente")
                }

                Button(
                    onClick = { estado = "REALIZADO" },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (estado == "REALIZADO")
                            VerdeBoton else Color.LightGray,
                        contentColor = if (estado == "REALIZADO")
                            Color.White else VerdeOscuro
                    )
                ) {
                    Text("Realizado")
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Button(
                onClick = {
                    errorValidacion = when {
                        nombre.isBlank() ->
                            "Introduce el nombre del viaje."

                        destino.isBlank() ->
                            "Introduce el destino."

                        fechaInicio.isBlank() ->
                            "Introduce la fecha de inicio."

                        fechaFin.isBlank() ->
                            "Introduce la fecha de fin."

                        fechaInicio.isNotBlank() && !fechaEsValida(fechaInicio.trim()) ->
                            "La fecha de inicio debe tener el formato AAAA-MM-DD."

                        fechaFin.isNotBlank() && !fechaEsValida(fechaFin.trim()) ->
                            "La fecha de fin debe tener el formato AAAA-MM-DD."


                        fechaEsValida(fechaInicio.trim()) &&
                                fechaEsValida(fechaFin.trim()) &&
                                fechaFin.trim() < fechaInicio.trim() ->
                            "La fecha de fin no puede ser anterior a la fecha de inicio."

                        presupuesto.isNotBlank() &&
                                (presupuesto.replace(',', '.').toDoubleOrNull() == null ||
                                        presupuesto.replace(',', '.').toDoubleOrNull()!! < 0) ->
                            "Introduce un presupuesto válido y no negativo."

                        else -> null
                    }

                    if (errorValidacion == null) {
                        onGuardarClick()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = VerdeBoton
                )
            ) {
                Text("Guardar viaje")
            }
            if (errorValidacion != null) {
                Text(
                    text = errorValidacion!!,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Text(
                text = "* Campos obligatorios",
                color = VerdeOscuro,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

