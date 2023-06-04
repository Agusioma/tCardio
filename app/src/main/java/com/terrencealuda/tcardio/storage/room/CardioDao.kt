package com.terrencealuda.tcardio.storage.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CardioDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBpm(heartBpm: HeartBpm)

    @Insert
    suspend fun insertPred(prediction: Predictions)


    @Query("SELECT * FROM heart_bpm ORDER BY timestamp DESC LIMIT 10")
    fun getRecentTenBpm(): Flow<List<HeartBpm>>

    @Query("SELECT predictionStored FROM predictions ORDER BY timeStored DESC LIMIT 10")
    fun getRecentTenPreds(): Flow<List<Double>>

    /*@Query("SELECT * FROM predictions ORDER BY timeStored DESC LIMIT 1")
    fun getRecentPrediction(): Flow<List<Predictions>>*/

    @Query("SELECT AVG(data) FROM heart_bpm ORDER BY timestamp DESC LIMIT 10")
    fun getLatestAvg(): Flow<Double>

    @Query("SELECT data FROM heart_bpm ORDER BY timestamp DESC LIMIT 1")
    fun getLastBpm(): Flow<Double>

    @Query("SELECT AVG(data) FROM heart_bpm ORDER BY timestamp DESC")
    fun getOverallAvg():Flow<Double>

    @Query("SELECT MAX(data) FROM heart_bpm ORDER BY timestamp DESC")
    fun getMaxBpm():Flow<Double>

}
