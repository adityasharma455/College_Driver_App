package com.example.driverapp.persentation.Screens.AuthScreens.LoginScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.driverapp.Common.ResultState
import com.example.driverapp.domain.models.DriverModel
import com.example.driverapp.domain.useCases.AuthSectionUseCase.DriverAuthUseCase
import com.example.driverapp.persentation.Screens.AuthScreens.AuthScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class LogInViewModel(
    private val authUseCase: DriverAuthUseCase
): ViewModel() {

    private val _logInState= MutableStateFlow<AuthScreenState>(AuthScreenState.Idle)
    val logInState = _logInState.asStateFlow()

    fun logInDriver(email: String, password: String){
        viewModelScope.launch(Dispatchers.IO) {
            authUseCase.loginDriver(email, password).collectLatest { result ->
                when(result){
                    is ResultState.Loading -> {
                        _logInState.value = AuthScreenState.Loading
                    }
                    is ResultState.Success<DriverModel> ->{
                        _logInState.value = AuthScreenState.LogInSuccess(Success = result.data)

                    }
                    is ResultState.Error ->{
                        _logInState.value = AuthScreenState.Error(result.message.toString() ?: "Failed to LogIn")
                    }
                }

            }
        }
    }

    // 🔹 Sign out current driver
    fun logOut() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _logInState.value = AuthScreenState.Loading
                authUseCase.logoutDriver()  // ✅ calling your use case
                _logInState.value = AuthScreenState.signOut
            } catch (e: Exception) {
                _logInState.value = AuthScreenState.Error(e.message ?: "Sign out failed")
            }
        }
    }
}