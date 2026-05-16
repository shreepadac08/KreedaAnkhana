package com.example.kreedaankhana.data.repository

import androidx.lifecycle.LiveData
import com.example.kreedaankhana.data.database.dao.ScoreDao
import com.example.kreedaankhana.data.database.entities.Score
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ScoreRepository(private val scoreDao: ScoreDao) {
    fun getRecentMatches(): LiveData<List<Score>> {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            .format(Date())
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        val yesterday = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            .format(calendar.time)
        
        return scoreDao.getRecentScores(yesterday)
    }
    
    val allScores: LiveData<List<Score>> = scoreDao.getAllScores()
    
    suspend fun insert(score: Score) = scoreDao.insert(score)
    suspend fun delete(score: Score) = scoreDao.delete(score)
}
