package com.example.gestionserviciostecnicos2.repository

import com.example.gestionserviciostecnicos2.data.Service
import com.example.gestionserviciostecnicos2.data.ServiceDao
import com.example.gestionserviciostecnicos2.data.ServiceOrder
import com.example.gestionserviciostecnicos2.data.ServiceOrderDao
import kotlinx.coroutines.flow.Flow

class ServiceRepository(private val serviceDao: ServiceDao, private val serviceOrderDao: ServiceOrderDao) {

    val allServices: Flow<List<Service>> = serviceDao.getAllServices()
    val allServiceOrders: Flow<List<ServiceOrder>> = serviceOrderDao.getAllServiceOrders()

    suspend fun insertService(service: Service) {
        serviceDao.insertService(service)
    }

    suspend fun updateService(service: Service) {
        serviceDao.updateService(service)
    }

    suspend fun deleteService(service: Service) {
        serviceDao.deleteService(service)
    }

    suspend fun insertServiceOrder(serviceOrder: ServiceOrder) {
        serviceOrderDao.insertServiceOrder(serviceOrder)
    }

    fun getServiceById(serviceId: Int): Flow<Service> {
        return serviceDao.getServiceById(serviceId)
    }

    fun getServiceOrderById(serviceOrderId: Int): Flow<ServiceOrder> {
        return serviceOrderDao.getServiceOrderById(serviceOrderId)
    }

    fun getServiceOrdersForUser(userId: Int): Flow<List<ServiceOrder>> {
        return serviceOrderDao.getServiceOrdersForUser(userId)
    }
}
