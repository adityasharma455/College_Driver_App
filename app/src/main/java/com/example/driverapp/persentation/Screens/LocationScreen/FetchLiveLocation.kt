package com.example.driverapp.persentation.Screens.LocationScreen

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationServices
import android.location.LocationManager
import android.content.Intent
import android.provider.Settings
import kotlinx.coroutines.tasks.await


fun isLocationEnabled(context: Context): Boolean {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
    return locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) ?: false ||
            locationManager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ?: false
}

fun openLocationSettings(context: Context) {
    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
    context.startActivity(intent)
}

@SuppressLint("MissingPermission")
suspend fun getLocationSuspend(context: Context): Pair<Double, Double>? {
    return try {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        // First try last location
        val last = fusedLocationClient.lastLocation.await()
        if (last != null) return Pair(last.latitude, last.longitude)

        // Try fresh GPS location
        val fresh = fusedLocationClient.getCurrentLocation(
            com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY,
            null
        ).await()

        if (fresh != null) Pair(fresh.latitude, fresh.longitude)
        else null

    } catch (e: Exception) {
        null
    }
}

