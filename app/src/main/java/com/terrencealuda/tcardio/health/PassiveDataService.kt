package com.terrencealuda.tcardio.health

import androidx.health.services.client.PassiveListenerService
import androidx.health.services.client.data.*
import com.terrencealuda.tcardio.repository.PassiveDataRepository
import com.terrencealuda.tcardio.repository.latestDataRate
import com.terrencealuda.tcardio.repository.latestIntervalDataPointSum
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

            dataPoints.getData(DataType.CALORIES_DAILY ).latestIntervalDataPointSum("CALORIES")?.let {
                repository.storeLatestData(it,"CALORIES")
            }

            dataPoints.getData(DataType.STEPS_DAILY).latestIntervalDataPointSum("CALORIES")?.let{
                repository.storeLatestLongData(it, "STEPS_DAILY")
            }
           /*dataPoints.getData(DataType.STEPS).latestDataRate("STEPS")?.let {
                repository.storeLatestData(it,"STEPS")
            }*/



        }
    }

}
