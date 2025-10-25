package com.example.gestionserviciostecnicos2.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestionserviciostecnicos2.data.AppDatabase
import com.example.gestionserviciostecnicos2.data.Service
import com.example.gestionserviciostecnicos2.data.ServiceOrder
import com.example.gestionserviciostecnicos2.repository.ServiceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Estado del formulario de servicio
data class ServiceFormState(
    val clientName: String = "",
    val deviceType: String = "",
    val issueDescription: String = "",
    val imageUri: Uri? = null,
    val clientNameError: String? = null,
    val deviceTypeError: String? = null,
    val issueDescriptionError: String? = null,
    val isSaved: Boolean = false
)

class ServiceViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ServiceRepository
    val allServices: Flow<List<Service>>

    // Lógica del formulario
    private val _formState = MutableStateFlow(ServiceFormState())
    val formState: StateFlow<ServiceFormState> = _formState.asStateFlow()

    init {
        val db = AppDatabase.getDatabase(application)
        repository = ServiceRepository(db.serviceDao(), db.serviceOrderDao())
        allServices = repository.allServices
    }

    fun onClientNameChange(name: String) {
        _formState.update { it.copy(clientName = name, clientNameError = null) }
    }

    fun onDeviceTypeChange(type: String) {
        _formState.update { it.copy(deviceType = type, deviceTypeError = null) }
    }

    fun onIssueDescriptionChange(description: String) {
        _formState.update { it.copy(issueDescription = description, issueDescriptionError = null) }
    }

    fun onImageUriChange(uri: Uri?) {
        _formState.update { it.copy(imageUri = uri) }
    }

    fun saveService() {
        val state = _formState.value
        val hasError = state.clientName.isBlank() || state.deviceType.isBlank() || state.issueDescription.isBlank()

        if (hasError) {
            _formState.update {
                it.copy(
                    clientNameError = if (state.clientName.isBlank()) "Ingrese Un Nombre" else null,
                    deviceTypeError = if (state.deviceType.isBlank()) "Ingrese el Producto" else null,
                    issueDescriptionError = if (state.issueDescription.isBlank()) "Campo requerido" else null
                )
            }
            return
        }

        viewModelScope.launch {
            val newService = Service(
                clientName = state.clientName,
                deviceType = state.deviceType,
                issueDescription = state.issueDescription,
                entryDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date()),
                status = "Pendiente",
                imageUri = state.imageUri?.toString()
            )
            repository.insertService(newService)
            _formState.update { it.copy(isSaved = true) } // Indica que se guardó
        }
    }

    fun resetFormState() {
        _formState.value = ServiceFormState()
    }

    val allServiceOrders: Flow<List<ServiceOrder>> = repository.allServiceOrders

    fun updateService(service: Service) = viewModelScope.launch {
        repository.updateService(service)
    }

    fun deleteService(service: Service) = viewModelScope.launch {
        repository.deleteService(service)
    }

    fun getServiceById(serviceId: Int): Flow<Service> {
        return repository.getServiceById(serviceId)
    }
}