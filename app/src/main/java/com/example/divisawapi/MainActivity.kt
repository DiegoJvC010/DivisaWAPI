package com.example.divisawapi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.divisawapi.worker.ExchangeRateWorker
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*
        val workRequest = PeriodicWorkRequestBuilder<ExchangeRateWorker>(15, TimeUnit.MINUTES).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "exchange_rate_worker",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
        */
        //Prueba Instantanea
        val workRequest = OneTimeWorkRequestBuilder<ExchangeRateWorker>().build()

        WorkManager.getInstance(this).enqueue(workRequest)

        finish()
    }
}

