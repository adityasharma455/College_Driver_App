package com.example.driverapp.data.di

import com.example.driverapp.data.repoImplementation.repoImplementation
import com.example.driverapp.domain.repo.Repo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.koin.dsl.module

val dataModule= module {

        single<FirebaseFirestore> { FirebaseFirestore.getInstance() }
        single<FirebaseAuth> { FirebaseAuth.getInstance() }

        single<Repo> { repoImplementation(
            firestore = get(), auth = get(),
        ) }
}