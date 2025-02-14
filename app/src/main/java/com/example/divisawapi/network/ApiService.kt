package com.example.divisawapi.network

import com.example.divisawapi.data.ExchangeRateResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService{
    @GET("v6/1b18c892e069c707ffea1e48/latest/MXN")
    suspend fun getExchangeRates(): Response<ExchangeRateResponse>
}