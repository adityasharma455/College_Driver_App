package com.example.driverapp.domain.useCases.AuthSectionUseCase


import com.example.driverapp.domain.models.DriverModel
import com.example.driverapp.domain.repo.Repo

class DriverAuthUseCase(private val repo: Repo) {

    suspend fun registerDriver(driver: DriverModel) =
        repo.registerDriver(driver)

    suspend fun loginDriver(email: String, password: String) =
        repo.loginDriver(email, password)

    suspend fun getCurrentDriver() =
        repo.getCurrentDriver()

    suspend fun logoutDriver() =
        repo.logoutDriver()

    suspend fun updateDriverInfo(driverId: String, updatedDriver: DriverModel) =
        repo.updateDriverInfo(driverId, updatedDriver)
}
