package com.devinou971.minesweeperandroid.storageclasses

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface GameDataDAO {
    @Insert
    fun insertGameData(gameData: GameData)

    @Delete
    fun deleteGameData(gameData: GameData)

    @Query("SELECT * FROM GameData")
    fun getAllGameData() : Cursor

    @Query("SELECT * FROM GameData WHERE _id=:id")
    fun getOneGameData(id: Int) : GameData

    @Query("SELECT * FROM GameData WHERE game_type=:difficulty ORDER BY time ASC LIMIT 1")
    fun getBestTimeForDifficulty(difficulty: Int) : GameData?

    @get:Query("SELECT NOT EXISTS(select 1 from GameData LIMIT 1)")
    val isFirstGame: Boolean
}