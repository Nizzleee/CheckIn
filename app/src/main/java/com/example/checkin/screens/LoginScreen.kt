package com.example.checkin.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.checkin.viewmodel.AttendanceViewModel

@Composable
fun LoginScreen(
    viewModel: AttendanceViewModel,
    onAdminLogin: () -> Unit,
    onEmployeeLogin: (Int) -> Unit
) {
    var selectedEmployeeId by remember { mutableStateOf("") }
    var showEmployeeInput by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    val users by viewModel.users.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.AdminPanelSettings,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = PrimaryBlue
        )
        Spacer(Modifier.height(16.dp))
        Text("CheckIn", fontSize = 36.sp, fontWeight = FontWeight.Bold, color = PrimaryBlue)
        Text("Sistema de Asistencia", fontSize = 16.sp, color = Color.Gray)
        Spacer(Modifier.height(48.dp))

        RoleCard(
            title = "Administrador",
            description = "Gestiona la asistencia de todos los empleados",
            icon = Icons.Default.AdminPanelSettings,
            color = AdminColor,
            onClick = {
                viewModel.setRole("admin")
                onAdminLogin()
            }
        )

        Spacer(Modifier.height(16.dp))

        RoleCard(
            title = "Empleado",
            description = "Registra y consulta tu propia asistencia",
            icon = Icons.Default.Person,
            color = EmployeeColor,
            onClick = { showEmployeeInput = true }
        )

        if (showEmployeeInput) {
            Spacer(Modifier.height(24.dp))
            OutlinedTextField(
                value = selectedEmployeeId,
                onValueChange = {
                    selectedEmployeeId = it
                    errorMessage = ""
                },
                label = { Text("Ingresa tu ID de empleado (1-20)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )
            if (errorMessage.isNotEmpty()) {
                Text(errorMessage, color = Color.Red, fontSize = 12.sp)
            }
            Spacer(Modifier.height(12.dp))
            Button(
                onClick = {
                    val id = selectedEmployeeId.toIntOrNull()
                    if (id != null) {
                        val user = users.find { it.id == id }
                        if (user != null) {
                            viewModel.setRole("employee")
                            viewModel.setCurrentUser(user)
                            onEmployeeLogin(id)
                        } else {
                            errorMessage = "ID no encontrado. Ingresa un número del 1 al 20"
                        }
                    } else {
                        errorMessage = "Ingresa un número válido"
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = EmployeeColor),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Ingresar", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun RoleCard(
    title: String,
    description: String,
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
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = color.copy(alpha = 0.1f),
                modifier = Modifier.size(56.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, null, tint = color, modifier = Modifier.size(32.dp))
                }
            }
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(description, color = Color.Gray, fontSize = 13.sp)
            }
        }
    }
}