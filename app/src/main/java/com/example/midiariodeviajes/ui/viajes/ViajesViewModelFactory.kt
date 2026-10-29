package com.example.midiariodeviajes.ui.viajes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.midiariodeviajes.data.ViajesRepository

class ViajesViewModelFactory(
    private val viajesRepository: ViajesRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViajesViewModel::class.java)) {
            return ViajesViewModel(viajesRepository) as T
        }

        throw IllegalArgumentException(
            "ViewModel no reconocido: ${modelClass.name}"
        )
    }
}