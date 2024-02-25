package com.warba.core.utils

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DateUtilModule {

    @Provides
    fun provideUtil(): DateUtil {
        return DateUtil
    }
}