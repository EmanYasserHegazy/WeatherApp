package com.warba.data.util

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.warba.data.model.remote.CurrentWeather
import com.warba.data.model.remote.Day
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharedPreferenceUtil @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    @ApplicationContext private val context: Context,
    private val gson: Gson
) {

    fun saveModelList(key: String, modelList: List<Day>?) {
        val json = gson.toJson(modelList)
        sharedPreferences.edit().putString(key, json).apply()
    }

    fun getModelList(key: String): List<Day>? {
        val json = sharedPreferences.getString(key, null)
        return if (json != null) {
            val type = object : TypeToken<List<CurrentWeather>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }
}
