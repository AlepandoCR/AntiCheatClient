package dev.alepando.scanner

object ProcessScanner {

    fun getRunningProcesses(): List<String> {
        val processes = mutableListOf<String>()
        try {
            val process = ProcessBuilder("cmd", "/c", "tasklist").start()
            val reader = process.inputStream.bufferedReader()
            reader.useLines { lines ->
                lines.drop(3).forEach {
                    val processName = it.takeWhile { ch -> ch != ' ' }
                    if (processName.isNotBlank()) {
                        processes.add(processName)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return processes
    }
}
