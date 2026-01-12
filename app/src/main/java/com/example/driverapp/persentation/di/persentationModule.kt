package com.example.driverapp.persentation.di


import com.example.driverapp.persentation.Screens.AuthScreens.LoginScreen.LogInViewModel
import com.example.driverapp.persentation.Screens.AuthScreens.SignUpScreen.SignUpViewModel
import com.example.driverapp.persentation.Screens.BusScreen.BusCreationViewModel
import com.example.driverapp.persentation.Screens.HomeScreens.HomeViewModel
import com.example.driverapp.persentation.Screens.ProfileScreens.ProfileViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

var persentationModule = module {

    viewModel<SignUpViewModel> { SignUpViewModel(driverAuthUseCase = get()) }
    viewModel<LogInViewModel> { LogInViewModel(authUseCase = get()) }
    viewModel<BusCreationViewModel> { BusCreationViewModel(busUseCase = get()) }
    viewModel<HomeViewModel> { HomeViewModel(busUseCase = get()) }
    viewModel { ProfileViewModel(getCurrentDriverUseCase = get()) }
}