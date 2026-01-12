package com.example.driverapp.persentation.Screens.ProfileScreens

import com.example.driverapp.domain.models.DriverModel

sealed class ProfileScreenState {
    object Loading : ProfileScreenState()
    data class Success(val driver: DriverModel) : ProfileScreenState()
    data class Error(val message: String) : ProfileScreenState()
}
