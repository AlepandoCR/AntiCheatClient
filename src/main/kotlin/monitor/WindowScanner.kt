package dev.alepando.monitor

import com.sun.jna.Native
import com.sun.jna.platform.win32.*
import com.sun.jna.ptr.IntByReference

object WindowScanner {

    fun getVisibleWindowsWithProcesses(): List<String> {
        val results = mutableListOf<String>()
        val user32 = User32.INSTANCE
        val kernel32 = Kernel32.INSTANCE
        val psapi = Psapi.INSTANCE

        user32.EnumWindows({ hWnd, _ ->
            if (!user32.IsWindowVisible(hWnd)) return@EnumWindows true

            val windowText = CharArray(1024)
            user32.GetWindowText(hWnd, windowText, 1024)
            val title = Native.toString(windowText)

            if (title.isBlank()) return@EnumWindows true

            val pidRef = IntByReference()
            user32.GetWindowThreadProcessId(hWnd, pidRef)
            val pid = pidRef.value

            val hProcess = kernel32.OpenProcess(
                WinNT.PROCESS_QUERY_INFORMATION or WinNT.PROCESS_VM_READ,
                false,
                pid
            ) ?: return@EnumWindows true

            val exeNameBytes = ByteArray(1024)
            val len = psapi.GetModuleFileNameExA(hProcess, null, exeNameBytes, exeNameBytes.size)
            val exeName = if (len > 0) {
                val fullPath = String(exeNameBytes, 0, len)
                fullPath.substringAfterLast('\\')
            } else {
                "unknown.exe"
            }

            kernel32.CloseHandle(hProcess)

            results.add("[APP] $exeName - $title")
            true
        }, null)

        return results
    }
}
