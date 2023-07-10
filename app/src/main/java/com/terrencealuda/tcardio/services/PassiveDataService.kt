package com.terrencealuda.tcardio.services

import androidx.health.services.client.PassiveListenerService
import androidx.health.services.client.data.*
import com.terrencealuda.tcardio.storage.repositories.PassiveDataRepository
import com.terrencealuda.tcardio.storage.repositories.latestCalRate
import com.terrencealuda.tcardio.storage.repositories.latestDataRate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class PassiveDataService : PassiveListenerService() {
    @Inject
    lateinit var repository: PassiveDataRepository

    override fun onNewDataPointsReceived(dataPoints: DataPointContainer) {
        runBlocking {
            dataPoints.getData(DataType.HEART_RATE_BPM).latestDataRate("HEART_RATE_BPM")?.let {
                repository.storeLatestData(it,"HEART_RATE_BPM")
            }

           /* dataPoints.getData(DataType.VO2_MAX).latestDataRate("VO2_MAX")?.let {
                repository.storeLatestData(it,"VO2_MAX")
            }*/

            dataPoints.getData(DataType.CALORIES ).latestCalRate("CALORIES")?.let {
                repository.storeLatestData(it,"CALORIES")
            }

        }
    }

}
