package dev.alepando

import activity.ScreenshotTaker
import dev.alepando.client.ConfigLoader
import dev.alepando.monitor.NetworkScanner
import dev.alepando.monitor.WindowScanner
import dev.alepando.scanner.ProcessScanner
import network.ReportSender
import kotlin.concurrent.fixedRateTimer

fun main() {
    println("🟢 AntiCheat Estudiante iniciado...")

    fixedRateTimer(name = "report-timer", initialDelay = 1000, period = 15000) {
        val screenshots = mutableListOf<ByteArray>()

        repeat(3) { index ->
            val screenshot = ScreenshotTaker.captureScreenshot()
            screenshots.add(screenshot)
            if (index < 2) Thread.sleep(5000)
        }

        val processes = ProcessScanner.getRunningProcesses()
        val windows = WindowScanner.getVisibleWindowsWithProcesses()

        val namedConnections = NetworkScanner.getActiveConnectionsWithNames()
        val connections = namedConnections.map { (ip, name) ->
            val label = if (name != null && name != ip) "$ip ($name)" else ip
            "[WEB] $label"
        }

        val allConnections = windows + connections

        ReportSender.sendReportWithScreenshots(
            uid = ConfigLoader.uid,
            room = ConfigLoader.room,
            activeConnections = allConnections,
            activeApps = processes,
            screenshots = screenshots
        )
    }
}

