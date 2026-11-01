package com.example.midiariodeviajes.data

import com.example.midiariodeviajes.data.api.RetrofitClient
import com.example.midiariodeviajes.data.model.ViajeDto

class ViajesRepository {

    suspend fun obtenerViajes(token: String): Result<List<ViajeDto>> {
        return try {
            val respuesta = RetrofitClient.viajesApi.obtenerViajes(
                token = "Bearer $token"
            )

            if (respuesta.isSuccessful) {
                Result.success(respuesta.body() ?: emptyList())
            } else {
                Result.failure(
                    Exception("No se pudieron cargar los viajes (${respuesta.code()}).")
                )
            }
        } catch (e: Exception) {
            Result.failure(
                Exception("No se pudieron cargar los viajes. Comprueba la conexión.")
            )
        }
    }

    suspend fun eliminarViaje(token: String, viajeId: String): Result<Unit> {
        return try {
            val respuesta = RetrofitClient.viajesApi.eliminarViaje(
                token = "Bearer $token",
                id = viajeId
            )

            if (respuesta.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(
                    Exception("No se pudo eliminar el viaje (${respuesta.code()}).")
                )
            }
        } catch (e: Exception) {
            Result.failure(
                Exception("No se pudo eliminar el viaje. Comprueba la conexión.")
            )
        }
    }
}