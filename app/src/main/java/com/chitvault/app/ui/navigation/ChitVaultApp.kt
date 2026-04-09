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
import com.chitvault.app.ui.viewmodel.AddUserViewModel
import com.chitvault.app.ui.viewmodel.HomeViewModel
import com.chitvault.app.ui.viewmodel.LoginViewModel

@Composable
fun ChitVaultApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Destinations.Login.route,
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
    }
}

