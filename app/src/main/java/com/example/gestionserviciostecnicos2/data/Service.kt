package com.example.gestionserviciostecnicos2.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "services")
data class Service(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val clientName: String,
    val deviceType: String,
    val issueDescription: String,
    val entryDate: String,
    val status: String
)
