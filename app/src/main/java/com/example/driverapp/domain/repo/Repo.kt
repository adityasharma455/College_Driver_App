package com.example.driverapp.domain.repo

import com.example.driverapp.Common.ResultState
import com.example.driverapp.domain.models.BusModel
import com.example.driverapp.domain.models.DriverModel
import kotlinx.coroutines.flow.Flow

interface Repo {
     fun registerOrUpdateBus(bus: BusModel): Flow<ResultState<Boolean>>
     fun getBusByDriverId(driverId: String): Flow<ResultState<BusModel>>

    fun getBusForCurrentDriver(): Flow<ResultState<BusModel>>
     fun updateBusLocation(busId: String, latitude: Double, longitude: Double): Flow<ResultState<Boolean>>

      fun registerDriver(driver: DriverModel): Flow<ResultState<Boolean>>

      fun loginDriver(email: String, password: String): Flow<ResultState<DriverModel>>

      fun getCurrentDriver(): Flow<ResultState<DriverModel>>

      fun logoutDriver(): Flow<ResultState<Boolean>>

      fun updateDriverInfo(driverId: String, updatedDriver: DriverModel): Flow<ResultState<Boolean>>

      fun createBus(bus: BusModel): Flow<ResultState<Boolean>>


}