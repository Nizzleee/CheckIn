package com.example.checkin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.*
import com.example.checkin.network.UserApiService
import com.example.checkin.screens.LoginScreen
import com.example.checkin.screens.admin.*
import com.example.checkin.screens.employee.*
import com.example.checkin.ui.theme.CheckInTheme
import com.example.checkin.viewmodel.AttendanceViewModel
import com.example.checkin.viewmodel.AttendanceViewModelFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://dummyjson.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(UserApiService::class.java)
        val viewModel: AttendanceViewModel = ViewModelProvider(
            this, AttendanceViewModelFactory(service)
        ).get(AttendanceViewModel::class.java)

        setContent {
            CheckInTheme {
                CheckInApp(viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckInApp(viewModel: AttendanceViewModel) {
    val navController = rememberNavController()
    val currentRole by viewModel.currentRole.collectAsState()
    val currentDestination = navController
        .currentBackStackEntryAsState().value?.destination?.route

    val showBottomBar = currentDestination != "login"

    Scaffold(
        topBar = {
            if (showBottomBar) {
                TopAppBar(
                    title = {
                        Text(
                            "CheckIn",
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    navigationIcon = {
                        if (currentDestination != "adminDashboard" &&
                            currentDestination != "employeeDashboard"
                        ) {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    Icons.Default.ArrowBack,
                                    null,
                                    tint = Color.White
                                )
                            }
                        }
                    },
                    actions = {
                        IconButton(onClick = {
                            viewModel.setRole("")
                            navController.navigate("login") {
                                popUpTo(0)
                            }
                        }) {
                            Icon(Icons.Default.Logout, null, tint = Color.White)
                        }
                    }
                )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(containerColor = Color.White) {
                    if (currentRole == "admin") {
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.Dashboard, null) },
                            label = { Text("Dashboard") },
                            selected = currentDestination == "adminDashboard",
                            onClick = { navController.navigate("adminDashboard") }
                        )
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.People, null) },
                            label = { Text("Empleados") },
                            selected = currentDestination == "employeeList",
                            onClick = { navController.navigate("employeeList") }
                        )
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.BarChart, null) },
                            label = { Text("Reporte") },
                            selected = currentDestination == "attendanceSummary",
                            onClick = { navController.navigate("attendanceSummary") }
                        )
                    } else if (currentRole == "employee") {
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.Home, null) },
                            label = { Text("Inicio") },
                            selected = currentDestination == "employeeDashboard",
                            onClick = { navController.navigate("employeeDashboard") }
                        )
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.EditCalendar, null) },
                            label = { Text("Asistencia") },
                            selected = currentDestination == "myAttendance",
                            onClick = { navController.navigate("myAttendance") }
                        )
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.Person, null) },
                            label = { Text("Perfil") },
                            selected = currentDestination == "myProfile",
                            onClick = { navController.navigate("myProfile") }
                        )
                    }
                }
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = "login",
            modifier = Modifier.padding(padding)
        ) {
            composable("login") {
                LoginScreen(
                    viewModel = viewModel,
                    onAdminLogin = { navController.navigate("adminDashboard") },
                    onEmployeeLogin = { navController.navigate("employeeDashboard") }
                )
            }
            composable("adminDashboard") {
                AdminDashboard(navController, viewModel)
            }
            composable("employeeList") {
                EmployeeList(navController, viewModel)
            }
            composable("attendanceSummary") {
                AttendanceSummary(viewModel)
            }
            composable("employeeDashboard") {
                EmployeeDashboard(navController, viewModel)
            }
            composable("myAttendance") {
                MyAttendance(viewModel)
            }
            composable("myProfile") {
                MyProfile(viewModel)
            }
        }
    }
}