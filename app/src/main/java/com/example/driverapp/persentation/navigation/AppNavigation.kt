package com.example.driverapp.persentation.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.bottombar.AnimatedBottomBar
import com.example.bottombar.components.BottomBarItem
import com.example.bottombar.model.IndicatorDirection
import com.example.bottombar.model.IndicatorStyle
import com.example.driverapp.persentation.Screens.AuthScreens.LoginScreen.DriverLogInScreen
import com.example.driverapp.persentation.Screens.AuthScreens.SignUpScreen.DriverSignUpScreen
import com.example.driverapp.persentation.Screens.BusScreen.CreateBusScreen
import com.example.driverapp.persentation.Screens.HomeScreens.HomeScreen
import com.example.driverapp.persentation.Screens.ProfileScreens.DriverProfileScreen
import com.google.firebase.auth.FirebaseAuth

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DriverAppNavigation(firebaseAuth: FirebaseAuth) {
    val navController = rememberNavController()
    var selectedItem by remember { mutableIntStateOf(0) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Define authentication routes
    val authRoutes = setOf(
        Routes.DriverLogInScreenRoutes,
        Routes.DriverSignUpScreenRoutes
    )

    // Show bottom bar only if logged in & not on login/signup screens
    val showBottomBar = currentRoute != null &&
            !authRoutes.contains(currentRoute) &&
            firebaseAuth.currentUser != null

    // Decide start screen based on login status
    val startScreen = if (firebaseAuth.currentUser == null) {
        SubNavigation.AuthScreenRoutes
    } else {
        SubNavigation.HomeScreenRoutes
    }

    // Define bottom navigation items
    val bottomNavItems = listOf(
        BottomItem(title = "Home", icon = Icons.Default.Home),
        BottomItem(title = "Profile", icon = Icons.Default.AccountCircle),
        BottomItem(title ="Bus", icon = Icons.Default.DirectionsBus)
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomBar) {
                AnimatedBottomBar(
                    modifier = Modifier.padding(WindowInsets.navigationBars.asPaddingValues()),
                    selectedItem = selectedItem,
                    itemSize = bottomNavItems.size,
                    containerColor = Color.Transparent,
                    indicatorStyle = IndicatorStyle.FILLED,
                    indicatorColor = Color(0xFF1565C0),
                    indicatorDirection = IndicatorDirection.BOTTOM
                ) {
                    bottomNavItems.forEachIndexed { index, item ->
                        BottomBarItem(
                            selected = selectedItem == index,
                            onClick = {
                                selectedItem = index
                                when (index) {
                                    0 -> navController.navigate(Routes.HomeScreenRoutes) {
                                        popUpTo(SubNavigation.HomeScreenRoutes) { inclusive = true }
                                    }
                                    1 -> navController.navigate(Routes.DriverProfileScreenRoutes){
                                        popUpTo(SubNavigation.HomeScreenRoutes) { inclusive = true }
                                    }
                                    2-> navController.navigate(Routes.createBusScreenRoutes){
                                        popUpTo(SubNavigation.HomeScreenRoutes) { inclusive = true }
                                    }
                                }
                            },
                            imageVector = item.icon,
                            label = item.title,
                            containerColor = Color.Transparent
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavHost(navController = navController, startDestination = startScreen) {

                // AUTH ROUTES
                navigation<SubNavigation.AuthScreenRoutes>(
                    startDestination = Routes.DriverLogInScreenRoutes
                ) {
                    composable<Routes.DriverLogInScreenRoutes> {
                        DriverLogInScreen(navController = navController)
                    }
                    composable<Routes.DriverSignUpScreenRoutes> {
                        DriverSignUpScreen(navController= navController)
                    }
                }

                // HOME ROUTES
                navigation<SubNavigation.HomeScreenRoutes>(
                    startDestination = Routes.HomeScreenRoutes
                ) {
                    composable<Routes.HomeScreenRoutes> {
                        HomeScreen(navController = navController)
                    }
                }

                navigation<SubNavigation.BusCreationRoutes>(
                    startDestination = Routes.createBusScreenRoutes
                ){
                    composable<Routes.createBusScreenRoutes> {
                        CreateBusScreen(navController = navController)
                    }
                }

                // PROFILE ROUTES
                navigation<SubNavigation.DriverProfileRoutes>(
                    startDestination = Routes.DriverProfileScreenRoutes
                ) {
                    composable<Routes.DriverProfileScreenRoutes> {
                        DriverProfileScreen(navController = navController)
                    }
                }
            }
        }
    }
}

data class BottomItem(
    val title: String,
    val icon: ImageVector
)
