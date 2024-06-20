package com.herdialfachri.pangankita.ui.data.home_api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object HomeConfig {

    const val baseUrl = "https://rickandmortyapi.com/api/"
    const val baseUrl2 = "https://backend-xglaudyh2q-et.a.run.app/"

    fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl2)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getService(): HomeService {
        return getRetrofit().create(HomeService::class.java)
    }
}