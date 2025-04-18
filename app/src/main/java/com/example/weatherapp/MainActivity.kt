package com.example.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.weatherapp.ui.theme.WeatherAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {
                val authViewModel: AuthViewModel = viewModel()
                val weatherViewModel: WeatherViewModel = viewModel()

                // Manage the state of whether the user is logged in or not
                var isLoggedIn by remember { mutableStateOf(authViewModel.user != null) }

                // Check if the user is already logged in
                if (isLoggedIn) {
                    // If logged in, show the WeatherScreen
                    WeatherScreen(authViewModel, weatherViewModel)
                } else {
                    // If not logged in, show the AuthScreen with onSuccess callback
                    AuthScreen(authViewModel) {
                        // On successful login or signup, update the state to show WeatherScreen
                        isLoggedIn = true
                    }
                }
            }
        }
    }
}
