
package com.terrencealuda.tcardio.daggerhilt

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.terrencealuda.tcardio.repository.PassiveDataRepository.Companion.PREFERENCES_FILENAME
import com.terrencealuda.tcardio.storage.room.TCardioDatabase
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MainApplication : Application(), Configuration.Provider {
    @Inject lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(PREFERENCES_FILENAME)

const val TAG = "TCARDIO"
