package dev.alepando.monitor

import java.net.InetAddress
import java.util.concurrent.*

object NetworkScanner {
    private val cache = ConcurrentHashMap<String, String?>()

    fun getActiveConnectionsWithNames(): List<Pair<String, String?>> {
        val process = ProcessBuilder("netstat", "-n")
            .redirectErrorStream(true)
            .start()
        val output = process.inputStream.bufferedReader().readLines()

        val ips = output
            .filter { it.startsWith("  TCP") || it.startsWith("  UDP") }
            .mapNotNull {
                val parts = it.trim().split(Regex("\\s+"))
                parts.getOrNull(2)?.split(":")?.get(0) // sin puerto
            }
            .filter { it.isNotBlank() && !it.startsWith("127.") }
            .distinct()

        val executor = Executors.newFixedThreadPool(10)
        val futures = ips.map { ip ->
            executor.submit<Pair<String, String?>> {
                val name = cache.computeIfAbsent(ip) { resolveHostName(it) }
                ip to name
            }
        }

        val result = futures.mapNotNull {
            try {
                it.get(300, TimeUnit.MILLISECONDS)
            } catch (e: Exception) {
                null
            }
        }
        executor.shutdown()

        return result
    }

    private fun resolveHostName(ip: String): String? {
        return try {
            val address = InetAddress.getByName(ip)
            val host = address.hostName
            if (host == ip) null else host
        } catch (e: Exception) {
            null
        }
    }
}
