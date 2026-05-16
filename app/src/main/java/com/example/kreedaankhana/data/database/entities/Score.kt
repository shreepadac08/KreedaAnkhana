package com.example.kreedaankhana.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scores")
data class Score(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    
    @ColumnInfo(name = "team1_name")
    val team1Name: String,
    
    @ColumnInfo(name = "team2_name")
    val team2Name: String,
    
    @ColumnInfo(name = "game_type")
    val gameType: String,
    
    @ColumnInfo(name = "winner")
    val winner: String,
    
    @ColumnInfo(name = "result")
    val result: String,
    
    @ColumnInfo(name = "date")
    val date: String,
    
    @ColumnInfo(name = "time")
    val time: String,
    
    @ColumnInfo(name = "timestamp")
    val timestamp: Long,
    
    @ColumnInfo(name = "user_id")
    val userId: Int
)
