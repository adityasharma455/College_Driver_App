package com.example.driverapp.persentation.Screens.HomeScreens

sealed class LocationUpdateState {
    object Idle : LocationUpdateState()
    object Updating : LocationUpdateState()
    object Success : LocationUpdateState()
    data class Error(val message: String) : LocationUpdateState()
}