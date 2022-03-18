package com.local.growkart.di.module

import android.content.Context
import android.content.SharedPreferences
import com.local.growkart.util.DatabaseUtil
import com.local.growkart.util.PreferenceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedPrefModule {
    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context) =
        context.getSharedPreferences(
            DatabaseUtil.PREF_NAME, Context.MODE_PRIVATE
        )

    @Provides
    @Singleton
    fun providePrefManager(pref: SharedPreferences) = PreferenceManager(pref)

}