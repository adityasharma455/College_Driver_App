package com.example.driverapp.persentation.Screens.BusScreen


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.driverapp.Common.ResultState
import com.example.driverapp.domain.models.BusModel
import com.example.driverapp.domain.repo.Repo
import com.example.driverapp.domain.useCases.BusSectionUseCase.BusUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class BusCreationViewModel(
    private val busUseCase: BusUseCase
) : ViewModel() {

    private val _busState = MutableStateFlow<BusScreenState>(BusScreenState.Idle)
    val busState = _busState.asStateFlow()

    /** CREATE or UPDATE bus */
    fun createBus(bus: BusModel) {
        viewModelScope.launch(Dispatchers.IO) {
            busUseCase.createBusUseCase(bus).collectLatest { result ->
                when (result) {
                    is ResultState.Loading -> {
                        _busState.value = BusScreenState.Loading
                    }
                    is ResultState.Success -> {
                        _busState.value = BusScreenState.BusCreated(true)
                    }
                    is ResultState.Error -> {
                        _busState.value = BusScreenState.Error(result.message ?: "Failed to create bus")
                    }
                }
            }
        }
    }

    fun resetState() {
        _busState.value = BusScreenState.Idle
    }

}
