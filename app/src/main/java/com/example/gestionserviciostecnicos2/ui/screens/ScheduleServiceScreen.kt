package com.example.gestionserviciostecnicos2.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.gestionserviciostecnicos2.viewmodel.ServiceViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleServiceScreen(
    serviceViewModel: ServiceViewModel, 
    onServiceScheduled: () -> Unit
) {
    val formState by serviceViewModel.formState.collectAsState()

    // Navega hacia atrás cuando el estado isSaved cambia a true
    LaunchedEffect(formState.isSaved) {
        if (formState.isSaved) {
            onServiceScheduled()
            serviceViewModel.resetFormState() // Resetea el estado para la próxima vez
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text(text = "Agendar un Nuevo Servicio", style = MaterialTheme.typography.headlineMedium)
        
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = formState.clientName,
            onValueChange = { serviceViewModel.onClientNameChange(it) },
            label = { Text("Nombre del Cliente") },
            modifier = Modifier.fillMaxWidth(),
            isError = formState.clientNameError != null,
            singleLine = true
        )
        formState.clientNameError?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }

        OutlinedTextField(
            value = formState.deviceType,
            onValueChange = { serviceViewModel.onDeviceTypeChange(it) },
            label = { Text("Tipo de Dispositivo") },
            modifier = Modifier.fillMaxWidth(),
            isError = formState.deviceTypeError != null,
            singleLine = true
        )
        formState.deviceTypeError?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }

        OutlinedTextField(
            value = formState.issueDescription,
            onValueChange = { serviceViewModel.onIssueDescriptionChange(it) },
            label = { Text("Descripción del Problema") },
            modifier = Modifier.fillMaxWidth(),
            isError = formState.issueDescriptionError != null,
            maxLines = 5
        )
        formState.issueDescriptionError?.let {
            Text(text = it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
        }
        
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { serviceViewModel.saveService() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Agendar Servicio")
        }
    }
}
