package com.example.driverapp.persentation.Screens.HomeScreens


import com.example.driverapp.domain.models.BusModel

sealed class HomeScreenState {

    object Idle : HomeScreenState()
    object Loading : HomeScreenState()

    data class BusLoaded(val bus: BusModel) : HomeScreenState()
    data class NoBusFound(val message: String = "No Bus Assigned") : HomeScreenState()

    data class Error(val message: String) : HomeScreenState()
}
