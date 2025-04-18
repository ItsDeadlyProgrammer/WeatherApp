package com.example.weatherapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    private val _weatherData = MutableStateFlow<WeatherResponse?>(null)
    val weatherData = _weatherData.asStateFlow()

    fun getWeatherByCity(city: String) {
        viewModelScope.launch {
            try {
                _weatherData.value = RetrofitClient.api.getWeatherByCity(city)
            } catch (_: Exception) { }
        }
    }

    fun getWeatherByCoords(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                _weatherData.value = RetrofitClient.api.getWeatherByCoords(lat, lon)
            } catch (_: Exception) { }
        }
    }
}
