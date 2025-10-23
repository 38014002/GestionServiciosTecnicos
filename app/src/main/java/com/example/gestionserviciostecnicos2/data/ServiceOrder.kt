package com.example.gestionserviciostecnicos2.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "service_orders",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["clientId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["technicianId"],
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class ServiceOrder(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val clientId: Int, // Foreign key to User
    val technicianId: Int?, // Foreign key to User (can be null)
    val serviceId: Int, // Foreign key to Service
    val orderDate: String,
    val status: String // e.g., "pending", "in_progress", "completed"
)
