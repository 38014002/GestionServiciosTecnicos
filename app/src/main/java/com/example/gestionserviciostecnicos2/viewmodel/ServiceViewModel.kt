package com.example.gestionserviciostecnicos2.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.gestionserviciostecnicos2.data.AppDatabase
import com.example.gestionserviciostecnicos2.data.Service
import com.example.gestionserviciostecnicos2.data.ServiceOrder
import com.example.gestionserviciostecnicos2.data.User
import com.example.gestionserviciostecnicos2.repository.ServiceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Estado del formulario de servicio
data class ServiceFormState(
    val nombreCliente: String = "",
    val tipoDispositivo: String = "",
    val descripcionProblema: String = "",
    val uriImagen: Uri? = null,
    val errorNombreCliente: String? = null,
    val errorTipoDispositivo: String? = null,
    val errorDescripcionProblema: String? = null,
    val guardadoExitoso: Boolean = false
)

class ServiceViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ServiceRepository
    val allServices: Flow<List<Service>>

    // Lógica del formulario
    private val _formState = MutableStateFlow(ServiceFormState())
    val formState: StateFlow<ServiceFormState> = _formState.asStateFlow()

    init {
        val db = AppDatabase.getDatabase(application)
        repository = ServiceRepository(db.serviceDao(), db.serviceOrderDao(), db.userDao())
        allServices = repository.allServices
    }

    fun onClientNameChange(name: String) {
        _formState.update { it.copy(nombreCliente = name, errorNombreCliente = null) }
    }

    fun onDeviceTypeChange(type: String) {
        _formState.update { it.copy(tipoDispositivo = type, errorTipoDispositivo = null) }
    }

    fun onIssueDescriptionChange(description: String) {
        _formState.update { it.copy(descripcionProblema = description, errorDescripcionProblema = null) }
    }

    fun onImageUriChange(uri: Uri?) {
        _formState.update { it.copy(uriImagen = uri) }
    }

    fun saveService() {
        val state = _formState.value
        val hasError = state.nombreCliente.isBlank() || state.tipoDispositivo.isBlank() || state.descripcionProblema.isBlank()

        if (hasError) {
            _formState.update {
                it.copy(
                    errorNombreCliente = if (state.nombreCliente.isBlank()) "Ingrese Un Nombre" else null,
                    errorTipoDispositivo = if (state.tipoDispositivo.isBlank()) "Ingrese el Producto" else null,
                    errorDescripcionProblema = if (state.descripcionProblema.isBlank()) "Campo requerido" else null
                )
            }
            return
        }

        viewModelScope.launch {
            // Inserta un usuario de demostración si no existe
            val demoUser = repository.getUserById(1).firstOrNull()
            if (demoUser == null) {
                repository.insertUser(User(id = 1, name = "Demo User", email = "demo@example.com", role = "client"))
            }

            val newService = Service(
                clientName = state.nombreCliente,
                deviceType = state.tipoDispositivo,
                issueDescription = state.descripcionProblema,
                entryDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date()),
                status = "Pendiente",
                imageUri = state.uriImagen?.toString()
            )
            val serviceId = repository.insertService(newService)

            val newServiceOrder = ServiceOrder(
                clientId = 1, // Asigna el ID del usuario de demostración
                technicianId = null, // Sin técnico asignado inicialmente
                serviceId = serviceId.toInt(),
                orderDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date()),
                status = "Ingresado"
            )
            repository.insertServiceOrder(newServiceOrder)

            _formState.update { it.copy(guardadoExitoso = true) } // Indica que se guardó
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

    fun deleteServiceOrder(serviceOrder: ServiceOrder) = viewModelScope.launch {
        repository.deleteServiceOrder(serviceOrder)
    }

    fun getServiceById(serviceId: Int): Flow<Service> {
        return repository.getServiceById(serviceId)
    }

    fun getServiceOrderById(serviceOrderId: Int): Flow<ServiceOrder> {
        return repository.getServiceOrderById(serviceOrderId)
    }

    fun getUserById(userId: Int): Flow<User> {
        return repository.getUserById(userId)
    }

    fun insertUser(user: User) = viewModelScope.launch {
        repository.insertUser(user)
    }
}