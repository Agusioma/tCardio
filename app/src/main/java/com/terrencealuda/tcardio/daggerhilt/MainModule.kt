package com.terrencealuda.tcardio.daggerhilt

import android.content.Context
import androidx.health.services.client.HealthServices
import androidx.health.services.client.HealthServicesClient
import com.terrencealuda.tcardio.predictor.HeartPredictor
import com.terrencealuda.tcardio.storage.room.TCardioDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MainModule {
    @Singleton
    @Provides
    fun provideHealthServicesClient(@ApplicationContext context: Context): HealthServicesClient =
        HealthServices.getClient(context)

    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context: Context) = context.dataStore

    @Singleton
    @Provides
    fun provideDB(@ApplicationContext context: Context) = TCardioDatabase.getDatabase(context)

    @Singleton
    @Provides
    fun providePredictor(@ApplicationContext context: Context) = HeartPredictor(context)

    @Singleton
    @Provides
    fun provideApplicationCoroutineScope(): CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Default)

}
