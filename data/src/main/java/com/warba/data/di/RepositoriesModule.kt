package com.warba.data.di

import com.warba.data.repository.WeatherRepository
import com.warba.data.repository.WeatherRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface RepositoriesModule {

    @Binds
    fun provideWeatherRepo(weatherRepositoryImpl: WeatherRepositoryImpl): WeatherRepository
}