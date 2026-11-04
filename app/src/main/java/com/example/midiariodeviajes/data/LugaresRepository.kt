package com.example.midiariodeviajes.data

import com.example.midiariodeviajes.data.api.RetrofitClient
import com.example.midiariodeviajes.data.model.LugarDto

class LugaresRepository {

    suspend fun obtenerLugares(
        token: String,
        viajeId: String
    ): Result<List<LugarDto>> {
        return try {
            val respuesta = RetrofitClient.viajesApi.obtenerLugares(
                token = "Bearer $token",
                viajeId = viajeId
            )

            if (respuesta.isSuccessful) {
                Result.success(respuesta.body() ?: emptyList())
            } else {
                Result.failure(
                    Exception(
                        "No se pudieron cargar los lugares (${respuesta.code()})."
                    )
                )
            }
        } catch (e: Exception) {
            Result.failure(
                Exception("No se pudieron cargar los lugares. Comprueba la conexión.")
            )
        }
    }
}