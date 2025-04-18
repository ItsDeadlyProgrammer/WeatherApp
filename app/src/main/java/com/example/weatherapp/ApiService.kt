package com.example.weatherapp

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("weather")
    suspend fun getWeatherByCity(
        @Query("q") city: String,
        @Query("appid") apiKey: String = RetrofitClient.API_KEY,
        @Query("units") units: String = "metric"
    ): WeatherResponse

    @GET("weather")
    suspend fun getWeatherByCoords(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String = RetrofitClient.API_KEY,
        @Query("units") units: String = "metric"
    ): WeatherResponse
}
