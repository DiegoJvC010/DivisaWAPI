package com.example.divisawapi.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.TypeConverters


//Interfaz que define las consultas que se pueden realizar sobre la tabla de la BD
@Dao
@TypeConverters(Converters::class)
interface ExchangeRateDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(exchangeRate: ExchangeRateEntity)

    @Query("SELECT * FROM exchange_rates ORDER BY id DESC")
    suspend fun getAllRates(): List<ExchangeRateEntity>

}