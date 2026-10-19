package com.example.midiariodeviajes.data.model


import com.google.gson.annotations.SerializedName

data class LugarDto(
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("viajeId")
    val viajeId: String? = null,

    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("descripcion")
    val descripcion: String? = null,

    @SerializedName("direccion")
    val direccion: String? = null,

    @SerializedName("latitud")
    val latitud: Double? = null,

    @SerializedName("longitud")
    val longitud: Double? = null
)
