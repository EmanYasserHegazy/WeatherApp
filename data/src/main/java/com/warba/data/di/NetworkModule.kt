package com.warba.data.di

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.warba.data.network.Constants
import com.warba.data.network.Constants.BASE_URL
import com.warba.data.network.Constants.TIMEOUT_CONNECT
import com.warba.data.network.Constants.TIMEOUT_READ
import com.warba.data.network.WeatherService
import com.warba.data.util.SharedPreferenceUtil
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("weather_pref", Context.MODE_PRIVATE)
    }

    @Provides
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    fun provideCache(@ApplicationContext context: Context): Cache {
        val cacheDirectory = File(context.cacheDir, "temperature-cache")
        return Cache(cacheDirectory, Constants.CACHE_SIZE.toLong())
    }

    @Provides
    fun provideSharedPreferencesUtil(
        sharedPreferences: SharedPreferences,
        @ApplicationContext context: Context,
        gson: Gson
    ): SharedPreferenceUtil {
        return SharedPreferenceUtil(sharedPreferences, context, gson)
    }

    @Provides
    fun provideOkHttpClient(
        cache: Cache
    ): OkHttpClient {
        val okHttpBuilder = OkHttpClient.Builder()
        okHttpBuilder.connectTimeout(TIMEOUT_CONNECT.toLong(), TimeUnit.SECONDS)
        okHttpBuilder.readTimeout(TIMEOUT_READ.toLong(), TimeUnit.SECONDS)
        okHttpBuilder.readTimeout(TIMEOUT_READ.toLong(), TimeUnit.SECONDS)
        okHttpBuilder.cache(cache)
        return okHttpBuilder.build()
    }

    @Singleton
    @Provides
    fun provideGsonFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, gsonFactory: GsonConverterFactory): Retrofit {
        return Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(gsonFactory)
            .client(okHttpClient).build()

    }

    @Provides
    @Singleton
    fun provideWeatherService(retrofit: Retrofit): WeatherService {
        return retrofit.create(WeatherService::class.java)
    }
}