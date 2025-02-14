package com.example.divisawapi.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object RetrofitClient{
    private const val urlBase = "https://v6.exchangerate-api.com/"

    val apiService: ApiService by lazy {
        Retrofit.Builder().baseUrl(urlBase).addConverterFactory(GsonConverterFactory.create()).build().create(ApiService::class.java)
    }
}