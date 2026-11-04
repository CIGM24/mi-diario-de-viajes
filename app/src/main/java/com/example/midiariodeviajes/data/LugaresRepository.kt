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

    suspend fun crearLugar(
        token: String,
        viajeId: String,
        lugar: LugarDto
    ): Result<LugarDto> {
        return try {
            val respuesta = RetrofitClient.viajesApi.crearLugar(
                token = "Bearer $token",
                viajeId = viajeId,
                lugar = lugar
            )

            if (respuesta.isSuccessful) {
                val lugarGuardado = respuesta.body()

                if (lugarGuardado != null) {
                    Result.success(lugarGuardado)
                } else {
                    Result.failure(
                        Exception("El servidor no devolvió los datos del lugar.")
                    )
                }
            } else {
                Result.failure(
                    Exception("No se pudo guardar el lugar (${respuesta.code()}).")
                )
            }
        } catch (e: Exception) {
            Result.failure(
                Exception("No se pudo guardar el lugar. Comprueba la conexión.")
            )
        }
    }

    suspend fun actualizarLugar(
        token: String,
        lugarId: String,
        lugar: LugarDto
    ): Result<LugarDto> {
        return try {
            val respuesta = RetrofitClient.viajesApi.actualizarLugar(
                token = "Bearer $token",
                id = lugarId,
                lugar = lugar
            )

            if (respuesta.isSuccessful) {
                val lugarActualizado = respuesta.body()

                if (lugarActualizado != null) {
                    Result.success(lugarActualizado)
                } else {
                    Result.failure(
                        Exception("El servidor no devolvió los datos del lugar actualizado.")
                    )
                }
            } else {
                Result.failure(
                    Exception("No se pudo actualizar el lugar (${respuesta.code()}).")
                )
            }
        } catch (e: Exception) {
            Result.failure(
                Exception("No se pudo actualizar el lugar. Comprueba la conexión.")
            )
        }
    }
}