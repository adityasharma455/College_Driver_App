package com.example.driverapp.domain.useCases.BusSectionUseCase

import com.example.driverapp.domain.models.BusModel
import com.example.driverapp.domain.repo.Repo

class BusUseCase(private val repo: Repo) {

    suspend fun registerOrUpdateBus(bus: BusModel) = repo.registerOrUpdateBus(bus)

    suspend fun getBusByDriverId(driverId: String) = repo.getBusByDriverId(driverId)

    suspend fun updateBusLocation(busId: String, latitude: Double, longitude: Double) =
        repo.updateBusLocation(busId, latitude, longitude)

    suspend fun createBusUseCase(bus: BusModel) = repo.createBus(bus)

    suspend fun getBusForCurrentDriver() = repo.getBusForCurrentDriver()
}
