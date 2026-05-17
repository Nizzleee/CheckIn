package com.example.checkin.screens.employee

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.checkin.screens.PrimaryBlue
import com.example.checkin.viewmodel.AttendanceViewModel

@Composable
fun MyProfile(viewModel: AttendanceViewModel) {
    val currentUser by viewModel.currentUser.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = currentUser?.image,
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(Modifier.height(12.dp))
        Text(
            "${currentUser?.firstName} ${currentUser?.lastName}",
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        )
        Text(currentUser?.company?.title ?: "", color = Color.Gray, fontSize = 15.sp)

        Spacer(Modifier.height(24.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(3.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Información Personal", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(Modifier.height(12.dp))
                ProfileInfoRow(Icons.Default.Badge, "ID", currentUser?.id.toString())
                ProfileInfoRow(Icons.Default.Email, "Email", currentUser?.email ?: "")
                ProfileInfoRow(Icons.Default.Phone, "Teléfono", currentUser?.phone ?: "")
                ProfileInfoRow(Icons.Default.Cake, "Edad", "${currentUser?.age} años")
            }
        }

        Spacer(Modifier.height(16.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(3.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Información Laboral", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(Modifier.height(12.dp))
                ProfileInfoRow(Icons.Default.Business, "Empresa", currentUser?.company?.name ?: "")
                ProfileInfoRow(Icons.Default.Work, "Cargo", currentUser?.company?.title ?: "")
                ProfileInfoRow(Icons.Default.Groups, "Departamento", currentUser?.company?.department ?: "")
            }
        }
    }
}

@Composable
fun ProfileInfoRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = PrimaryBlue, modifier = Modifier.size(20.dp))
        Spacer(Modifier.width(12.dp))
        Column {
            Text(label, color = Color.Gray, fontSize = 12.sp)
            Text(value, fontWeight = FontWeight.Medium, fontSize = 14.sp)
        }
    }
    HorizontalDivider(color = Color.Gray.copy(alpha = 0.1f))
}