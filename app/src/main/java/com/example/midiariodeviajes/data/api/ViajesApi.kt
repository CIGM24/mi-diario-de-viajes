package com.example.midiariodeviajes.data.api

import com.example.midiariodeviajes.data.model.LoginRequestDto
import com.example.midiariodeviajes.data.model.LoginResponseDto
import com.example.midiariodeviajes.data.model.ViajeDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ViajesApi {

    @POST("api/auth/login")
    suspend fun iniciarSesion(
        @Body request: LoginRequestDto
    ): Response<LoginResponseDto>

    @GET("api/viajes")
    suspend fun obtenerViajes(
        @retrofit2.http.Header("Authorization") token: String
    ): Response<List<ViajeDto>>
}