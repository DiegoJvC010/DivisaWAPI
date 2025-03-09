package com.example.divisawapi.network

import com.example.divisawapi.data.ExchangeRateResponse
import retrofit2.Response
import retrofit2.http.GET

interface ApiService{
    @GET("v6/21979ca005773ce125edf08e/latest/MXN")
    suspend fun getExchangeRates(): Response<ExchangeRateResponse>
}