package com.example.midiariodeviajes.ui.viajes.formulario

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.midiariodeviajes.data.model.LugarDto

@Composable
fun AnadirLugarDialog(
    viajeId: String,
    onDismiss: () -> Unit,
    onGuardar: (LugarDto) -> Unit
) {
    var nombre by rememberSaveable { mutableStateOf("") }
    var descripcion by rememberSaveable { mutableStateOf("") }
    var direccion by rememberSaveable { mutableStateOf("") }
    var latitud by rememberSaveable { mutableStateOf("") }
    var longitud by rememberSaveable { mutableStateOf("") }
    var error by rememberSaveable { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Añadir lugar") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 440.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre *") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = direccion,
                    onValueChange = { direccion = it },
                    label = { Text("Dirección") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = latitud,
                    onValueChange = { latitud = it },
                    label = { Text("Latitud (opcional)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = longitud,
                    onValueChange = { longitud = it },
                    label = { Text("Longitud (opcional)") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Decimal
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                if (error != null) {
                    Text(error!!)
                }
            }
        },
        confirmButton = {
            TextButton(
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
                }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}