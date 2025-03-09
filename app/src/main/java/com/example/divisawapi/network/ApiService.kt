package com.example.divisawapi.network

import com.example.divisawapi.data.ExchangeRateResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService{
    @GET("v6/ea89034455f0918eb70bb598/latest/MXN")
    suspend fun getExchangeRates(): Response<ExchangeRateResponse>
}