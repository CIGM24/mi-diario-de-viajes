package com.example.midiariodeviajes.data.model

import com.google.gson.annotations.SerializedName

data class ViajeDto(
    @SerializedName("id")
    val id: String? = null,

    @SerializedName("nombre")
    val nombre: String,

    @SerializedName("destino")
    val destino: String,

    @SerializedName("fechaInicio")
    val fechaInicio: String,

    @SerializedName("fechaFin")
    val fechaFin: String,

    @SerializedName("estado")
    val estado: String,

    @SerializedName("presupuesto")
    val presupuesto: Double? = null,

    @SerializedName("descripcion")
    val descripcion: String? = null,

    @SerializedName("fotoUrl")
    val fotoUrl: String? = null
)