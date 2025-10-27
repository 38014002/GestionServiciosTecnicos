package com.example.gestionserviciostecnicos2.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ServiceOrderDao {
    @Insert
    suspend fun insertServiceOrder(serviceOrder: ServiceOrder)

    @Delete
    suspend fun deleteServiceOrder(serviceOrder: ServiceOrder)

    @Query("SELECT * FROM service_orders WHERE clientId = :userId")
    fun getServiceOrdersForUser(userId: Int): Flow<List<ServiceOrder>>

    @Query("SELECT * FROM service_orders")
    fun getAllServiceOrders(): Flow<List<ServiceOrder>>

    @Query("SELECT * FROM service_orders WHERE id = :serviceOrderId")
    fun getServiceOrderById(serviceOrderId: Int): Flow<ServiceOrder>
}
