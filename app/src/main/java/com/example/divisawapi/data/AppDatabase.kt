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
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "exchange_rate_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
