package com.local.growkart.di.module

import android.annotation.SuppressLint
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.text.SimpleDateFormat
import javax.inject.Qualifier

@InstallIn(SingletonComponent::class)
@Module
object DateModule {

    @SuppressLint("SimpleDateFormat")
    @Provides
    @DDMMYY
    fun provideDDMMYYDateFormatter() = SimpleDateFormat("dd/MM/yy hh:mm a")

    @SuppressLint("SimpleDateFormat")
    @Provides
    @DDMMMYYYY
    fun provideDDMMMYYYYDateFormatter() = SimpleDateFormat("dd/MMM/yyyy")
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DDMMYY

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DDMMMYYYY
