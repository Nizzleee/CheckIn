package com.example.checkin.screens.employee

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.checkin.model.AttendanceStatus
import com.example.checkin.viewmodel.AttendanceViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MyAttendance(viewModel: AttendanceViewModel) {
    val currentUser by viewModel.currentUser.collectAsState()
    val attendances by viewModel.attendances.collectAsState()
    val myAttendance = currentUser?.let { attendances[it.id] }
    val status = myAttendance?.status ?: AttendanceStatus.PENDIENTE
    val date = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("es")).format(Date())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Registrar Asistencia", fontWeight = FontWeight.Bold, fontSize = 22.sp)
        Text(date, color = Color.Gray, fontSize = 14.sp)

        Spacer(Modifier.height(32.dp))

        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(status.color).copy(alpha = 0.1f)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    when (status) {
                        AttendanceStatus.PRESENTE -> Icons.Default.CheckCircle
                        AttendanceStatus.AUSENTE -> Icons.Default.Cancel
                        AttendanceStatus.TARDANZA -> Icons.Default.Schedule
                        AttendanceStatus.PENDIENTE -> Icons.Default.HourglassEmpty
                    },
                    null,
                    tint = Color(status.color),
                    modifier = Modifier.size(64.dp)
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "Estado actual: ${status.label}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(status.color)
                )
                if (myAttendance?.timestamp?.isNotEmpty() == true) {
                    Text(
                        "Registrado a las ${myAttendance.timestamp}",
                        color = Color.Gray,
                        fontSize = 13.sp
                    )
                }
            }
        }

        Spacer(Modifier.height(32.dp))

        Text("Marcar mi asistencia", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        Spacer(Modifier.height(16.dp))

        AttendanceButton(
            title = "Presente",
            subtitle = "Llegué a tiempo",
            icon = Icons.Default.CheckCircle,
            color = Color(0xFF388E3C),
            isSelected = status == AttendanceStatus.PRESENTE,
            onClick = {
                currentUser?.let {
                    viewModel.markAttendance(it.id, AttendanceStatus.PRESENTE)
                }
            }
        )

        Spacer(Modifier.height(12.dp))

        AttendanceButton(
            title = "Tardanza",
            subtitle = "Llegué tarde",
            icon = Icons.Default.Schedule,
            color = Color(0xFFF57C00),
            isSelected = status == AttendanceStatus.TARDANZA,
            onClick = {
                currentUser?.let {
                    viewModel.markAttendance(it.id, AttendanceStatus.TARDANZA)
                }
            }
        )

        Spacer(Modifier.height(12.dp))

        AttendanceButton(
            title = "Ausente",
            subtitle = "No puedo asistir hoy",
            icon = Icons.Default.Cancel,
            color = Color(0xFFD32F2F),
            isSelected = status == AttendanceStatus.AUSENTE,
            onClick = {
                currentUser?.let {
                    viewModel.markAttendance(it.id, AttendanceStatus.AUSENTE)
                }
            }
        )
    }
}

@Composable
fun AttendanceButton(
    title: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) color.copy(alpha = 0.1f) else Color.White
        ),
        elevation = CardDefaults.cardElevation(if (isSelected) 0.dp else 3.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = color.copy(alpha = 0.1f),
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, null, tint = color, modifier = Modifier.size(28.dp))
                }
            }
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = color)
                Text(subtitle, color = Color.Gray, fontSize = 13.sp)
            }
            if (isSelected) {
                Icon(Icons.Default.CheckCircle, null, tint = color)
            }
        }
    }
}