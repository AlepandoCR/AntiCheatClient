package dev.alepando.monitor

object NetworkScanner {
    fun getActiveConnections(): List<String> {
        val process = ProcessBuilder("netstat", "-n")
            .redirectErrorStream(true)
            .start()
        val output = process.inputStream.bufferedReader().readLines()

        return output
            .filter { it.startsWith("  TCP") || it.startsWith("  UDP") }
            .mapNotNull {
                val parts = it.trim().split(Regex("\\s+"))
                parts.getOrNull(2) // Dirección remota
            }
            .filter { it.isNotBlank() && !it.startsWith("127.") } // Evita loopback
    }
}
