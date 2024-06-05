package com.example.admin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.admin.screens.LoginAdmin
import com.example.admin.viewmodel.LogInViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val loginViewModel: LogInViewModel = viewModel()
            val context = LocalContext.current
            NavHost(navController, startDestination = Screens.Login) {
                composable(route = Screens.Login) {
                    LoginAdmin(context,loginViewModel)
                }
            }

        }
    }
}

