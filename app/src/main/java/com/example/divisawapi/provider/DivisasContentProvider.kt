package com.example.divisawapi.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.pm.PackageManager
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.util.Log
import com.example.divisawapi.data.AppDatabase
import com.example.divisawapi.data.Converters
import java.text.SimpleDateFormat
import java.util.*

class DivisasContentProvider : ContentProvider() {

    override fun onCreate(): Boolean {
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        // 1. Verificar el permiso
        val requiredPermission = "com.example.divisawapi.permission.ACCESS_DIVISA"
        if (context?.checkCallingOrSelfPermission(requiredPermission) != PackageManager.PERMISSION_GRANTED) {
            throw SecurityException("Se requiere el permiso $requiredPermission para acceder a este ContentProvider.")
        }

        // 2. Extraer parámetros
        val currency = uri.getQueryParameter("currency")
        val startDateStr = uri.getQueryParameter("startDate")
        val endDateStr = uri.getQueryParameter("endDate")

        if (currency.isNullOrEmpty() || startDateStr.isNullOrEmpty() || endDateStr.isNullOrEmpty()) {
            Log.e("DivisasContentProvider", "Faltan parámetros: currency, startDate o endDate.")
            return null
        }

        // 3. Parsear las fechas de inicio y fin
        val dateFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US)
        val startDate: Date
        val endDate: Date
        try {
            startDate = dateFormat.parse(startDateStr) ?: return null
            endDate = dateFormat.parse(endDateStr) ?: return null
        } catch (e: Exception) {
            Log.e("DivisasContentProvider", "Error al parsear fechas: ${e.localizedMessage}")
            return null
        }

        // 4. Obtener TODOS los registros
        val db = AppDatabase.getDatabase(context!!)
        val rawCursor = db.exchangeRateDao().getAllRatesCursor() // En vez de getRatesByDateRange

        // 5. Crear un MatrixCursor para exponer los resultados
        val matrixCursor = MatrixCursor(
            arrayOf(
                DivisasContract.COLUMN_ID,
                DivisasContract.COLUMN_TIME_LAST_UPDATE,
                DivisasContract.COLUMN_EXCHANGE_RATE
            )
        )

        // 6. Convertir la columna 'rates' a Map
        val converters = Converters()

        // 7. Recorrer todos los registros y filtrar en memoria
        rawCursor?.use { cursor ->
            // Índices de columnas
            val colId = cursor.getColumnIndexOrThrow("id")
            val colTimeUpdate = cursor.getColumnIndexOrThrow("timeLastUpdateUtc")
            val colRates = cursor.getColumnIndexOrThrow("rates")

            while (cursor.moveToNext()) {
                val id = cursor.getInt(colId)
                val timeLastUpdateStr = cursor.getString(colTimeUpdate)
                val ratesJson = cursor.getString(colRates)

                // Parsear la fecha de la BD
                val recordDate = try {
                    dateFormat.parse(timeLastUpdateStr)
                } catch (e: Exception) {
                    null
                }

                if (recordDate != null) {
                    // Verificar si está en el rango [startDate, endDate]
                    if (!recordDate.before(startDate) && !recordDate.after(endDate)) {
                        // Está dentro del rango
                        val rateMap = converters.toRatesMap(ratesJson)
                        val exchangeRate = rateMap[currency]
                        if (exchangeRate != null) {
                            matrixCursor.addRow(arrayOf(id, timeLastUpdateStr, exchangeRate))
                        }
                    }
                }
            }
        }

        return matrixCursor
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        throw UnsupportedOperationException("Operación insert no soportada en DivisasContentProvider")
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        throw UnsupportedOperationException("Operación update no soportada en DivisasContentProvider")
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        throw UnsupportedOperationException("Operación delete no soportada en DivisasContentProvider")
    }

    override fun getType(uri: Uri): String? {
        return "vnd.android.cursor.dir/vnd.${DivisasContract.AUTHORITY}.exchange_rates"
    }
}
