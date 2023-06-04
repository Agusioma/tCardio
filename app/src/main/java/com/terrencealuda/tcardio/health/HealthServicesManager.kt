package com.terrencealuda.tcardio.health

import android.util.Log
import androidx.concurrent.futures.await
import androidx.health.services.client.HealthServicesClient
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.PassiveListenerConfig
import com.terrencealuda.tcardio.daggerhilt.TAG
import javax.inject.Inject

/**
 * Entry point for [HealthServicesClient] APIs. This also provides suspend functions around
 * those APIs to enable use in coroutines.
 */
class HealthServicesManager @Inject constructor(
    healthServicesClient: HealthServicesClient
) {
    private val passiveMonitoringClient = healthServicesClient.passiveMonitoringClient
    private val dataTypes = setOf(
        DataType.HEART_RATE_BPM,
        DataType.CALORIES_DAILY,
        DataType.STEPS_DAILY
        /*DataType.CALORIES
         DataType.VO2_MAX,
        DataType.STEPS*/)

    suspend fun hasHealthDataCapability(dataType: String): Boolean {
        val capabilities = passiveMonitoringClient.getCapabilitiesAsync().await()
        var targetDType: Any = ""
        val supportedDTypes = capabilities.supportedDataTypesPassiveMonitoring
        when(dataType){
            "HEART_RATE_BPM" -> targetDType = DataType.HEART_RATE_BPM
            "CALORIES" -> targetDType = DataType.CALORIES
            "STEPS_DAILY" -> targetDType = DataType.STEPS_DAILY
            else -> {
                targetDType = "None"
            }
        }
        return (targetDType in supportedDTypes)
    }

    suspend fun registerForHeartRateData() {
        val passiveListenerConfig = PassiveListenerConfig.builder()
            .setDataTypes(dataTypes)
            .build()

        Log.i(TAG, "Registering listener")
        passiveMonitoringClient.setPassiveListenerServiceAsync(
            PassiveDataService::class.java,
            passiveListenerConfig
        ).await()
    }

    suspend fun unregisterForHeartRateData() {
        Log.i(TAG, "Unregistering listeners")
        passiveMonitoringClient.clearPassiveListenerServiceAsync().await()
    }
}
