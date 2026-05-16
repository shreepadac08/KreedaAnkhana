package com.example.kreedaankhana.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.kreedaankhana.data.database.entities.Score

@Dao
interface ScoreDao {
    @Insert
    suspend fun insert(score: Score)
    
    @Query("""
        SELECT * FROM scores 
        WHERE date >= :yesterdayDate 
        ORDER BY date DESC, timestamp DESC
    """)
    fun getRecentScores(yesterdayDate: String): LiveData<List<Score>>
    
    @Query("SELECT * FROM scores ORDER BY timestamp DESC")
    fun getAllScores(): LiveData<List<Score>>
    
    @Delete
    suspend fun delete(score: Score)
}
