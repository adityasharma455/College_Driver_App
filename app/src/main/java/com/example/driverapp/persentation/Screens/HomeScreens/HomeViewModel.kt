package com.example.driverapp.persentation.Screens.HomeScreens

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.driverapp.Common.ResultState
import com.example.driverapp.domain.useCases.BusSectionUseCase.BusUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val busUseCase: BusUseCase
) : ViewModel() {

    private val _homeState = MutableStateFlow<HomeScreenState>(HomeScreenState.Idle)
    val homeState: StateFlow<HomeScreenState> = _homeState

    private val _locationState = MutableStateFlow<LocationUpdateState>(LocationUpdateState.Idle)
    val locationState: StateFlow<LocationUpdateState> = _locationState

    private var busId: String? = null
    private var locationJob: Job? = null

    fun loadDriverBus() {
        viewModelScope.launch {
            busUseCase.getBusForCurrentDriver().collect { result ->
                when (result) {

                    is ResultState.Loading ->
                        _homeState.value = HomeScreenState.Loading

                    is ResultState.Success -> {
                        val bus = result.data
                        busId = bus.busId
                        _homeState.value = HomeScreenState.BusLoaded(bus)
                    }

                    is ResultState.Error -> {
                        if (result.message.contains("No bus", true))
                            _homeState.value = HomeScreenState.NoBusFound()
                        else
                            _homeState.value = HomeScreenState.Error(result.message)
                    }
                }
            }
        }
    }


     fun uploadLocationToServer(lat: Double, lon: Double) {
        val id = busId ?: return

        viewModelScope.launch(Dispatchers.IO) {
            busUseCase.updateBusLocation(id, lat, lon).collect { result ->
                when (result) {
                    is ResultState.Loading ->
                        _locationState.value = LocationUpdateState.Updating

                    is ResultState.Success ->
                        _locationState.value = LocationUpdateState.Success

                    is ResultState.Error ->
                        _locationState.value = LocationUpdateState.Error(result.message)
                }
            }
        }
    }


    fun startLocationSharing(
        context: Context,
        getLocation: suspend (Context) -> Pair<Double, Double>?
    ) {
        if (locationJob != null) return

        locationJob = viewModelScope.launch {
            _locationState.value = LocationUpdateState.Updating

            while (true) {
                val position = getLocation(context)
                if (position != null) {
                    uploadLocationToServer(position.first, position.second)
                }
                delay(3000)
            }
        }
    }

    fun stopLocationSharing() {
        locationJob?.cancel()
        locationJob = null
        _locationState.value = LocationUpdateState.Idle
    }
}
