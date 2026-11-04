package com.example.midiariodeviajes.ui.viajes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.midiariodeviajes.data.LugaresRepository
import com.example.midiariodeviajes.data.model.LugarDto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LugaresUiState(
    val lugares: List<LugarDto> = emptyList(),
    val cargando: Boolean = false,
    val mensajeError: String? = null
)

class LugaresViewModel(
    private val lugaresRepository: LugaresRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LugaresUiState())
    val uiState = _uiState.asStateFlow()

    fun cargarLugares(token: String, viajeId: String) {
        viewModelScope.launch {
            _uiState.value = LugaresUiState(cargando = true)

            val resultado = lugaresRepository.obtenerLugares(
                token = token,
                viajeId = viajeId
            )

            _uiState.value = resultado.fold(
                onSuccess = { lugares ->
                    LugaresUiState(lugares = lugares)
                },
                onFailure = { error ->
                    LugaresUiState(
                        mensajeError = error.message
                            ?: "No se pudieron cargar los lugares."
                    )
                }
            )
        }
    }
}