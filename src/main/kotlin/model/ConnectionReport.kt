package dev.alepando.model

data class ConnectionReport(
    val uid: String,
    val room: String,
    val hostname: String,
    val user: String,             // Usuario actual (En caso de obtener nombre de la Avi)
    val activeConnections: List<String>,  // Lista de dominios o IPs conectadas
    val timestamp: Long,         // Momento del envío
    val activeApps: List<String> // procesos abiertos
)
