package com.devinou971.minesweeperandroid.storageclasses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.devinou971.minesweeperandroid.utils.Difficulty

@Entity
class GameData {
    @PrimaryKey(autoGenerate = true)
    var _id: Int?

    @ColumnInfo(name = "time")
    var time: Int

    @ColumnInfo(name = "game_type")
    var gameType: Int

    constructor(id: Int?, time: Int, gameType: Int) {
        _id = id
        this.time = time;
        this.gameType = gameType
    }

    constructor(id: Int?, time: Int, difficulty: Difficulty) : this(id, time, difficulty.id)

    override fun toString(): String {
        return "GameData(_id=$_id, time=$time, gameType=$gameType)"
    }
}