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

    suspend fun crearViaje(
        token: String,
        viaje: ViajeDto
    ): Result<ViajeDto> {
        return try {
            val respuesta = RetrofitClient.viajesApi.crearViaje(
                token = "Bearer $token",
                viaje = viaje
            )

            if (respuesta.isSuccessful) {
                val viajeCreado = respuesta.body()

                if (viajeCreado != null) {
                    Result.success(viajeCreado)
                } else {
                    Result.failure(
                        Exception("El servidor no devolvió los datos del viaje creado.")
                    )
                }
            } else {
                Result.failure(
                    Exception("No se pudo guardar el viaje (${respuesta.code()}).")
                )
            }
        } catch (e: Exception) {
            Result.failure(
                Exception("No se pudo guardar el viaje. Comprueba la conexión.")
            )
        }
    }

    suspend fun actualizarViaje(
        token: String,
        viajeId: String,
        viaje: ViajeDto
    ): Result<ViajeDto> {
        return try {
            val respuesta = RetrofitClient.viajesApi.actualizarViaje(
                token = "Bearer $token",
                id = viajeId,
                viaje = viaje
            )

            if (respuesta.isSuccessful) {
                val viajeActualizado = respuesta.body()

                if (viajeActualizado != null) {
                    Result.success(viajeActualizado)
                } else {
                    Result.failure(
                        Exception("El servidor no devolvió los datos del viaje actualizado.")
                    )
                }
            } else {
                Result.failure(
                    Exception("No se pudo actualizar el viaje (${respuesta.code()}).")
                )
            }
        } catch (e: Exception) {
            Result.failure(
                Exception("No se pudo actualizar el viaje. Comprueba la conexión.")
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