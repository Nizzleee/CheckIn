package com.example.checkin.screens.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.checkin.model.UserModel
import com.example.checkin.screens.PrimaryBlue
import com.example.checkin.viewmodel.AttendanceViewModel

@Composable
fun EmployeeList(
    navController: NavHostController,
    viewModel: AttendanceViewModel
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val attendances by viewModel.attendances.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val filteredUsers = viewModel.getFilteredUsers()

    Column(modifier = Modifier.fillMaxSize()) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.setSearchQuery(it) },
            placeholder = { Text("Buscar empleado o departamento...") },
            leadingIcon = { Icon(Icons.Default.Search, null) },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { viewModel.setSearchQuery("") }) {
                        Icon(Icons.Default.Clear, null)
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryBlue
            )
        )

        Text(
            "${filteredUsers.size} empleados",
            modifier = Modifier.padding(horizontal = 16.dp),
            color = Color.Gray,
            fontSize = 13.sp
        )

        Spacer(Modifier.height(8.dp))

        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PrimaryBlue)
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredUsers, key = { it.id }) { user ->
                    EmployeeCard(
                        user = user,
                        attendance = attendances[user.id],
                        onMarkAttendance = { status ->
                            viewModel.markAttendance(user.id, status)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun EmployeeCard(
    user: UserModel,
    attendance: com.example.checkin.model.AttendanceModel?,
    onMarkAttendance: (AttendanceStatus) -> Unit
) {
    val status = attendance?.status ?: AttendanceStatus.PENDIENTE
    val statusColor = Color(status.color)
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(3.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = user.image,
                contentDescription = null,
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "${user.firstName} ${user.lastName}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Text(user.company.title, color = Color.Gray, fontSize = 13.sp)
                Text(user.company.department, color = PrimaryBlue, fontSize = 12.sp)
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
                Spacer(Modifier.height(4.dp))
                Box {
                    IconButton(
                        onClick = { showMenu = true },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            Icons.Default.MoreVert, null,
                            tint = Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        listOf(
                            AttendanceStatus.PRESENTE,
                            AttendanceStatus.AUSENTE,
                            AttendanceStatus.TARDANZA
                        ).forEach { s ->
                            DropdownMenuItem(
                                text = { Text(s.label, color = Color(s.color)) },
                                onClick = {
                                    onMarkAttendance(s)
                                    showMenu = false
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}