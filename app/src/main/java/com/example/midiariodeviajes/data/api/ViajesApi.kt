package com.example.midiariodeviajes.data.api

import com.example.midiariodeviajes.data.model.LoginRequestDto
import com.example.midiariodeviajes.data.model.LoginResponseDto
import com.example.midiariodeviajes.data.model.ViajeDto
import com.example.midiariodeviajes.data.model.RegistroRequestDto
import com.example.midiariodeviajes.data.model.RegistroResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.Part


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

    @GET("api/viajes/{id}")
    suspend fun obtenerViaje(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<ViajeDto>

    @PUT("api/viajes/{id}")
    suspend fun actualizarViaje(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body viaje: ViajeDto
    ): Response<ViajeDto>

    @DELETE("api/viajes/{id}")
    suspend fun eliminarViaje(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<Void>

    @Multipart
    @POST("api/viajes/{id}/foto")
    suspend fun subirFoto(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Part archivo: MultipartBody.Part
    ): Response<Map<String, String>>
}
