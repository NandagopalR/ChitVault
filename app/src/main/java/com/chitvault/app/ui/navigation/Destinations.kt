package com.chitvault.app.ui.navigation

sealed class Destinations(val route: String) {
    data object Login : Destinations("login")
    data object Home : Destinations("home")
    data object AddUser : Destinations("add_user")
}
