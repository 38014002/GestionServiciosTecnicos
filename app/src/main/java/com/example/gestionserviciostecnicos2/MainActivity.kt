package com.example.gestionserviciostecnicos2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gestionserviciostecnicos2.ui.screens.OrderMonitorScreen
import com.example.gestionserviciostecnicos2.ui.screens.ScheduleServiceScreen
import com.example.gestionserviciostecnicos2.ui.screens.ServiceDetailScreen
import com.example.gestionserviciostecnicos2.ui.theme.GestionServiciosTecnicos2Theme
import com.example.gestionserviciostecnicos2.viewmodel.ServiceViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GestionServiciosTecnicos2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigator()
                }
            }
        }
    }
}

@Composable
fun AppNavigator() {
    val navController = rememberNavController()
    val serviceViewModel: ServiceViewModel = viewModel()
    NavHost(navController = navController, startDestination = "orderMonitor") {
        composable("orderMonitor") {
            OrderMonitorScreen(
                serviceViewModel = serviceViewModel,
                onScheduleServiceClick = { navController.navigate("scheduleService") },
                onServiceOrderClick = { serviceId -> navController.navigate("serviceDetail/$serviceId") }
            )
        }
        composable("scheduleService") {
            ScheduleServiceScreen(
                serviceViewModel = serviceViewModel,
                onServiceScheduled = { navController.popBackStack() })
        }
        composable(
            route = "serviceDetail/{serviceId}",
            arguments = listOf(navArgument("serviceId") { type = NavType.IntType })
        ) {
            val serviceId = it.arguments?.getInt("serviceId") ?: 0
            ServiceDetailScreen(
                serviceId = serviceId,
                serviceViewModel = serviceViewModel,
                onServiceDeleted = { navController.popBackStack() }
            )
        }
    }
}
