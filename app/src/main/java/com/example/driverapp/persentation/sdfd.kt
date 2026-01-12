//package com.example.driverapp.persentation.Screens.HomeScreens
//
//import androidx.compose.animation.AnimatedVisibility
//import androidx.compose.animation.fadeIn
//import androidx.compose.animation.fadeOut
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.shadow
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavController
//import com.example.driverapp.domain.models.BusModel
//import org.koin.compose.viewmodel.koinViewModel
//
//@Composable
//fun HomeScreen(
//    navController: NavController,
//    viewModel: HomeViewModel = koinViewModel()
//) {
//    val state by viewModel.homeState.collectAsState()
//
//    LaunchedEffect(Unit) {
//        viewModel.loadDriverBus()
//    }
//
//    val gradient = Brush.verticalGradient(
//        colors = listOf(
//            Color(0xFF004E92),
//            Color(0xFF000428)
//        )
//    )
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(gradient)
//            .padding(16.dp)
//    ) {
//
//        when (state) {
//
//            is HomeScreenState.Loading -> LoadingUI()
//
//            is HomeScreenState.Error -> ErrorUI(
//                (state as HomeScreenState.Error).message
//            )
//
//            is HomeScreenState.NoBusFound -> NoBusUI()
//
//            is HomeScreenState.BusLoaded -> {
//                val bus = (state as HomeScreenState.BusLoaded).bus
//                HomeDashboard(bus)
//            }
//
//            else -> {}
//        }
//    }
//}
//
//@Composable
//fun LoadingUI() {
//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        CircularProgressIndicator(color = Color.White)
//    }
//}
//
//@Composable
//fun ErrorUI(message: String) {
//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        Text(message, color = Color.Red, fontWeight = FontWeight.Bold)
//    }
//}
//
//@Composable
//fun NoBusUI() {
//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        Text(
//            "No Bus Assigned!",
//            color = Color.White,
//            fontSize = 20.sp,
//            fontWeight = FontWeight.Bold
//        )
//    }
//}
//
//@Composable
//fun HomeDashboard(bus: BusModel) {
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(8.dp),
//        verticalArrangement = Arrangement.Top,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//
//        // 🌟 Welcome Card
//        Card(
//            modifier = Modifier
//                .fillMaxWidth()
//                .shadow(12.dp, RoundedCornerShape(20.dp)),
//            colors = CardDefaults.cardColors(
//                containerColor = Color.White.copy(alpha = 0.95f)
//            )
//        ) {
//            Column(
//                modifier = Modifier.padding(20.dp),
//                horizontalAlignment = Alignment.Start
//            ) {
//                Text(
//                    text = "Welcome,",
//                    style = MaterialTheme.typography.titleMedium,
//                    color = Color.Gray
//                )
//                Text(
//                    text = bus.driverName.ifBlank { "Driver" },
//                    style = MaterialTheme.typography.headlineMedium,
//                    fontWeight = FontWeight.Bold,
//                    color = Color(0xFF004E92)
//                )
//            }
//        }
//
//        Spacer(Modifier.height(20.dp))
//
//        // 🚍 Bus Info Card
//        AnimatedVisibility(visible = true, enter = fadeIn(), exit = fadeOut()) {
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .shadow(10.dp, RoundedCornerShape(20.dp)),
//                colors = CardDefaults.cardColors(
//                    containerColor = Color.White.copy(alpha = 0.95f)
//                )
//            ) {
//                Column(modifier = Modifier.padding(20.dp)) {
//
//                    Text(
//                        "Your Bus",
//                        style = MaterialTheme.typography.titleLarge,
//                        fontWeight = FontWeight.Bold,
//                        color = Color(0xFF004E92)
//                    )
//
//                    Spacer(Modifier.height(12.dp))
//
//                    BusInfoRow("Bus ID", bus.busId)
//                    BusInfoRow("Number", bus.busNumber)
//                    BusInfoRow("Route", bus.routeName)
//                    BusInfoRow("Status", if (bus.isActive) "Active" else "Inactive")
//                }
//            }
//        }
//
//        Spacer(Modifier.height(30.dp))
//
//        // 🟢 Start Sharing Button
//        Button(
//            onClick = { /* EMPTY */ },
//            colors = ButtonDefaults.buttonColors(Color(0xFF00C853)),
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(55.dp)
//        ) {
//            Text(
//                "Start Sharing Location",
//                color = Color.White,
//                fontSize = 18.sp,
//                fontWeight = FontWeight.Bold
//            )
//        }
//
//        Spacer(Modifier.height(16.dp))
//
//        // 🔴 Stop Sharing Button
//        Button(
//            onClick = { /* EMPTY */ },
//            colors = ButtonDefaults.buttonColors(Color(0xFFD50000)),
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(55.dp)
//        ) {
//            Text(
//                "Stop Sharing Location",
//                color = Color.White,
//                fontSize = 18.sp,
//                fontWeight = FontWeight.Bold
//            )
//        }
//    }
//}
//
//@Composable
//fun BusInfoRow(title: String, value: String) {
//    Column(modifier = Modifier.padding(bottom = 12.dp)) {
//        Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
//        Text(
//            value,
//            color = Color.Gray,
//            fontSize = 15.sp,
//            maxLines = 1,
//            overflow = TextOverflow.Ellipsis
//        )
//    }
//}
