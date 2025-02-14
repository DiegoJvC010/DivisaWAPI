package com.example.divisawapi.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [ExchangeRateEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class) //Indica que usará los conversores personalizados (para Map<String, Double>)
abstract class AppDatabase : RoomDatabase() {

    abstract fun exchangeRateDao(): ExchangeRateDao

    // Singleton para asegurar que solo haya una instancia de la base de datos en ejecución.
    companion object {
        @Volatile private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "divisas_db"
                ).build().also { instance = it }
            }
    }
}
