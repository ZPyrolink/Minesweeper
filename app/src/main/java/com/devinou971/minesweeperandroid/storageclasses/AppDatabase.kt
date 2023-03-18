package com.devinou971.minesweeperandroid.storageclasses

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// This is the singleton that will be calling the database
@Database(entities = [GameData::class], version = AppDatabase.VERSION)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gameDataDAO(): GameDataDAO // This will have hte GameDataDao at the end

    companion object { // This companion has the singleton
        public const val NAME: String = "MineSweeperDatabase"
        public const val VERSION: Int = 1

        @Volatile
        private var instance: AppDatabase? = null

        fun getAppDataBase(context: Context): AppDatabase {
            if (instance == null) {
                synchronized(AppDatabase::class) {
                    if (instance == null) {
                        instance = Room.databaseBuilder(
                            context.applicationContext,
                            AppDatabase::class.java,
                            NAME
                        ).build()
                    }
                }
            }

            return this.instance!!
        }
    }
}