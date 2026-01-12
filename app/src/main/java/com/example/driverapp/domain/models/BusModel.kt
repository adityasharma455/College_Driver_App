package com.example.driverapp.domain.models

data class BusModel(
    val busId: String = "",
    val busNumber: String = "",
    val driverId: String = "",
    val driverName: String = "",
    val routeName: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null,
    val isActive: Boolean = false,
    val lastUpdated: Long = System.currentTimeMillis()
)
