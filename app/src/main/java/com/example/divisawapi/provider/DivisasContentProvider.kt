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
        return true  //El ContentProvider fue creado correctamente
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {

        //Verifica que la aplicacion que hace la consulta tenga el permiso necesario
        val requiredPermission = "com.example.divisawapi.permission.ACCESS_DIVISA"
        if (context?.checkCallingOrSelfPermission(requiredPermission) != PackageManager.PERMISSION_GRANTED) {
            throw SecurityException("Se requiere el permiso $requiredPermission para acceder a este ContentProvider.")
        }

        //Extrae los parametros enviados en la consulta (la moneda y el rango de fechas)
        val currency = uri.getQueryParameter("currency") //Moneda solicitada (ejemplo: "trumpcoins")
        val startDateStr = uri.getQueryParameter("startDate") //Fecha de inicio del rango
        val endDateStr = uri.getQueryParameter("endDate") //Fecha de fin del rango

        //Si falta algun parametro, se muestra un error y se detiene la consulta(pa debugiar)
        if (currency.isNullOrEmpty() || startDateStr.isNullOrEmpty() || endDateStr.isNullOrEmpty()) {
            Log.e("DivisasContentProvider", "Faltan parámetros: currency, startDate o endDate.")
            return null
        }

        //Convertir las fechas recibidas (en formato String) a objetos Date
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

        //Obtener todos los registros de la base de datos
        val db = AppDatabase.getDatabase(context!!) //Aqui se accede a la base de datos
        val rawCursor = db.exchangeRateDao().getAllRatesCursor() //Aqui se Obtienen las tasas de cambio

        //Cursor para devolver los resultados filtrados
        val matrixCursor = MatrixCursor(
            arrayOf(
                DivisasContract.COLUMN_ID, //ID del registro
                DivisasContract.COLUMN_TIME_LAST_UPDATE, //Fecha de la actualizacion
                DivisasContract.COLUMN_EXCHANGE_RATE //Valor de la tasa de cambio
            )
        )
        //Convertidor para transformar la columna 'rates' a Map
        val converters = Converters()

        //Recorrer todos los registros obtenidos de la base de datos y filtrarlos
        rawCursor?.use { cursor ->
            //Indices de columnas
            val colId = cursor.getColumnIndexOrThrow("id")
            val colTimeUpdate = cursor.getColumnIndexOrThrow("timeLastUpdateUtc")
            val colRates = cursor.getColumnIndexOrThrow("rates")

            while (cursor.moveToNext()) {
                val id = cursor.getInt(colId)
                val timeLastUpdateStr = cursor.getString(colTimeUpdate)//Fecha de actualizacion (String)
                val ratesJson = cursor.getString(colRates)//JSON con las tasas de cambio

                //Convertir la fecha obtenida de la BD a un objeto Date
                val recordDate = try {
                    dateFormat.parse(timeLastUpdateStr)
                } catch (e: Exception) {
                    null
                }

                if (recordDate != null) {
                    //Verificar si la fecha está dentro del rango solicitado por el usuario
                    if (!recordDate.before(startDate) && !recordDate.after(endDate)) {

                        //Convertir los valores JSON en un mapa de tasas de cambio
                        val rateMap = converters.toRatesMap(ratesJson)
                        val exchangeRate = rateMap[currency]//Obtener la tasa de cambio solicitada

                        //Si la tasa de cambio existe, se agrega al cursor
                        if (exchangeRate != null) {
                            matrixCursor.addRow(arrayOf(id, timeLastUpdateStr, exchangeRate))
                        }
                    }
                }
            }
        }
        //Se devuelven los datos filtrados en el cursor
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
        //Indica que este ContentProvider devuelve una lista de registros
        return "vnd.android.cursor.dir/vnd.${DivisasContract.AUTHORITY}.exchange_rates"
    }
}
