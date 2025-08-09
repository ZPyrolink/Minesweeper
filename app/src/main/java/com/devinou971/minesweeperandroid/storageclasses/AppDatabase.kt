package com.devinou971.minesweeperandroid.storageclasses

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [GameData::class], version = AppDatabase.VERSION)
abstract class AppDatabase : RoomDatabase() {
    abstract val gameDataDAO: GameDataDAO

    companion object { // This companion has the singleton
        const val NAME: String = "MineSweeperDatabase"
        const val VERSION: Int = 1

        @Volatile
        private var instance: AppDatabase? = null

        val Context.appDatabase: AppDatabase
            get() {
                if (instance == null)
                    buildAppDb()

                return instance!!
            }

        private fun Context.buildAppDb() = synchronized(AppDatabase) {
            if (instance != null)
                return

            instance = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java,
                NAME
            ).build()
        }
    }
}