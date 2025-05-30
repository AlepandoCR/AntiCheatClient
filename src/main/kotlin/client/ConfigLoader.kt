package dev.alepando.client

import java.io.File
import java.util.*

object ConfigLoader {
    private val props = Properties()

    init {
        val file = File("config/client.properties") // momentáneo para guardar el número de aula
        if (!file.exists()) throw RuntimeException("Config file no encontrado en ${file.absolutePath}")
        props.load(file.inputStream())
    }

    val uid: String = props.getProperty("uid") ?: error("uid no configurado")
    val room: String = props.getProperty("room") ?: error("room no configurado")
}
