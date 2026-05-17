package com.example.checkin.model

data class AttendanceModel(
    val userId: Int,
    val status: AttendanceStatus = AttendanceStatus.PENDIENTE,
    val timestamp: String = ""
)

enum class AttendanceStatus(val label: String, val color: Long) {
    PRESENTE("Presente", 0xFF388E3C),
    AUSENTE("Ausente", 0xFFD32F2F),
    TARDANZA("Tardanza", 0xFFF57C00),
    PENDIENTE("Pendiente", 0xFF9E9E9E)
}