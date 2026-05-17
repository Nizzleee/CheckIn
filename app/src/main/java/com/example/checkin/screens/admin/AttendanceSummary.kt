package com.example.checkin.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.checkin.model.AttendanceStatus
import com.example.checkin.viewmodel.AttendanceViewModel

@Composable
fun AttendanceSummary(viewModel: AttendanceViewModel) {
    val users by viewModel.users.collectAsState()
    val attendances by viewModel.attendances.collectAsState()
    val stats = viewModel.getStats()
    val total = users.size.takeIf { it > 0 } ?: 1

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Reporte del día", fontWeight = FontWeight.Bold, fontSize = 22.sp)
            Spacer(Modifier.height(4.dp))
            Text("${users.size} empleados en total", color = Color.Gray, fontSize = 14.sp)
            Spacer(Modifier.height(16.dp))
        }

        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(3.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Distribución", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                    Spacer(Modifier.height(12.dp))
                    listOf(
                        AttendanceStatus.PRESENTE to Color(0xFF388E3C),
                        AttendanceStatus.AUSENTE to Color(0xFFD32F2F),
                        AttendanceStatus.TARDANZA to Color(0xFFF57C00),
                        AttendanceStatus.PENDIENTE to Color(0xFF9E9E9E)
                    ).forEach { (status, color) ->
                        val count = stats[status] ?: 0
                        val progress = count.toFloat() / total
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Text(
                                status.label,
                                modifier = Modifier.width(90.dp),
                                fontSize = 13.sp,
                                color = color,
                                fontWeight = FontWeight.Medium
                            )
                            LinearProgressIndicator(
                                progress = { progress },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = color,
                                trackColor = color.copy(alpha = 0.1f)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                count.toString(),
                                fontWeight = FontWeight.Bold,
                                color = color,
                                fontSize = 14.sp,
                                modifier = Modifier.width(24.dp)
                            )
                        }
                    }
                }
            }
        }

        item {
            Text("Detalle por empleado", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        }

        items(users, key = { it.id }) { user ->
            val attendance = attendances[user.id]
            val status = attendance?.status ?: AttendanceStatus.PENDIENTE
            val statusColor = Color(status.color)

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = user.image,
                        contentDescription = null,
                        modifier = Modifier.size(44.dp).clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "${user.firstName} ${user.lastName}",
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )
                        Text(user.company.department, color = Color.Gray, fontSize = 12.sp)
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = statusColor.copy(alpha = 0.1f)
                        ) {
                            Text(
                                status.label,
                                color = statusColor,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                        if (attendance?.timestamp?.isNotEmpty() == true) {
                            Spacer(Modifier.height(2.dp))
                            Text(attendance.timestamp, color = Color.Gray, fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }
}