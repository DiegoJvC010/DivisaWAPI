package com.example.divisawapi.network

import com.example.divisawapi.data.ExchangeRateResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService{
    @GET("v6/acbae99336b098792024ca02/latest/MXN")
    suspend fun getExchangeRates(): Response<ExchangeRateResponse>
}