package com.example.divisawapi.provider

import android.net.Uri

object DivisasContract {
    // Autoridad del ContentProvider (debe coincidir con la declarada en AndroidManifest).
    const val AUTHORITY = "com.example.divisawapi.divisasprovider"

    // URI base para acceder a la información de Exchange Rates.
    val CONTENT_URI: Uri = Uri.parse("content://$AUTHORITY/exchange_rates")

    // Columnas que expondrá el ContentProvider en el Cursor.
    // Ajusta los nombres si quieres mostrar más o menos columnas.
    const val COLUMN_ID = "id"
    const val COLUMN_TIME_LAST_UPDATE = "timeLastUpdateUtc"
    const val COLUMN_EXCHANGE_RATE = "exchangeRate"
}
