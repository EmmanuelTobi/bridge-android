package com.bridge.androidtechnicaltest.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [PupilEntity::class], version = 1, exportSchema = false)
abstract class PupilDatabase : RoomDatabase() {
    abstract fun pupilDao(): PupilDao

    companion object {
        @Volatile
        private var sInstance: PupilDatabase? = null

        fun getDatabase(context: Context): PupilDatabase {
            return sInstance ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PupilDatabase::class.java,
                    "pupil_database"
                ).build()
                sInstance = instance
                instance
            }
        }
    }
}