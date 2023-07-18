package com.terrencealuda.tcardio.presentation.exercise

import com.terrencealuda.tcardio.presentation.summary.SummaryScreenState
import com.terrencealuda.tcardio.services.ExerciseServiceState
import com.terrencealuda.tcardio.storage.repositories.ServiceState
import java.time.Duration

data class ExerciseScreenState(
    val hasExerciseCapabilities: Boolean,
    val isTrackingAnotherExercise: Boolean,
    val serviceState: ServiceState,
    val exerciseState: ExerciseServiceState?
) {
    fun toSummary(): SummaryScreenState {
        val exerciseMetrics = exerciseState?.exerciseMetrics
        val averageHeartRate = exerciseMetrics?.heartRateAverage ?: Double.NaN
        val totalDistance = exerciseMetrics?.distance ?: 0.0
        val totalCalories = exerciseMetrics?.calories ?: Double.NaN
        val duration = exerciseState?.activeDurationCheckpoint?.activeDuration ?: Duration.ZERO
        return SummaryScreenState(averageHeartRate, totalDistance, totalCalories, duration)
    }

    val isEnding: Boolean
        get() = exerciseState?.exerciseState?.isEnding == true

    val isEnded: Boolean
        get() = exerciseState?.exerciseState?.isEnded == true

    val isPaused: Boolean
        get() = exerciseState?.exerciseState?.isPaused == true
}