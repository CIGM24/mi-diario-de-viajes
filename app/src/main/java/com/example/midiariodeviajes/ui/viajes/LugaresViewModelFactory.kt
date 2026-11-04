package com.example.midiariodeviajes.ui.viajes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.midiariodeviajes.data.LugaresRepository

class LugaresViewModelFactory(
    private val lugaresRepository: LugaresRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LugaresViewModel::class.java)) {
            return LugaresViewModel(lugaresRepository) as T
        }

        throw IllegalArgumentException(
            "ViewModel no reconocido: ${modelClass.name}"
        )
    }
}