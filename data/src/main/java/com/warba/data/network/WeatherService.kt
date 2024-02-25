package com.warba.data.network

import com.warba.data.model.remote.CurrentWeather
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherService {
    @GET("timeline/{city}")
    suspend fun getWeather(
        @Path("city") cityName: String,
        @Query("unitGroup") unitGroup: String,
        @Query("key") apiKey: String,
        @Query("contentType") contentType: String
    ): Response<CurrentWeather>
}