package com.example.gestionserviciostecnicos2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gestionserviciostecnicos2.viewmodel.ServiceViewModel

@Composable
fun ServiceDetailScreen(
    serviceId: Int,
    serviceViewModel: ServiceViewModel = viewModel(),
    onServiceDeleted: () -> Unit
) {
    val service by serviceViewModel.getServiceById(serviceId).collectAsState(initial = null)
    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        service?.let { currentService ->
            Text(text = "Nombre del Cliente: ${currentService.clientName}")
            Text(text = "Tipo de Dispositivo: ${currentService.deviceType}")
            Text(text = "Descripción del Problema: ${currentService.issueDescription}")
            Text(text = "Fecha de Ingreso: ${currentService.entryDate}")
            Text(text = "Estado: ${currentService.status}", style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.height(32.dp))

            // Botones para cambiar el estado
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                if (currentService.status == "Pendiente") {
                    Button(onClick = { 
                        val updatedService = currentService.copy(status = "En Proceso")
                        serviceViewModel.updateService(updatedService)
                    }) {
                        Text("Iniciar Proceso")
                    }
                }
                if (currentService.status == "En Proceso") {
                    Button(onClick = { 
                        val updatedService = currentService.copy(status = "Finalizado")
                        serviceViewModel.updateService(updatedService)
                     }) {
                        Text("Finalizar Servicio")
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f)) // Empuja el botón de eliminar hacia abajo

            Button(
                onClick = { showDialog = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
            ) {
                Text("Eliminar Solicitud")
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirmar Eliminación") },
            text = { Text("¿Estás seguro de que deseas eliminar esta solicitud de servicio? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        service?.let { serviceViewModel.deleteService(it) }
                        showDialog = false
                        onServiceDeleted()
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog = false }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}
