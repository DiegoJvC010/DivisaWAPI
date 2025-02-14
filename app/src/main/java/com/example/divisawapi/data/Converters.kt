package com.example.divisawapi.data

import android.util.Log
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

//Clase que contiene convertidores para que Room pueda almacenar tipos complejos
class Converters {

        //Convierte un Map<String, Double> a un String JSON para guardarlo en la base de datos
        @TypeConverter
        fun fromRatesMap(value: Map<String, Double>): String {
            return Gson().toJson(value)
        }
        //Convierte un String JSON a un Map<String, Double> cuando se lee desde la base de datos
        @TypeConverter
        fun toRatesMap(value: String): Map<String, Double> {
            val mapType = object : TypeToken<Map<String, Double>>() {}.type
            return Gson().fromJson(value, mapType)
        }
}
