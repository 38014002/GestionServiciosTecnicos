package com.example.gestionserviciostecnicos2.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.gestionserviciostecnicos2.ui.components.ServiceCard
import com.example.gestionserviciostecnicos2.viewmodel.ServiceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderMonitorScreen(
    serviceViewModel: ServiceViewModel,
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
                itemsIndexed(filteredServices) { index, service ->
                    AnimatedVisibility(
                        visible = true,
                        enter = fadeIn(animationSpec = tween(durationMillis = 500, delayMillis = index * 100)) +
                                slideInVertically(
                                    initialOffsetY = { it / 2 }, 
                                    animationSpec = tween(durationMillis = 500, delayMillis = index * 100)
                                ),
                    ) {
                         ServiceCard(service = service, onClick = { onServiceOrderClick(service.id) })
                    }
                }
            }
        }
    }
}
