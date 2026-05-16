package com.example.kreedaankhana.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "challenge_responses")
data class ChallengeResponse(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    
    @ColumnInfo(name = "challenge_id")
    val challengeId: Int,
    
    @ColumnInfo(name = "responder_team_name")
    val responderTeamName: String,
    
    @ColumnInfo(name = "responder_name")
    val responderName: String,
    
    @ColumnInfo(name = "user_id")
    val userId: Int,
    
    @ColumnInfo(name = "response_text")
    val responseText: String,
    
    @ColumnInfo(name = "timestamp")
    val timestamp: Long
)
