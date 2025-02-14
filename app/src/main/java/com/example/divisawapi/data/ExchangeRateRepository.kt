package com.example.divisawapi.data

import android.content.Context
import com.example.divisawapi.network.RetrofitClient

class ExchangeRateRepository(context: Context){
    private val exchangeRateDao = AppDatabase.getDatabase(context).exchangeRateDao()

    suspend fun fetchExchangeRatesFromRemote(): ExchangeRateResponse?{
        val response = RetrofitClient.apiService.getExchangeRates()
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun insertExchangeRate(entity: ExchangeRateEntity){
        exchangeRateDao.insert(entity)
    }

    suspend fun getAllExchangeRates(): List<ExchangeRateEntity> = exchangeRateDao.getAllRates()
}