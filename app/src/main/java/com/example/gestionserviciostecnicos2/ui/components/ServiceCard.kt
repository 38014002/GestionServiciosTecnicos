package com.example.gestionserviciostecnicos2.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.gestionserviciostecnicos2.data.Service

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceCard(service: Service, onClick: () -> Unit) {
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
            Text(text = "Fecha: ${service.entryDate}")
            Text(text = "Estado: ${service.status}")
        }
    }
}
