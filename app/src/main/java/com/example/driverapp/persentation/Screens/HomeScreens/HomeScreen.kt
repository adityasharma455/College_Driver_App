package com.example.driverapp.persentation.Screens.HomeScreens

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.example.driverapp.domain.models.BusModel
import com.example.driverapp.persentation.Screens.LocationForegroundService
import com.example.driverapp.persentation.Screens.LocationScreen.getLocationSuspend
import com.example.driverapp.persentation.Screens.LocationScreen.isLocationEnabled
import com.example.driverapp.persentation.Screens.LocationScreen.openLocationSettings
import com.google.accompanist.permissions.*
import org.koin.compose.viewmodel.koinViewModel

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = koinViewModel()
) {
    val homeState by viewModel.homeState.collectAsState()
    val locationState by viewModel.locationState.collectAsState()

    val context = LocalContext.current
    // ⭐⭐⭐ ADD THIS BLOCK BELOW ⭐⭐⭐
    DisposableEffect(Unit) {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(ctx: Context?, intent: Intent?) {
                val lat = intent?.getDoubleExtra("lat", 0.0) ?: return
                val lon = intent.getDoubleExtra("lon", 0.0)

                // send location to ViewModel to upload
                viewModel.uploadLocationToServer(lat, lon)
            }
        }

        context.registerReceiver(
            receiver,
            IntentFilter("LOCATION_UPDATE"),
            Context.RECEIVER_NOT_EXPORTED
        )


        onDispose {
            context.unregisterReceiver(receiver)
        }
    }
    // ⭐⭐⭐ END OF ADDED BLOCK ⭐⭐⭐
    val snackbarHostState = remember { SnackbarHostState() }

    val locationPermission = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    // Load bus info initially
    LaunchedEffect(Unit) { viewModel.loadDriverBus() }

    // Show snackbar for location update result
    LaunchedEffect(locationState) {
        when (locationState) {
            is LocationUpdateState.Success ->
                snackbarHostState.showSnackbar("Location Updated ✔")

            is LocationUpdateState.Error ->
                snackbarHostState.showSnackbar(
                    (locationState as LocationUpdateState.Error).message
                )

            else -> {}
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF004E92), Color(0xFF000428))
                    )
                )
                .padding(16.dp)
        ) {
            when (homeState) {

                is HomeScreenState.Loading -> LoadingUI()

                is HomeScreenState.Error ->
                    ErrorUI((homeState as HomeScreenState.Error).message)

                is HomeScreenState.NoBusFound -> NoBusUI()

                is HomeScreenState.BusLoaded -> {
                    val bus = (homeState as HomeScreenState.BusLoaded).bus

                    HomeDashboard(
                        bus = bus,
                        locationState = locationState,

                        // START SHARING BUTTON
                        onStartSharing = {

                            if (!locationPermission.status.isGranted) {
                                locationPermission.launchPermissionRequest()
                                return@HomeDashboard
                            }

                            if (!isLocationEnabled(context)) {
                                openLocationSettings(context)
                                return@HomeDashboard
                            }

                            val serviceIntent =
                                Intent(context, LocationForegroundService::class.java)
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                context.startForegroundService(serviceIntent)
                            } else {
                                context.startService(serviceIntent)
                            }
                        },

                        // STOP SHARING BUTTON
                        onStopSharing = {
                            val serviceIntent = Intent(context, LocationForegroundService::class.java)
                            context.stopService(serviceIntent)
                        }
                    )
                }

                else -> {}
            }
        }
    }
}


@Composable
fun LoadingUI() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator(color = Color.White)
    }
}

@Composable
fun ErrorUI(msg: String) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(msg, color = Color.Red, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun NoBusUI() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("No Bus Assigned!", color = Color.White, fontSize = 20.sp)
    }
}

@Composable
fun HomeDashboard(
    bus: BusModel,
    locationState: LocationUpdateState,
    onStartSharing: () -> Unit,
    onStopSharing: () -> Unit
) {

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Welcome UI
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(12.dp, RoundedCornerShape(20.dp)),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(Modifier.padding(20.dp)) {
                Text("Welcome", color = Color.Gray)
                Text(
                    bus.driverName,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF004E92)
                )
            }
        }

        Spacer(Modifier.height(20.dp))

        // Bus Info Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(12.dp, RoundedCornerShape(20.dp)),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(Modifier.padding(20.dp)) {
                Text("Your Bus", fontSize = 22.sp, fontWeight = FontWeight.Bold)

                Spacer(Modifier.height(10.dp))

                BusInfoRow("Bus ID", bus.busId)
                BusInfoRow("Number", bus.busNumber)
                BusInfoRow("Route", bus.routeName)
            }
        }

        Spacer(Modifier.height(30.dp))

        // LIVE indicator
        AnimatedVisibility(
            visible = locationState is LocationUpdateState.Updating,
            enter = fadeIn(), exit = fadeOut()
        ) {
            Text(
                "🟢 LIVE SHARING",
                color = Color.Green,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }

        Spacer(Modifier.height(20.dp))

        // Start Sharing Button
        Button(
            onClick = onStartSharing,
            colors = ButtonDefaults.buttonColors(Color(0xFF00C853)),
            modifier = Modifier.fillMaxWidth().height(55.dp)
        ) {
            Text("Start Sharing Location", color = Color.White, fontSize = 18.sp)
        }

        Spacer(Modifier.height(12.dp))

        // Stop Sharing Button
        Button(
            onClick = onStopSharing,
            colors = ButtonDefaults.buttonColors(Color(0xFFD50000)),
            modifier = Modifier.fillMaxWidth().height(55.dp)
        ) {
            Text("Stop Sharing Location", color = Color.White, fontSize = 18.sp)
        }
    }
}

@Composable
fun BusInfoRow(label: String, value: String) {
    Column(Modifier.padding(bottom = 8.dp)) {
        Text(label, fontWeight = FontWeight.Bold)
        Text(value, color = Color.Gray)
    }
}
