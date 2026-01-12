package com.example.driverapp.domain.di

import com.example.driverapp.domain.useCases.AuthSectionUseCase.DriverAuthUseCase
import com.example.driverapp.domain.useCases.BusSectionUseCase.BusUseCase
import org.koin.dsl.module


val domainModule = module{
        factory { DriverAuthUseCase(get())}
        factory { BusUseCase(get()) }
}