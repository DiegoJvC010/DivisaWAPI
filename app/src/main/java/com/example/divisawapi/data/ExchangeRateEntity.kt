package com.example.divisawapi.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exchange_rates")
data class ExchangeRateEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val timeLastUpdateUtc: String,
    val timeNextUpdateUtc: String,
    val rates: Map<String, Double>
)
