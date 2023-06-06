package com.terrencealuda.tcardio.health

import android.util.Log
import androidx.health.services.client.PassiveListenerService
import androidx.health.services.client.data.*
import androidx.health.services.client.data.DataType.Companion.CALORIES
import com.terrencealuda.tcardio.repository.PassiveDataRepository
import com.terrencealuda.tcardio.repository.latestDataRate
import com.terrencealuda.tcardio.repository.latestIntervalDataPointSum
import com.terrencealuda.tcardio.repository.latestIntervalLongDataPointSum
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class PassiveDataService : PassiveListenerService() {
    @Inject
    lateinit var repository: PassiveDataRepository

    override fun onNewDataPointsReceived(dataPoints: DataPointContainer) {
        runBlocking {
            Log.i("THE SET!", dataPoints.dataTypes.toString())
            dataPoints.getData(CALORIES).map {
                Log.i("TEST1!", it.value.toString())
            }
            dataPoints.getData(DataType.HEART_RATE_BPM).latestDataRate("HEART_RATE_BPM")?.let {
                repository.storeLatestData(it,"HEART_RATE_BPM")
                Log.i("STORED HBP", it.toString())
            }
            //Log.i("STORED HBP", "STORED")
           dataPoints.getData(DataType.CALORIES_DAILY ).latestIntervalDataPointSum("CALORIES_DAILY")?.let {
                repository.storeLatestData(it,"CALORIES_DAILY")
               Log.i("STORED KCALS", it.toString())
            }
            //Log.i("STORED KCALS", "STORED")
            dataPoints.getData(DataType.STEPS_DAILY).latestIntervalLongDataPointSum("STEPS_DAILY")?.let{
                repository.storeLatestLongData(it, "STEPS_DAILY")
                Log.i("STORED STEPS", it.toString())
            }
            //Log.i("STORED STEPS", "STORED")
           /*dataPoints.getData(DataType.STEPS).latestDataRate("STEPS")?.let {
                repository.storeLatestData(it,"STEPS")
            }*/



        }
    }


}
