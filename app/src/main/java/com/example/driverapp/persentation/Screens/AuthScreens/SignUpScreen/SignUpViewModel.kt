package com.example.driverapp.persentation.Screens.AuthScreens.SignUpScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.driverapp.Common.ResultState
import com.example.driverapp.domain.models.DriverModel
import com.example.driverapp.domain.useCases.AuthSectionUseCase.DriverAuthUseCase
import com.example.driverapp.persentation.Screens.AuthScreens.AuthScreenState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.sql.Driver

class SignUpViewModel(
    private val driverAuthUseCase: DriverAuthUseCase
): ViewModel() {

    private val _signUpState = MutableStateFlow<AuthScreenState>(AuthScreenState.Idle)
    val signUpState = _signUpState.asStateFlow()

    fun registerDriver(driverData: DriverModel){
        viewModelScope.launch(Dispatchers.IO) {
            driverAuthUseCase.registerDriver(driverData).collectLatest { result->
                when(result){
                    is ResultState.Loading -> {
                        _signUpState.value = AuthScreenState.Loading
                    }
                    is ResultState.Success<Boolean> -> {
                        if (result.data == true){
                            _signUpState.value = AuthScreenState.RegistrationSuccess(Success = result.data)
                        }
                        else {
                            _signUpState.value = AuthScreenState.Error("Registration Returend False")
                        }
                    }
                    is ResultState.Error -> {
                        _signUpState.value = AuthScreenState.Error(result.message.toString() ?: "Registration Failed")
                    }
                }


            }

        }
    }



}