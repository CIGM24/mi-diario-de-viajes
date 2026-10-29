package com.example.midiariodeviajes.ui.viajes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.midiariodeviajes.data.ViajesRepository
import com.example.midiariodeviajes.data.model.ViajeDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ViajesUiState(
    val viajes: List<ViajeDto> = emptyList(),
    val cargando: Boolean = false,
    val mensajeError: String? = null,
    val cargaCompletada: Boolean = false
)

class ViajesViewModel(
    private val viajesRepository: ViajesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ViajesUiState())
    val uiState = _uiState.asStateFlow()

    fun cargarViajes(token: String) {
        viewModelScope.launch {
            _uiState.value = ViajesUiState(cargando = true)

            val resultado = viajesRepository.obtenerViajes(token)

            _uiState.value = resultado.fold(
                onSuccess = { viajes ->
                    ViajesUiState(
                        viajes = viajes,
                        cargaCompletada = true
                    )
                },
                onFailure = { error ->
                    ViajesUiState(
                        mensajeError = error.message
                            ?: "No se pudieron cargar los viajes."
                    )
                }
            )
        }
    }
}