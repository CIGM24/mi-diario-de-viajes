package com.example.midiariodeviajes.data.model

import com.google.gson.annotations.SerializedName

data class RegistroRequestDto(
    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("email")
    val email: String,

    @SerializedName("password")
    val password: String
)
