package com.example.order.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val title: String = "",
    val icon: ImageVector = Icons.Default.Info
) {
    object splash : Screen(
        route = "splash"
    )
    object pageview : Screen(
        route = "pageview"
    )
    object welcom : Screen(
        route = "welcom"
    )
    object Login : Screen(
        route = "login"
    )

    object Register : Screen(
        route = "register"
    )

    object reset : Screen(
        route = "resetPassword"
    )

    object VerifyPassword : Screen(
        route = "verifyPassword"
    )

    object changePassword : Screen(
        route = "changePassword"
    )

    object Home : Screen(
        route = "home/{userId}",
        title = "Home",
        icon = Icons.Default.Home
    ){
        fun createRoute(userId: String) = "home/$userId"
    }
    object Restaurant : Screen("restaurant/{userId}") {
        fun createRoute(userId: String) = "restaurant/$userId"
    }
    object ChangeAddress : Screen("changeAddress/{userId}") {
        fun createRoute(userId: String) = "changeAddress/$userId"
    }
    object Cart: Screen(
        route = "cart/{userId}"
    )
    {
        fun createRoute(userId: String) = "cart/$userId"
    }

}
