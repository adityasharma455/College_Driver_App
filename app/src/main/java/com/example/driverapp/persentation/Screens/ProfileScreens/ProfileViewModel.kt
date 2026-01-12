package com.example.driverapp.persentation.Screens.ProfileScreens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.driverapp.Common.ResultState
import com.example.driverapp.domain.useCases.AuthSectionUseCase.DriverAuthUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getCurrentDriverUseCase: DriverAuthUseCase
) : ViewModel() {

    private val _profileState = MutableStateFlow<ProfileScreenState>(ProfileScreenState.Loading)
    val profileState = _profileState.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            getCurrentDriverUseCase.getCurrentDriver().collect { result ->
                when (result) {

                    is ResultState.Loading ->
                        _profileState.value = ProfileScreenState.Loading

                    is ResultState.Success ->
                        _profileState.value = ProfileScreenState.Success(result.data)

                    is ResultState.Error ->
                        _profileState.value = ProfileScreenState.Error(result.message)
                }
            }
        }
    }
}
