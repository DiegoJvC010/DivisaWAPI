package com.example.divisawapi.provider


object DivisasContract {
    //Autoridad del ContentProvider, utilizada para identificar de manera unica al proveedor de datos
    //debe coincidir con la declaración en el AndroidManifest
    const val AUTHORITY = "com.example.divisawapi.divisasprovider"

    //Nombres de las columnas expuestas en el Cursor que devuelve el ContentProvider
    //Se utilizan para asegurar que los nombres de las columnas sean consistentes en las consultas
    //y en la implementacion del ContentProvider
    const val COLUMN_ID = "id"
    const val COLUMN_TIME_LAST_UPDATE = "timeLastUpdateUtc"
    const val COLUMN_EXCHANGE_RATE = "exchangeRate"
}
