package com.example.midiariodeviajes.ui.viajes.formulario

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.midiariodeviajes.data.model.LugarDto

private val VerdeOscuro = Color(0xFF263D32)
private val VerdeBoton = Color(0xFF45664A)
private val VerdeSalvia = Color(0xFFE4EBDD)
private val BlancoCalido = Color(0xFFFFFCF5)
private val Terracota = Color(0xFFB56F4D)

@Composable
fun AnadirLugarDialog(
    viajeId: String,
    lugarEditar: LugarDto? = null,
    onDismiss: () -> Unit,
    onGuardar: (LugarDto) -> Unit
) {
    var nombre by rememberSaveable(lugarEditar?.id) {
        mutableStateOf(lugarEditar?.nombre.orEmpty())
    }
    var descripcion by rememberSaveable(lugarEditar?.id) {
        mutableStateOf(lugarEditar?.descripcion.orEmpty())
    }
    var direccion by rememberSaveable(lugarEditar?.id) {
        mutableStateOf(lugarEditar?.direccion.orEmpty())
    }
    var latitud by rememberSaveable(lugarEditar?.id) {
        mutableStateOf(lugarEditar?.latitud?.toString().orEmpty())
    }
    var longitud by rememberSaveable(lugarEditar?.id) {
        mutableStateOf(lugarEditar?.longitud?.toString().orEmpty())
    }
    var error by rememberSaveable(lugarEditar?.id) {
        mutableStateOf<String?>(null)
    }

    val coloresCampo = OutlinedTextFieldDefaults.colors(
        focusedTextColor = Color.Black,
        unfocusedTextColor = Color.Black,
        focusedBorderColor = VerdeBoton,
        focusedLabelColor = VerdeBoton,
        cursorColor = VerdeOscuro
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(24.dp),
        containerColor = BlancoCalido,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("📍", fontSize = 24.sp)
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = if (lugarEditar == null) "Añadir lugar" else "Editar lugar",
                        color = VerdeOscuro,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (lugarEditar == null) {
                            "Guarda un sitio especial de tu viaje"
                        } else {
                            "Actualiza los datos del lugar"
                        },
                        color = VerdeBoton,
                        style = androidx.compose.material3.MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 440.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre *") },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = coloresCampo,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    minLines = 1,
                    maxLines = 2,
                    shape = RoundedCornerShape(14.dp),
                    colors = coloresCampo,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = direccion,
                    onValueChange = { direccion = it },
                    label = { Text("Dirección") },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = coloresCampo,
                    modifier = Modifier.fillMaxWidth()
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(VerdeSalvia, RoundedCornerShape(16.dp))
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("🌍", fontSize = 20.sp)
                            Text(
                                text = "Ubicación en el mapa (opcional)",
                                color = VerdeOscuro,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Text(
                            text = "Añade ambas coordenadas para guardar la posición exacta.",
                            color = VerdeBoton,
                            style = androidx.compose.material3.MaterialTheme.typography.bodySmall
                        )
                    }

                    OutlinedTextField(
                        value = latitud,
                        onValueChange = { latitud = it },
                        label = { Text("Latitud") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal
                        ),
                        shape = RoundedCornerShape(14.dp),
                        colors = coloresCampo,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = longitud,
                        onValueChange = { longitud = it },
                        label = { Text("Longitud") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Decimal
                        ),
                        shape = RoundedCornerShape(14.dp),
                        colors = coloresCampo,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                if (error != null) {
                    Text(
                        text = error!!,
                        color = Terracota,
                        style = androidx.compose.material3.MaterialTheme.typography.bodySmall
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val latitudNumero = latitud.trim()
                        .replace(',', '.')
                        .toDoubleOrNull()
                    val longitudNumero = longitud.trim()
                        .replace(',', '.')
                        .toDoubleOrNull()

                    error = when {
                        nombre.isBlank() ->
                            "El nombre del lugar es obligatorio."

                        latitud.isBlank() != longitud.isBlank() ->
                            "Introduce tanto la latitud como la longitud."

                        latitud.isNotBlank() && latitudNumero == null ->
                            "La latitud no es válida."

                        longitud.isNotBlank() && longitudNumero == null ->
                            "La longitud no es válida."

                        latitudNumero != null && latitudNumero !in -90.0..90.0 ->
                            "La latitud debe estar entre -90 y 90."

                        longitudNumero != null && longitudNumero !in -180.0..180.0 ->
                            "La longitud debe estar entre -180 y 180."

                        else -> null
                    }

                    if (error == null) {
                        onGuardar(
                            LugarDto(
                                id = lugarEditar?.id,
                                viajeId = viajeId,
                                nombre = nombre.trim(),
                                descripcion = descripcion.trim()
                                    .takeIf { it.isNotEmpty() },
                                direccion = direccion.trim()
                                    .takeIf { it.isNotEmpty() },
                                latitud = latitudNumero,
                                longitud = longitudNumero
                            )
                        )
                    }
                },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = VerdeOscuro,
                    contentColor = Color.White
                )
            ) {
                Text(if (lugarEditar == null) "Guardar lugar" else "Guardar cambios")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = VerdeBoton)
            }
        }
    )
}
