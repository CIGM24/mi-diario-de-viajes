package com.example.midiariodeviajes.data.api

import com.example.midiariodeviajes.data.model.LoginRequestDto
import com.example.midiariodeviajes.data.model.LoginResponseDto
import com.example.midiariodeviajes.data.model.ViajeDto
import com.example.midiariodeviajes.data.model.RegistroRequestDto
import com.example.midiariodeviajes.data.model.RegistroResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ViajesApi {

    @POST("api/auth/login")
    suspend fun iniciarSesion(
        @Body request: LoginRequestDto
    ): Response<LoginResponseDto>

    @POST("api/auth/register")
    suspend fun registrarUsuario(
        @Body request: RegistroRequestDto
    ): Response<RegistroResponseDto>

    @GET("api/viajes")
    suspend fun obtenerViajes(
        @retrofit2.http.Header("Authorization") token: String
    ): Response<List<ViajeDto>>

    @POST("api/viajes")
    suspend fun crearViaje(
        @retrofit2.http.Header("Authorization") token: String,
        @Body viaje: ViajeDto
    ): Response<ViajeDto>
}