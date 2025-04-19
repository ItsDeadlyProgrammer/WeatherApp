package com.example.weatherapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.android.gms.location.LocationServices

@SuppressLint("MissingPermission")
@Composable
fun WeatherScreen(authViewModel: AuthViewModel, viewModel: WeatherViewModel) {
    val context = LocalContext.current
    var cityInput by remember { mutableStateOf("") }
    val weather = viewModel.weatherData.collectAsState().value

    LaunchedEffect(Unit) {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                100
            )
            return@LaunchedEffect
        }

        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                viewModel.getWeatherByCoords(it.latitude, it.longitude)
            }
        }
    }

    val gradient = Brush.verticalGradient(
        colors = listOf(Color(0xFFFFE29F), Color(0xFFFFA07A), Color(0xFF5B86E5))
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize()
        ) {
            Row(Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = cityInput,
                    onValueChange = { cityInput = it },
                    label = { Text("Search city") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(Modifier.width(8.dp))
                Button(onClick = { viewModel.getWeatherByCity(cityInput) }, Modifier.padding(top=10.dp)) {
                    Text("Search")
                }
            }

            Spacer(Modifier.height(24.dp))

            weather?.let {
                val animationRes = when (it.weather.firstOrNull()?.main?.lowercase()) {
                    "clear" -> R.raw.sunny
                    "clouds" -> R.raw.cloudy
                    "rain" -> R.raw.rainy
                    "snow" -> R.raw.snowy
                    else -> R.raw.sunny
                }

                val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(animationRes))
                val progress by animateLottieCompositionAsState(composition, iterations = LottieConstants.IterateForever)

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(16.dp)
                        .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                        .padding(24.dp)
                ) {
                    LottieAnimation(
                        composition = composition,
                        progress = { progress },
                        modifier = Modifier.height(140.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Text("City: ${it.name}", fontSize = 22.sp)
                    Text("Temperature: ${it.main.temp} °C", fontSize = 18.sp)
                    Text("Condition: ${it.weather.firstOrNull()?.main}", fontSize = 18.sp)
                    Text("Humidity: ${it.main.humidity}%", fontSize = 18.sp)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = { authViewModel.logout() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Logout")
            }
        }
    }
}
