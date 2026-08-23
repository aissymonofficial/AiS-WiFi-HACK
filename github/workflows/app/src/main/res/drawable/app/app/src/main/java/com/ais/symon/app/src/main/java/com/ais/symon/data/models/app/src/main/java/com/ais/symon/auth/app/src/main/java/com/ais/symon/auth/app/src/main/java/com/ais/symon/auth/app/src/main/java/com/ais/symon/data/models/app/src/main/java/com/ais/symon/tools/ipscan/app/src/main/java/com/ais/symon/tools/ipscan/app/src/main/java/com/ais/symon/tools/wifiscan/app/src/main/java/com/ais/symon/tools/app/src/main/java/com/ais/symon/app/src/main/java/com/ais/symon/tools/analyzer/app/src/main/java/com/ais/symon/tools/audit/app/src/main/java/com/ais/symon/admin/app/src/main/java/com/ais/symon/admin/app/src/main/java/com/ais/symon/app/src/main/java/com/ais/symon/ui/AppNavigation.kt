package com.ais.symon.ui

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ais.symon.admin.AdminScreen
import com.ais.symon.auth.AuthViewModel
import com.ais.symon.auth.LoginScreen
import com.ais.symon.auth.RegisterScreen
import com.ais.symon.ui.screens.*

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Dashboard : Screen("dashboard")
    object Scanner : Screen("scanner")
    object Results : Screen("results/{scanType}") {
        fun createRoute(scanType: String) = "results/$scanType"
    }
    object AdminPanel : Screen("admin")
    object WiFiScanner : Screen("wifiscanner")
    object WiFiDeauth : Screen("wifideauth")
    object WiFiAnalyzer : Screen("wifianalyzer")
    object WiFiAudit : Screen("wifiaudit")
    object Settings : Screen("settings")
    object Premium : Screen("premium")
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val state by authViewModel.state.collectAsState()
    var startDest by remember { mutableStateOf(Screen.Login.route) }

    LaunchedEffect(state.isLoggedIn) {
        if (state.isLoggedIn) {
            if (state.isAdmin) {
                navController.navigate(Screen.AdminPanel.route) {
                    popUpTo(0) { inclusive = true }
                }
            } else {
                navController.navigate(Screen.Dashboard.route) {
                    popUpTo(0) { inclusive = true }
                }
            }
        }
    }

    NavHost(navController = navController, startDestination = startDest) {
        composable(Screen.Login.route) {
            if (state.isLoggedIn) return@composable
            LoginScreen(
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                onLoginSuccess = { /* Handled by LaunchedEffect */ }
            )
        }
        composable(Screen.Register.route) {
            RegisterScreen(
                onNavigateToLogin = { navController.popBackStack() },
                onRegisterSuccess = { /* Handled by LaunchedEffect */ }
            )
        }
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onNavigateToScanner = { navController.navigate(Screen.Scanner.route) },
                onNavigateToWiFiScanner = { navController.navigate(Screen.WiFiScanner.route) },
                onNavigateToDeauth = { navController.navigate(Screen.WiFiDeauth.route) },
                onNavigateToAnalyzer = { navController.navigate(Screen.WiFiAnalyzer.route) },
                onNavigateToAudit = { navController.navigate(Screen.WiFiAudit.route) },
                onNavigateToAdmin = { navController.navigate(Screen.AdminPanel.route) },
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
                onNavigateToPremium = { navController.navigate(Screen.Premium.route) },
                onLogout = { authViewModel.logout() },
                isAdmin = state.isAdmin
            )
        }
        composable(Screen.Scanner.route) {
            ScannerScreen(
                onBack = { navController.popBackStack() },
                onResults = { scanType -> navController.navigate(Screen.Results.createRoute(scanType)) }
            )
        }
        composable(
            Screen.Results.route,
            arguments = listOf(navArgument("scanType") { type = NavType.StringType })
        ) { backStackEntry ->
            val scanType = backStackEntry.arguments?.getString("scanType") ?: "all"
            ResultsScreen(scanType = scanType, onBack = { navController.popBackStack() })
        }
        composable(Screen.AdminPanel.route) {
            if (state.isAdmin) AdminScreen()
            else {
                // Non-admin redirected
                navController.navigate(Screen.Dashboard.route) { popUpTo(0) { inclusive = true } }
            }
        }
        composable(Screen.WiFiScanner.route) {
            WiFiScannerScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.WiFiDeauth.route) {
            WiFiDeauthScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.WiFiAnalyzer.route) {
            WiFiAnalyzerScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.WiFiAudit.route) {
            WiFiAuditScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Settings.route) {
            SettingsScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Premium.route) {
            PremiumScreen(onBack = { navController.popBackStack() })
        }
    }
}
