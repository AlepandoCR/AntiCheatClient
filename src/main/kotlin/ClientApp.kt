package dev.alepando

import dev.alepando.client.ConfigLoader
import dev.alepando.monitor.NetworkScanner
import dev.alepando.scanner.ProcessScanner
import network.ReportSender
import kotlin.concurrent.fixedRateTimer

fun main() {
    println("🟢 AntiCheat Estudiante iniciado...")
    fixedRateTimer(name = "report-timer", initialDelay = 1000, period = 15000) {
        val connections = NetworkScanner.getActiveConnections()
        val processes = ProcessScanner.getRunningProcesses()
        ReportSender.sendReport(ConfigLoader.uid, ConfigLoader.room, connections, processes)

    }
}