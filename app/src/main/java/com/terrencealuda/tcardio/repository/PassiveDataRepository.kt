package com.terrencealuda.tcardio.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.HeartRateAccuracy
import androidx.health.services.client.data.HeartRateAccuracy.SensorStatus.Companion.ACCURACY_HIGH
import androidx.health.services.client.data.HeartRateAccuracy.SensorStatus.Companion.ACCURACY_MEDIUM
import androidx.health.services.client.data.IntervalDataPoint
import androidx.health.services.client.data.SampleDataPoint
import com.terrencealuda.tcardio.storage.room.HeartBpm
import com.terrencealuda.tcardio.storage.room.Predictions
import com.terrencealuda.tcardio.storage.room.TCardioDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

/**
 * Stores heart rate measurements and whether or not passive data is enabled.
 */
class PassiveDataRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>,
    private val database: TCardioDatabase
) {

    val getLatestAvg: Flow<Double> = database.cardioDao().getLatestAvg().map {
        it?:0.0
    }

    val getLatestVal: Flow<Double> = database.cardioDao().getLastBpm().map {
        it?:0.0
    }

    val getMaxHeartBpm: Flow<Double> = database.cardioDao().getMaxBpm().map {
        it?:0.0
    }

    val passiveDataEnabled: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[PASSIVE_DATA_ENABLED] ?: false
    }

    val currentTimeNow = LocalDateTime.now()

    val cardioTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")
    val cardioFormattedTime = currentTimeNow.format(cardioTimeFormatter)

    suspend fun setPassiveDataEnabled(enabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[PASSIVE_DATA_ENABLED] = enabled
        }
    }

    val latestHeartRate: Flow<Double> = dataStore.data.map { prefs ->
        prefs[LATEST_HEART_RATE] ?: 0.0
    }
    val latestCals: Flow<Double> = dataStore.data.map { prefs ->
        prefs[LATEST_CALORIES] ?: 0.0
    }
    val latestVO2Max: Flow<Double> = dataStore.data.map { prefs ->
        prefs[LATEST_VO2_MAX] ?: 0.0
    }
    val firsttimer: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[FIRST_TIMER] ?: true
    }
    suspend fun storePrediction(par0: Double){
        withContext(Dispatchers.IO){
            database.cardioDao().insertPred(Predictions(
                timeStored =  cardioFormattedTime,
                predictionStored = par0)
            )
        }
    }

    suspend fun storeLatestData(par0: Double, par1: String) {
        dataStore.edit { prefs ->
            prefs[FIRST_TIMER] = false
            when(par1){
                "HEART_RATE_BPM" -> prefs[LATEST_HEART_RATE] = par0
                "CALORIES" -> prefs[LATEST_CALORIES] = par0
                "VO2_MAX" -> prefs[LATEST_VO2_MAX] = par0
               // "STEPS" -> prefs[LATEST_VO2_MAX] = par0
                else -> {
                    false
                }
            }

        }

        withContext(Dispatchers.IO){
            database.cardioDao().insertBpm(HeartBpm(
                timestampStore =  cardioFormattedTime,
                dataStored = par0)
            )
        }
    }

    companion object {
        const val PREFERENCES_FILENAME = "passive_data_prefs"
        private val PASSIVE_DATA_ENABLED = booleanPreferencesKey("passive_data_enabled")
        private val LATEST_HEART_RATE = doublePreferencesKey("latest_heart_rate")
        private val LATEST_CALORIES = doublePreferencesKey("latest_calories")
        private val LATEST_VO2_MAX = doublePreferencesKey("latest_vo2")
        private val FIRST_TIMER = booleanPreferencesKey("first_timer")
        /*
                DataType.HEART_RATE_BPM,
        DataType.CALORIES,
        DataType.VO2_MAX,
        DataType.STEPS

         */
    }
}

fun List<SampleDataPoint<Double>>.latestDataRate(dType: String): Double? {

    var targetDType: Any = ""

    when(dType){
        "HEART_RATE_BPM" -> targetDType = DataType.HEART_RATE_BPM
        //"VO2_MAX" -> targetDType = DataType.VO2_MAX
        "CALORIES" -> targetDType = DataType.CALORIES
        //"STEPS" -> targetDType = DataType.STEPS
        else -> {
            targetDType = "None"
        }
    }

    return this
        .filter { it.dataType == targetDType }
        .filter {
            it.accuracy == null ||
                    setOf(
                        ACCURACY_HIGH,
                        ACCURACY_MEDIUM
                    ).contains((it.accuracy as HeartRateAccuracy).sensorStatus)
        }
        .filter {
            it.value > 0
        }
        .maxByOrNull { it.timeDurationFromBoot }?.value
}

fun List<IntervalDataPoint<Double>>.latestCalRate(dType: String): Double? {

    var targetDType: Any = ""

    when(dType){
        "HEART_RATE_BPM" -> targetDType = DataType.HEART_RATE_BPM
        "CALORIES" -> targetDType = DataType.CALORIES
        "VO2_MAX" -> targetDType = DataType.VO2_MAX
        //"STEPS" -> targetDType = DataType.STEPS
        else -> {
            targetDType = "None"
        }
    }

    return this
        .filter { it.dataType == DataType.HEART_RATE_BPM }
        .filter {
            it.accuracy == null ||
                    setOf(
                        ACCURACY_HIGH,
                        ACCURACY_MEDIUM
                    ).contains((it.accuracy as HeartRateAccuracy).sensorStatus)
        }
        .filter {
            it.value > 0
        }
        .maxByOrNull { it.value }?.value
}