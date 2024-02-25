package com.warba.data.model.remote

import com.google.gson.annotations.SerializedName

data class CurrentWeather(
    @SerializedName("errorCode") val code: String? = null,
    @SerializedName("queryCost") val queryCost: Int = 0,
    @SerializedName("latitude") val latitude: Double = 0.0,
    @SerializedName("longitude") val longitude: Double = 0.0,
    @SerializedName("resolvedAddress") val resolvedAddress: String? = null,
    @SerializedName("address") val address: String? = null,
    @SerializedName("timezone") val timezone: String? = null,
    @SerializedName("tzoffset") val tzoffset: Int = 0,
    @SerializedName("description") val description: String? = null,
    @SerializedName("days") val days: List<Day>? = listOf(),
    @SerializedName("alerts") val alerts: List<Any>? = listOf(),
    @SerializedName("stations") val stations: Map<String, Station>? = mapOf(),
    @SerializedName("currentConditions") val currentConditions: CurrentConditions? = null
)

data class Day(
    @SerializedName("datetime") val datetime: String,
    @SerializedName("datetimeEpoch") val datetimeEpoch: Long? = 0L,
    @SerializedName("tempmax") val tempMax: Double,
    @SerializedName("tempmin") val tempMin: Double,
    @SerializedName("description") val description: String?,
    @SerializedName("icon") val dayWeatherState: String?
)

data class Station(
    @SerializedName("distance") val distance: Int,
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
)

data class CurrentConditions(
    @SerializedName("datetime") val datetime: String,
    @SerializedName("datetimeEpoch") val datetimeEpoch: Long,
    @SerializedName("temp") val temp: Double,
)
