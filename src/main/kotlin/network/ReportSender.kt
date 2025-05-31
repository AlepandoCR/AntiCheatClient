package network

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import dev.alepando.model.ConnectionReport
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.net.InetAddress
import java.time.Instant

object ReportSender {
    private val client = OkHttpClient()
    private val mapper = jacksonObjectMapper()
    private const val SERVER_URL = "http://localhost:8080/api/report"

    fun sendReport(uid: String, room: String, activeConnections: List<String>, activeApps: List<String>) {
        val report = ConnectionReport(
            uid = uid,
            room = room,
            hostname = InetAddress.getLocalHost().hostName,
            user = System.getProperty("user.name"),
            activeConnections = activeConnections,
            timestamp = Instant.now().epochSecond,
            activeApps = activeApps
        )

        val json = mapper.writeValueAsString(report)
        val requestBody = json.toRequestBody("application/json".toMediaType())

        val request = Request.Builder()
            .url(SERVER_URL)
            .post(requestBody)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                println("Error al enviar: ${response.code}")
            } else {
                println("Reporte enviado exitosamente")
            }
        }
    }

    fun sendReportWithScreenshots(
        uid: String,
        room: String,
        activeConnections: List<String>,
        activeApps: List<String>,
        screenshots: List<ByteArray>
    ) {
        try {
            val report = ConnectionReport(
                uid = uid,
                room = room,
                hostname = InetAddress.getLocalHost().hostName,
                user = System.getProperty("user.name"),
                activeConnections = activeConnections,
                timestamp = Instant.now().epochSecond,
                activeApps = activeApps
            )

            val jsonPart = mapper.writeValueAsString(report)

            val requestBodyBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("report", "report.json",
                    jsonPart.toRequestBody("application/json".toMediaType())
                )

            screenshots.forEachIndexed { index, bytes ->
                requestBodyBuilder.addFormDataPart(
                    "screenshot$index", "screenshot$index.png",
                    bytes.toRequestBody("image/png".toMediaType())
                )
            }

            val request = Request.Builder()
                .url(SERVER_URL)
                .post(requestBodyBuilder.build())
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) {
                    println("❌ Error al enviar reporte: ${response.code}")
                } else {
                    println("✅ Reporte enviado con éxito.")
                }
            }
        } catch (e: Exception) {
            println("❗ No se pudo enviar el reporte: ${e.message}")
        }
    }
}
