package com.example.midiariodeviajes.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.midiariodeviajes.data.RegistroRepository

class RegisterViewModelFactory(
    private val registroRepository: RegistroRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            return RegisterViewModel(registroRepository) as T
        }

        throw IllegalArgumentException(
            "ViewModel no reconocido: ${modelClass.name}"
        )
    }
}