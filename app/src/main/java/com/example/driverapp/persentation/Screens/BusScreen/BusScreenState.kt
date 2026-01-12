package com.example.driverapp.persentation.Screens.BusScreen

import com.example.driverapp.domain.models.BusModel


sealed class BusScreenState {

    object Idle : BusScreenState()
    object Loading : BusScreenState()

    data class BusCreated(val success: Boolean) : BusScreenState()

    data class BusFetched(val bus: BusModel) : BusScreenState()

    data class LocationUpdated(val success: Boolean) : BusScreenState()

    data class Error(val message: String) : BusScreenState()
}