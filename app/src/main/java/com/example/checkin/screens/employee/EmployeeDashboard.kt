package com.example.checkin.screens.employee

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.checkin.model.AttendanceStatus
import com.example.checkin.screens.EmployeeColor
import com.example.checkin.screens.PrimaryBlue
import com.example.checkin.viewmodel.AttendanceViewModel

@Composable
fun EmployeeDashboard(
    navController: NavHostController,
    viewModel: AttendanceViewModel
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val attendances by viewModel.attendances.collectAsState()
    val myAttendance = currentUser?.let { attendances[it.id] }
    val status = myAttendance?.status ?: AttendanceStatus.PENDIENTE
    val statusColor = Color(status.color)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Text("Mi Panel", fontWeight = FontWeight.Bold, fontSize = 24.sp)
        Text("Bienvenido de vuelta", color = Color.Gray, fontSize = 14.sp)

        Spacer(Modifier.height(24.dp))

        // Tarjeta de perfil
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = PrimaryBlue),
            elevation = CardDefaults.cardElevation(6.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = currentUser?.image,
                    contentDescription = null,
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.width(16.dp))
                Column {
                    Text(
                        "${currentUser?.firstName} ${currentUser?.lastName}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.White
                    )
                    Text(
                        currentUser?.company?.title ?: "",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 14.sp
                    )
                    Text(
                        currentUser?.company?.department ?: "",
                        color = Color.White.copy(alpha = 0.6f),
                        fontSize = 13.sp
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Text("Mi asistencia hoy", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        Spacer(Modifier.height(12.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = statusColor.copy(alpha = 0.1f)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    when (status) {
                        AttendanceStatus.PRESENTE -> Icons.Default.CheckCircle
                        AttendanceStatus.AUSENTE -> Icons.Default.Cancel
                        AttendanceStatus.TARDANZA -> Icons.Default.Schedule
                        AttendanceStatus.PENDIENTE -> Icons.Default.HourglassEmpty
                    },
                    null,
                    tint = statusColor,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(Modifier.width(16.dp))
                Column {
                    Text(
                        status.label,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = statusColor
                    )
                    if (myAttendance?.timestamp?.isNotEmpty() == true) {
                        Text(
                            "Registrado a las ${myAttendance.timestamp}",
                            color = Color.Gray,
                            fontSize = 13.sp
                        )
                    } else {
                        Text("Aún no registrado", color = Color.Gray, fontSize = 13.sp)
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Text("Acciones", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        Spacer(Modifier.height(12.dp))

        Card(
            onClick = { navController.navigate("myAttendance") },
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
                    color = EmployeeColor.copy(alpha = 0.1f),
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Default.EditCalendar, null,
                            tint = EmployeeColor,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
                Spacer(Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("Registrar asistencia", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text("Marca tu entrada del día", color = Color.Gray, fontSize = 13.sp)
                }
                Icon(Icons.Default.ChevronRight, null, tint = Color.Gray)
            }
        }

        Spacer(Modifier.height(12.dp))

        Card(
            onClick = { navController.navigate("myProfile") },
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
                    color = PrimaryBlue.copy(alpha = 0.1f),
                    modifier = Modifier.size(48.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Default.Person, null,
                            tint = PrimaryBlue,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
                Spacer(Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("Mi perfil", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Text("Ver mi información personal", color = Color.Gray, fontSize = 13.sp)
                }
                Icon(Icons.Default.ChevronRight, null, tint = Color.Gray)
            }
        }
    }
}