package com.devinou971.minesweeperandroid.storageclasses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.devinou971.minesweeperandroid.utils.Difficulty

@Entity
class GameData(
    @PrimaryKey(autoGenerate = true) var _id: Int?,
    @ColumnInfo(name = "time") var time: Int,
    difficulty: Difficulty
) {
    @ColumnInfo(name = "game_type")
    val gameType: Int

    init {
        gameType = difficulty.ordinal
    }

    override fun toString(): String {
        return "GameData(_id=$_id, time=$time, gameType=$gameType)"
    }
}