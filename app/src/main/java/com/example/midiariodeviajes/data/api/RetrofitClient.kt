package com.example.midiariodeviajes.data.api

import com.example.midiariodeviajes.data.ApiConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    val viajesApi: ViajesApi by lazy {
        Retrofit.Builder()
            .baseUrl(ApiConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ViajesApi::class.java)
    }
}