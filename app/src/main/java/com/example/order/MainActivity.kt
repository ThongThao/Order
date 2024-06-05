package com.example.order
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.ExperimentalComposeUiApi

import androidx.navigation.compose.rememberNavController
import com.example.order.navigation.Navigation

import com.example.order.ui.theme.OrderTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OrderTheme {
                val navController = rememberNavController()
                // Pass the NavController to the Navigation composable
                Navigation(navController)
            }
        }
    }
}
