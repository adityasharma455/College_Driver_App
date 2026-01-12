package com.example.driverapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.driverapp.persentation.navigation.DriverAppNavigation
import com.example.driverapp.ui.theme.DriverAppTheme
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val firebaseAuth : FirebaseAuth by inject()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DriverAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DriverAppNavigation(firebaseAuth = firebaseAuth)
//                   FetchLiveLocation()
                }
            }
        }
    }
}
