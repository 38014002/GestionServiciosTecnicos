package com.example.gestionserviciostecnicos2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.gestionserviciostecnicos2.data.Service
import com.example.gestionserviciostecnicos2.viewmodel.ServiceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderMonitorScreen(
    serviceViewModel: ServiceViewModel, // <-- ¡CORREGIDO!
    onScheduleServiceClick: () -> Unit,
    onServiceOrderClick: (Int) -> Unit
) {
    val services by serviceViewModel.allServices.collectAsState(initial = emptyList())
    var searchQuery by remember { mutableStateOf("") }

    val filteredServices = if (searchQuery.isEmpty()) {
        services
    } else {
        services.filter { 
            it.clientName.contains(searchQuery, ignoreCase = true) || 
            it.deviceType.contains(searchQuery, ignoreCase = true)
        }
    }

    Scaffold(
        topBar = {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                label = { Text("Buscar por cliente o equipo") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
                singleLine = true
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onScheduleServiceClick) {
                Icon(Icons.Default.Add, contentDescription = "Agendar Servicio")
            }
        }
    ) { paddingValues ->
        if (filteredServices.isEmpty() && services.isNotEmpty()) {
             Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("No se encontraron resultados", style = MaterialTheme.typography.bodyLarge)
            }
        } else if (services.isEmpty()) {
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(imageVector = Icons.Default.Inbox, contentDescription = "Bandeja vacía", modifier = Modifier.size(64.dp))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No hay servicios agendados.\n¡Toca el botón + para añadir el primero!",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                items(filteredServices) { service ->
                    ServiceItem(service = service, onClick = { onServiceOrderClick(service.id) })
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceItem(service: Service, onClick: () -> Unit) {
    val cardColor = when (service.status) {
        "Pendiente" -> Color(0xFFFFF9C4) // Amarillo claro
        "En Proceso" -> Color(0xFFBBDEFB) // Azul claro
        "Finalizado" -> Color(0xFFC8E6C9) // Verde claro
        else -> MaterialTheme.colorScheme.surfaceVariant
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = cardColor
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Cliente: ${service.clientName}")
            Text(text = "Equipo: ${service.deviceType}")
            Text(text = "Estado: ${service.status}")
        }
    }
}
