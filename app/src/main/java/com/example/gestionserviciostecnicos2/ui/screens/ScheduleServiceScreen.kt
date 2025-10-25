package com.example.gestionserviciostecnicos2.ui.screens

import android.Manifest
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.gestionserviciostecnicos2.ui.components.CustomTextField
import com.example.gestionserviciostecnicos2.viewmodel.ServiceViewModel
import androidx.core.content.FileProvider
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleServiceScreen(
    serviceViewModel: ServiceViewModel,
    onServiceScheduled: () -> Unit
) {
    val formState by serviceViewModel.formState.collectAsState()
    val context = LocalContext.current

    // --- Lógica para seleccionar imágenes ---

    // Launcher para la galería
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            uri?.let { serviceViewModel.onImageUriChange(it) }
        }
    )

    // Uris y launchers para la cámara
    val file = File(context.cacheDir, "camera_photo.jpg")
    val imageUri = FileProvider.getUriForFile(context, "${context.packageName}.provider", file)

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                serviceViewModel.onImageUriChange(imageUri)
            }
        }
    )

    // Launcher para el permiso de la cámara
    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                cameraLauncher.launch(imageUri)
            } else {
                Toast.makeText(context, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
            }
        }
    )

    // --- Efectos y UI ---

    LaunchedEffect(formState.guardadoExitoso) {
        if (formState.guardadoExitoso) {
            onServiceScheduled()
            serviceViewModel.resetFormState()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = "Agendar un Nuevo Servicio", style = MaterialTheme.typography.headlineMedium)

        CustomTextField(
            value = formState.nombreCliente,
            onValueChange = { serviceViewModel.onClientNameChange(it) },
            label = "Nombre del Cliente",
            isError = formState.errorNombreCliente != null,
            errorText = formState.errorNombreCliente
        )

        CustomTextField(
            value = formState.tipoDispositivo,
            onValueChange = { serviceViewModel.onDeviceTypeChange(it) },
            label = "Tipo de Dispositivo",
            isError = formState.errorTipoDispositivo != null,
            errorText = formState.errorTipoDispositivo
        )

        CustomTextField(
            value = formState.descripcionProblema,
            onValueChange = { serviceViewModel.onIssueDescriptionChange(it) },
            label = "Descripción del Problema",
            isError = formState.errorDescripcionProblema != null,
            errorText = formState.errorDescripcionProblema,
            maxLines = 5
        )

        // Botones para adjuntar foto
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = { 
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }) {
                Text("Tomar Foto")
            }
            Button(onClick = { galleryLauncher.launch("image/*") }) {
                Text("Desde Galería")
            }
        }

        // Vista previa de la imagen
        formState.uriImagen?.let {
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = "Vista previa de la imagen",
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.CenterHorizontally),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { serviceViewModel.saveService() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Agendar Servicio")
        }
    }
}
