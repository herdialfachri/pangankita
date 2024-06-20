package com.herdialfachri.pangankita.ui.data.home_api

import retrofit2.Call
import retrofit2.http.GET

interface HomeService {
    @GET("api/food/search?q=t")
    fun getMorty(): Call<SayuranResponse>
}