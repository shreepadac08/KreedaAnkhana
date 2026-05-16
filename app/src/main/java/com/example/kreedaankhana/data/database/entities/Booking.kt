package com.example.kreedaankhana.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookings")
data class Booking(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    
    @ColumnInfo(name = "team_name")
    val teamName: String,

    @ColumnInfo(name = "ground_name")
    val groundName: String,
    
    @ColumnInfo(name = "date")
    val date: String,  // Format: yyyy-MM-dd
    
    @ColumnInfo(name = "start_time")
    val startTime: String,  // Format: HH:mm
    
    @ColumnInfo(name = "end_time")
    val endTime: String,  // Format: HH:mm
    
    @ColumnInfo(name = "user_id")
    val userId: Int
)
