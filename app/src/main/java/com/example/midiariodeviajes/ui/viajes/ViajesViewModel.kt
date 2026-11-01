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
    val cargaCompletada: Boolean = false,
    val creacionCompletada: Boolean = false,
    val actualizacionCompletada: Boolean = false
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

    fun crearViaje(token: String, viaje: ViajeDto) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    cargando = true,
                    mensajeError = null,
                    creacionCompletada = false
                )
            }

            val resultado = viajesRepository.crearViaje(token, viaje)

            resultado.fold(
                onSuccess = { viajeCreado ->
                    _uiState.update { estado ->
                        estado.copy(
                            viajes = estado.viajes + viajeCreado,
                            cargando = false,
                            mensajeError = null,
                            creacionCompletada = true
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            cargando = false,
                            mensajeError = error.message
                                ?: "No se pudo guardar el viaje.",
                            creacionCompletada = false
                        )
                    }
                }
            )
        }
    }

    fun limpiarCreacionCompletada() {
        _uiState.update {
            it.copy(creacionCompletada = false)
        }
    }

    fun actualizarViaje(
        token: String,
        viajeId: String,
        viaje: ViajeDto
    ) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    cargando = true,
                    mensajeError = null,
                    actualizacionCompletada = false
                )
            }

            val resultado = viajesRepository.actualizarViaje(
                token = token,
                viajeId = viajeId,
                viaje = viaje
            )

            resultado.fold(
                onSuccess = { viajeActualizado ->
                    _uiState.update { estado ->
                        estado.copy(
                            viajes = estado.viajes.map { viajeEnLista ->
                                if (viajeEnLista.id == viajeId) {
                                    viajeActualizado
                                } else {
                                    viajeEnLista
                                }
                            },
                            cargando = false,
                            mensajeError = null,
                            actualizacionCompletada = true
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            cargando = false,
                            mensajeError = error.message
                                ?: "No se pudo actualizar el viaje.",
                            actualizacionCompletada = false
                        )
                    }
                }
            )
        }
    }

    fun limpiarActualizacionCompletada() {
        _uiState.update {
            it.copy(actualizacionCompletada = false)
        }
    }

    fun eliminarViaje(token: String, viajeId: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    cargando = true,
                    mensajeError = null
                )
            }

            val resultado = viajesRepository.eliminarViaje(token, viajeId)

            resultado.fold(
                onSuccess = {
                    _uiState.update { estado ->
                        estado.copy(
                            viajes = estado.viajes.filterNot { viaje ->
                                viaje.id == viajeId
                            },
                            cargando = false,
                            mensajeError = null
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            cargando = false,
                            mensajeError = error.message
                                ?: "No se pudo eliminar el viaje."
                        )
                    }
                }
            )
        }
    }
}