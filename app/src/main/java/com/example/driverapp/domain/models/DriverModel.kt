package com.example.driverapp.domain.models

data class DriverModel(
    val driverId: String = "",
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val phone: String = "",
    val busId: String = "",
    val licenseNumber: String = "",
    val isActive: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)