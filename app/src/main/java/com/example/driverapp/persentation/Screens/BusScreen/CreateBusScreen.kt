package com.example.driverapp.persentation.Screens.BusScreen

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.BusAlert
import androidx.compose.material.icons.filled.Route
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.driverapp.domain.models.BusModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateBusScreen(
    navController: NavController,
    viewModel: BusCreationViewModel = koinViewModel(),

) {
    val context = LocalContext.current
    val state by viewModel.busState.collectAsState()

    var busId by remember { mutableStateOf("") }
    var busNumber by remember { mutableStateOf("") }
    var routeName by remember { mutableStateOf("") }

    val isFormValid = busId.isNotBlank() && busNumber.isNotBlank() && routeName.isNotBlank()

    // Observe State
    LaunchedEffect(state) {
        when (state) {
            is BusScreenState.BusCreated -> {
                Toast.makeText(context, "Bus Registered Successfully!", Toast.LENGTH_SHORT).show()

                // Reset fields
                busId = ""
                busNumber = ""
                routeName = ""

                viewModel.resetState()

            }

            is BusScreenState.Error -> {
                Toast.makeText(
                    context,
                    (state as BusScreenState.Error).message,
                    Toast.LENGTH_LONG
                ).show()
                viewModel.resetState()
            }

            else -> Unit
        }
    }

    // Gradient BG
    val gradientColors = listOf(
        Color(0xFF004E92),
        Color(0xFF000428)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(gradientColors))
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.extraLarge),
            elevation = CardDefaults.cardElevation(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.96f))
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {

                Text(
                    "Register Bus",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color(0xFF004E92),
                    textAlign = TextAlign.Center
                )

                // Bus ID
                OutlinedTextField(
                    value = busId,
                    onValueChange = { busId = it },
                    label = { Text("Bus ID") },
                    leadingIcon = { Icon(Icons.Default.Badge, null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Bus Number
                OutlinedTextField(
                    value = busNumber,
                    onValueChange = { busNumber = it },
                    label = { Text("Bus Number") },
                    leadingIcon = { Icon(Icons.Default.BusAlert, null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Route Name
                OutlinedTextField(
                    value = routeName,
                    onValueChange = { routeName = it },
                    label = { Text("Route Name") },
                    leadingIcon = { Icon(Icons.Default.Route, null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Submit Button
                Button(
                    onClick = {
                        if (isFormValid) {

                            val busData = BusModel(
                                busId = busId,
                                busNumber = busNumber,
                                routeName = routeName
                            )
                            viewModel.createBus(busData)

                        } else {
                            Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                        }
                    },
                    enabled = isFormValid && state !is BusScreenState.Loading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {

                    AnimatedVisibility(
                        visible = state is BusScreenState.Loading,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }

                    AnimatedVisibility(
                        visible = state !is BusScreenState.Loading,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Text("Create Bus")
                    }
                }
            }
        }
    }
}
