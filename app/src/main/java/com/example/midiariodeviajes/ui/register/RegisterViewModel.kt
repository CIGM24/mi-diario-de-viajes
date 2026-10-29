package com.example.midiariodeviajes.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.midiariodeviajes.data.RegistroRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class RegisterUiState(
    val cargando: Boolean = false,
    val mensajeError: String? = null,
    val mensajeExito: String? = null
)

class RegisterViewModel(
    private val registroRepository: RegistroRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()

    fun registrar(nombre: String, email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = RegisterUiState(cargando = true)

            val resultado = registroRepository.registrarUsuario(
                nombre = nombre,
                email = email,
                password = password
            )

            _uiState.value = resultado.fold(
                onSuccess = {
                    RegisterUiState(
                        mensajeExito = "¡Cuenta creada! Ya puedes iniciar sesión."
                    )
                },
                onFailure = { error ->
                    RegisterUiState(
                        mensajeError = error.message
                            ?: "No se pudo crear la cuenta."
                    )
                }
            )
        }
    }
}