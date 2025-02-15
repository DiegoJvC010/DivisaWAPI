package com.example.divisawapi

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.divisawapi.data.ExchangeRateRepository
import com.example.divisawapi.worker.ExchangeRateWorker
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    private val repository by lazy { ExchangeRateRepository(applicationContext) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val workRequest = PeriodicWorkRequestBuilder<ExchangeRateWorker>(60, TimeUnit.MINUTES).build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "exchange_rate_worker",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
        //Prueba Instantanea
        /*val workRequest = OneTimeWorkRequestBuilder<ExchangeRateWorker>().build()
        WorkManager.getInstance(this).enqueue(workRequest)
         */
        lifecycleScope.launch {
            val allRates = repository.getAllExchangeRates()
            if (allRates.isEmpty()) {
                Log.d("MainActivity", "No hay registros en la base de datos")
            } else {
                allRates.forEach { rate ->
                    Log.d("MainActivity", "Registro ID: ${rate.id} - Última actualización: ${rate.timeLastUpdateUtc}")
                    Log.d("MainActivity", "Tasas de cambio: ${rate.rates}")
                }
            }
        }


    }
}

