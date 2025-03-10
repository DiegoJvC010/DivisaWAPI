package com.example.divisawapi.data

import android.database.Cursor
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

    //Obtiene todos los registros de la tabla en orden descendente por ID y los devuelve en un Cursor
    //para el ContentProvider
    @Query("SELECT * FROM exchange_rates ORDER BY id DESC")
    fun getAllRatesCursor(): Cursor

}