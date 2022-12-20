package com.terrencealuda.tcardio.storage.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [HeartBpm::class, Predictions::class], version = 1, exportSchema = false)
abstract class TCardioDatabase : RoomDatabase() {

    abstract fun cardioDao(): CardioDao

    companion object{
        @Volatile
        private var DBINSTANCE: TCardioDatabase? = null

        fun getDatabase(context: Context): TCardioDatabase{
            return DBINSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TCardioDatabase::class.java,
                    "tcardio_database"
                ) .fallbackToDestructiveMigration()
                    .build()
                DBINSTANCE = instance
                return instance
            }
        }
    }

}