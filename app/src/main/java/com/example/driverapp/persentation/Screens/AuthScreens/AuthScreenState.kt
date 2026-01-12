package com.example.driverapp.persentation.Screens.AuthScreens

import com.example.driverapp.domain.models.DriverModel

sealed class AuthScreenState {

    object  Idle: AuthScreenState()
    object Loading: AuthScreenState()
    data class RegistrationSuccess(val Success: Boolean) : AuthScreenState()
    data class LogInSuccess(val Success: DriverModel): AuthScreenState()

    object signOut: AuthScreenState()

    data class Error(val message: String): AuthScreenState()
}