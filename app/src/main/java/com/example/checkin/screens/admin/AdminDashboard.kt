package com.example.checkin.screens.admin

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
import androidx.navigation.NavHostController
import com.example.checkin.model.AttendanceStatus
import com.example.checkin.screens.PrimaryBlue
import com.example.checkin.viewmodel.AttendanceViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AdminDashboard(
    navController: NavHostController,
    viewModel: AttendanceViewModel
) {
    val stats = viewModel.getStats()
    val date = SimpleDateFormat("EEEE, dd MMMM yyyy", Locale("es")).format(Date())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text("Panel Administrador", fontWeight = FontWeight.Bold, fontSize = 24.sp)
        Text(date, color = Color.Gray, fontSize = 14.sp)

        Spacer(Modifier.height(24.dp))

        Text("Resumen del día", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                modifier = Modifier.weight(1f),
                title = "Presentes",
                value = stats[AttendanceStatus.PRESENTE] ?: 0,
                color = Color(0xFF388E3C),
                icon = Icons.Default.CheckCircle
            )
            StatCard(
                modifier = Modifier.weight(1f),
                title = "Ausentes",
                value = stats[AttendanceStatus.AUSENTE] ?: 0,
                color = Color(0xFFD32F2F),
                icon = Icons.Default.Cancel
            )
        }

        Spacer(Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            StatCard(
                modifier = Modifier.weight(1f),
                title = "Tardanzas",
                value = stats[AttendanceStatus.TARDANZA] ?: 0,
                color = Color(0xFFF57C00),
                icon = Icons.Default.Schedule
            )
            StatCard(
                modifier = Modifier.weight(1f),
                title = "Pendientes",
                value = stats[AttendanceStatus.PENDIENTE] ?: 0,
                color = Color(0xFF9E9E9E),
                icon = Icons.Default.HourglassEmpty
            )
        }

        Spacer(Modifier.height(32.dp))

        Text("Acciones", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        Spacer(Modifier.height(12.dp))

        ActionButton(
            title = "Ver lista de empleados",
            subtitle = "Marcar asistencia individual",
            icon = Icons.Default.People,
            color = PrimaryBlue,
            onClick = { navController.navigate("employeeList") }
        )

        Spacer(Modifier.height(12.dp))

        ActionButton(
            title = "Resumen de asistencia",
            subtitle = "Ver reporte completo del día",
            icon = Icons.Default.BarChart,
            color = Color(0xFF7B1FA2),
            onClick = { navController.navigate("attendanceSummary") }
        )
    }
}

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    title: String,
    value: Int,
    color: Color,
    icon: ImageVector
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f)),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, null, tint = color, modifier = Modifier.size(32.dp))
            Spacer(Modifier.height(8.dp))
            Text(value.toString(), fontWeight = FontWeight.Bold, fontSize = 28.sp, color = color)
            Text(title, fontSize = 13.sp, color = Color.Gray)
        }
    }
}

@Composable
fun ActionButton(
    title: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
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
                Text(title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(subtitle, color = Color.Gray, fontSize = 13.sp)
            }
            Icon(Icons.Default.ChevronRight, null, tint = Color.Gray)
        }
    }
}