package com.example.driverapp.persentation.navigation

import kotlinx.serialization.Serializable

sealed class SubNavigation{

    @Serializable
    object AuthScreenRoutes : SubNavigation()

    @Serializable
    object HomeScreenRoutes: SubNavigation()

    @Serializable
    object BusCreationRoutes: SubNavigation()

    @Serializable
    object DriverProfileRoutes: SubNavigation()
}

sealed class Routes{

    @Serializable
    object DriverSignUpScreenRoutes

    @Serializable
    object DriverLogInScreenRoutes

    @Serializable
    object HomeScreenRoutes

    @Serializable
    object DriverProfileScreenRoutes

    @Serializable
    object createBusScreenRoutes
}