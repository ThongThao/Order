// Navigation.kt
package com.example.order.navigation

import SignInScreen
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.order.Authentication.SignUpScreen
import com.example.order.Screens.HomeNavigation
import com.example.order.Welcome.PageViewScreen
import com.example.order.Welcome.SplashScreen
import com.example.order.Welcome.WelcomeScreen

@ExperimentalMaterial3Api
@ExperimentalComposeUiApi
@Composable
fun Navigation(navController: NavHostController) {
    NavHost(
        navController = navController, startDestination = Screen.splash.route
    ) {
        composable(route = Screen.splash.route) {
            SplashScreen(navToPageview = {
                navController.navigate(Screen.pageview.route)
            })
        }
        composable(route = Screen.pageview.route) {
            PageViewScreen(navToWelcom = {
                navController.navigate(Screen.welcom.route)
            })
        }
        composable(route = Screen.welcom.route) {
            WelcomeScreen(navToLogin = {
                navController.navigate(Screen.Login.route)
            }, navToSignup = { navController.navigate(Screen.Register.route)})
        }
        composable(route = Screen.Login.route) {
            SignInScreen(
                navToHome = { user ->
                    navController.navigate(Screen.Home.createRoute(user.id ?: ""))
                },
                navToSignUp = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }
        composable(route = Screen.Register.route) {
            SignUpScreen(navToLogin = {
                navController.navigate(Screen.Login.route)
            })
        }
        composable(route = Screen.Home.route,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            HomeNavigation(userId)
        }


    }
}
