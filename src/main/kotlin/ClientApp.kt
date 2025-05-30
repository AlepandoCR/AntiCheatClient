package dev.alepando

import dev.alepando.client.ConfigLoader
import dev.alepando.monitor.WindowScanner
import dev.alepando.scanner.ProcessScanner
import network.ReportSender
import kotlin.concurrent.fixedRateTimer

fun main() {
    println("🟢 AntiCheat Estudiante iniciado...")

    fixedRateTimer(name = "report-timer", initialDelay = 1000, period = 15000) {
        val processes = ProcessScanner.getRunningProcesses()
        val windows = WindowScanner.getVisibleWindowsWithProcesses() // mix de apps y hosts

        val allConnections = windows
        ReportSender.sendReport(ConfigLoader.uid, ConfigLoader.room, allConnections, processes)
    }
}
