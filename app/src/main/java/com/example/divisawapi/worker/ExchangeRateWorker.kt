package com.example.divisawapi.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.divisawapi.data.ExchangeRateEntity
import com.example.divisawapi.data.ExchangeRateRepository

class ExchangeRateWorker(context: Context, params: WorkerParameters): CoroutineWorker(context, params){
    private val repository = ExchangeRateRepository(applicationContext)

    override suspend fun doWork(): Result {
        try {
            val data = repository.fetchExchangeRatesFromRemote()
            if (data != null){
                val entity = ExchangeRateEntity(
                    timeLastUpdateUtc = data.time_last_update_utc,
                    timeNextUpdateUtc =  data.time_next_update_utc,
                    rates = data.conversion_rates
                )
                repository.insertExchangeRate(entity)

                Log.d("ExchangeRateWorker", "¡¡¡¡¡¡¡Nueva actualización!!!!!!!")
                Log.d("ExchangeRateWorker", "Hora de actualización: ${data.time_last_update_utc}")
                Log.d("ExchangeRateWorker", "Siguiente actualización: ${data.time_next_update_utc}")
                Log.d("ExchangeRateWorker", "Tasas de cambio: ${data.conversion_rates}")

                val allRates = repository.getAllExchangeRates()
                Log.d("ExchangeRateWorker","Registros almacenados:")
                allRates.forEach{
                    Log.d("ExchangeRateWorker","ID: ${it.id} - Tiempo de actualización: ${it.timeLastUpdateUtc}")
                    Log.d("ExchangeRateWorker","Tasas de cambio: ${it.rates}")
                }
            }else{
                Log.e("ExchangeRateWorker","No se obtuvieron datos de la API")
                return Result.retry()
            }
        }catch (e: Exception){
            Log.e("ExchangeRateWorker","Hubo un error: ${e.localizedMessage}")
            return  Result.retry()
        }
        return Result.success()
    }
}