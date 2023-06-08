package com.terrencealuda.tcardio.health

import com.google.android.libraries.healthdata.HealthDataClient
import com.google.android.libraries.healthdata.data.CumulativeAggregationSpec
import com.google.android.libraries.healthdata.data.IntervalDataTypes
import com.google.android.libraries.healthdata.data.ReadAggregatedDataRequest
import com.google.android.libraries.healthdata.data.ReadAggregatedDataResponse
import com.google.android.libraries.healthdata.data.TimeSpec
import com.google.common.util.concurrent.ListenableFuture
import java.time.LocalDateTime
import java.time.LocalTime


class StepsReader internal constructor(private val healthDataClient: HealthDataClient?) {
    @Throws(StepsReaderException::class)
    fun readAggregatedData(): ListenableFuture<ReadAggregatedDataResponse> {
        if (healthDataClient == null) {
            throw StepsReaderException("health client is null")
        }
        val readAggregatedDataRequest = ReadAggregatedDataRequest.builder()
            .setTimeSpec(
                TimeSpec.builder()
                    .setStartLocalDateTime(LocalDateTime.now().with(LocalTime.MIDNIGHT))
                    .build()
            )
            .addCumulativeAggregationSpec(
                CumulativeAggregationSpec.builder(IntervalDataTypes.STEPS).build()
            )
            .build()
        return healthDataClient.readAggregatedData(readAggregatedDataRequest)
    }

    fun readStepsFromAggregatedDataResponse(result: ReadAggregatedDataResponse?): Long {
        var steps = 0L
        if (result != null) {
            val cumulativeDataList = result.cumulativeDataList
            if (!cumulativeDataList.isEmpty()) {
                for (cumulativeData in cumulativeDataList) {
                    steps += cumulativeData.total!!.longValue
                }
            }
        }
        return steps
    }
}
