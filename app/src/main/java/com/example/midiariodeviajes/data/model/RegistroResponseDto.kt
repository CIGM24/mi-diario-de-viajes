package com.example.midiariodeviajes.data.model

import com.google.gson.annotations.SerializedName

data class RegistroResponseDto(
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("email")
    val email: String
)
