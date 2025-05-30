package dev.alepando.model

data class ConnectionReport(
    val uid: String,
    val room: String,
    val hostname: String,
    val user: String,             // user actual (En caso de obtener nombre de la Avi, si no el del desktop)
    val activeConnections: List<String>,  // dominios
    val timestamp: Long,         // momento del envío
    val activeApps: List<String> // procesos abiertos
)
