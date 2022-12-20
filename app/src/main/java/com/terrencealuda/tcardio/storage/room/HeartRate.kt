package com.terrencealuda.tcardio.storage.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "heart_bpm")
data class HeartBpm(
    @PrimaryKey(autoGenerate = true) var id: Int=0,
    @ColumnInfo(name = "timestamp") val timestampStore: String,
    @ColumnInfo(name = "data") val dataStored: Double
)

@Entity(tableName = "predictions")
data class Predictions(
   @PrimaryKey(autoGenerate = true)
    var id: Int=0,
    @ColumnInfo(name = "timeStored") val timeStored: String,
    @ColumnInfo(name = "predictionStored") val predictionStored: Double
)