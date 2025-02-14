package com.example.divisawapi.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ExchangeRateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exchangeRate: ExchangeRateEntity)

    @Query("SELECT * FROM exchange_rates ORDER BY id DESC")
    suspend fun getAllRates(): List<ExchangeRateEntity>
}