package com.example.midiariodeviajes.data

import com.example.midiariodeviajes.data.api.RetrofitClient
import com.example.midiariodeviajes.data.model.RegistroRequestDto

class RegistroRepository {

    suspend fun registrarUsuario(
        nombre: String,
        email: String,
        password: String
    ): Result<Unit> {
        return try {
            val respuesta = RetrofitClient.viajesApi.registrarUsuario(
                RegistroRequestDto(
                    nombre = nombre,
                    email = email,
                    password = password
                )
            )

            when {
                respuesta.isSuccessful -> Result.success(Unit)

                respuesta.code() == 409 ->
                    Result.failure(Exception("Ese correo electrónico ya está registrado."))

                respuesta.code() == 400 ->
                    Result.failure(Exception("Los datos introducidos no son válidos."))

                else ->
                    Result.failure(
                        Exception("No se pudo crear la cuenta (error ${respuesta.code()}).")
                    )
            }
        } catch (e: Exception) {
            Result.failure(
                Exception("No se pudo conectar con el servidor. Comprueba la conexión.")
            )
        }
    }
}
