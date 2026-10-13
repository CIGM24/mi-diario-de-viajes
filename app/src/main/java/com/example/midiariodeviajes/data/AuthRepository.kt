package com.example.midiariodeviajes.data

import com.example.midiariodeviajes.data.api.RetrofitClient
import com.example.midiariodeviajes.data.model.LoginRequestDto

class AuthRepository {

    suspend fun iniciarSesion(
        email: String,
        password: String
    ): Result<String> {
        return try {
            val respuesta = RetrofitClient.viajesApi.iniciarSesion(
                LoginRequestDto(
                    email = email,
                    password = password
                )
            )

            if (respuesta.isSuccessful) {
                val token = respuesta.body()?.token

                if (!token.isNullOrBlank()) {
                    Result.success(token)
                } else {
                    Result.failure(
                        Exception("El servidor no ha devuelto el token.")
                    )
                }
            } else {
                Result.failure(
                    Exception(
                        if (respuesta.code() == 401) {
                            "El correo o la contraseña no son correctos."
                        } else {
                            "Error del servidor (${respuesta.code()})."
                        }
                    )
                )
            }
        } catch (e: Exception) {
            Result.failure(
                Exception("No se pudo conectar con el servidor. Comprueba la conexión.")
            )
        }
    }
}