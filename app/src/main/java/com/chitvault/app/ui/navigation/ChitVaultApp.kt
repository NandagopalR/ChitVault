package com.chitvault.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.chitvault.app.ui.screens.adduser.AddUserScreen
import com.chitvault.app.ui.screens.home.HomeScreen
import com.chitvault.app.ui.screens.login.LoginScreen
import com.chitvault.app.ui.screens.settings.SettingsScreen
import com.chitvault.app.ui.viewmodel.AddUserViewModel
import com.chitvault.app.ui.viewmodel.HomeViewModel
import com.chitvault.app.ui.viewmodel.LoginViewModel
import com.chitvault.app.ui.viewmodel.SettingsViewModel

@Composable
fun ChitVaultApp(
    modifier: Modifier = Modifier,
    isLoggedIn: Boolean = false,
) {
    val navController = rememberNavController()
    val startDestination = if (isLoggedIn) Destinations.Home.route else Destinations.Login.route

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        composable(Destinations.Login.route) {
            val viewModel: LoginViewModel = hiltViewModel()
            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = {
                    navController.navigate(Destinations.Home.route) {
                        popUpTo(Destinations.Login.route) { inclusive = true }
                    }
                },
            )
        }
        composable(Destinations.Home.route) {
            val viewModel: HomeViewModel = hiltViewModel()

            val savedStateHandle = it.savedStateHandle
            LaunchedEffect(Unit) {
                savedStateHandle.getStateFlow("user_added", false).collect { added ->
                    if (added) {
                        savedStateHandle["user_added"] = false
                        viewModel.refresh()
                    }
                }
            }

            HomeScreen(
                viewModel = viewModel,
                onAddUser = { navController.navigate(Destinations.AddUser.route) },
                onSettings = { navController.navigate(Destinations.Settings.route) },
            )
        }
        composable(Destinations.AddUser.route) {
            val viewModel: AddUserViewModel = hiltViewModel()
            AddUserScreen(
                viewModel = viewModel,
                onSaveSuccess = {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("user_added", true)
                    navController.popBackStack()
                },
                onNavigateBack = { navController.popBackStack() },
            )
        }
        composable(Destinations.Settings.route) {
            val viewModel: SettingsViewModel = hiltViewModel()
            SettingsScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onLoggedOut = {
                    navController.navigate(Destinations.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
            )
        }
    }
}


