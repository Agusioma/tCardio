package com.terrencealuda.tcardio.presentation.summary

import java.time.Duration


data class SummaryScreenState(
    val averageHeartRate: Double,
    val totalDistance: Double,
    val totalCalories: Double,
    val elapsedTime: Duration,
)