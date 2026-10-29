package com.example.midiariodeviajes.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.midiariodeviajes.data.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val cargando: Boolean = false,
    val token: String? = null,
    val mensajeError: String? = null
)

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    fun iniciarSesion(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = LoginUiState(cargando = true)

            val resultado = authRepository.iniciarSesion(email, password)

            _uiState.value = resultado.fold(
                onSuccess = { token ->
                    LoginUiState(token = token)
                },
                onFailure = { error ->
                    LoginUiState(
                        mensajeError = error.message
                            ?: "No se pudo iniciar sesión."
                    )
                }
            )
        }
    }
}