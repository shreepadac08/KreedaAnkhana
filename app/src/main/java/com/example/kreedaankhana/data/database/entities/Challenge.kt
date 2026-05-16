package com.example.kreedaankhana.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "challenges")
data class Challenge(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    
    @ColumnInfo(name = "team_name")
    val teamName: String,
    
    @ColumnInfo(name = "user_name")
    val userName: String,
    
    @ColumnInfo(name = "sport_type")
    val sportType: String,
    
    @ColumnInfo(name = "ground")
    val ground: String,
    
    @ColumnInfo(name = "date")
    val date: String, // yyyy-MM-dd
    
    @ColumnInfo(name = "start_time")
    val startTime: String, // HH:mm
    
    @ColumnInfo(name = "description")
    val description: String,
    
    @ColumnInfo(name = "status")
    val status: String = "OPEN", // OPEN, MATCHED
    
    @ColumnInfo(name = "matched_with_team")
    val matchedWithTeam: String? = null,
    
    @ColumnInfo(name = "posted_by_id")
    val postedById: Int,
    
    @ColumnInfo(name = "timestamp")
    val timestamp: Long
)
