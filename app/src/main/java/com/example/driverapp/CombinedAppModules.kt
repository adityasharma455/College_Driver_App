package com.example.driverapp

import com.example.driverapp.data.di.dataModule
import com.example.driverapp.domain.di.domainModule
import com.example.driverapp.persentation.di.persentationModule

val CombinedappModules = listOf(
    dataModule,
    domainModule,
    persentationModule

)