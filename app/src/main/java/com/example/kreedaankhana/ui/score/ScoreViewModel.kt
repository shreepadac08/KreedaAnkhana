package com.example.kreedaankhana.ui.score

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.kreedaankhana.data.database.KreedaDatabase
import com.example.kreedaankhana.data.database.entities.Score
import com.example.kreedaankhana.data.repository.ScoreRepository
import kotlinx.coroutines.launch

class ScoreViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ScoreRepository
    val recentMatches: LiveData<List<Score>>
    
    init {
        val scoreDao = KreedaDatabase.getDatabase(application).scoreDao()
        repository = ScoreRepository(scoreDao)
        recentMatches = repository.getRecentMatches()
    }
    
    fun addScore(
        team1: String,
        team2: String,
        gameType: String,
        winner: String,
        result: String,
        date: String,
        time: String,
        userId: Int
    ) {
        viewModelScope.launch {
            val score = Score(
                team1Name = team1,
                team2Name = team2,
                gameType = gameType,
                winner = winner,
                result = result,
                date = date,
                time = time,
                timestamp = System.currentTimeMillis(),
                userId = userId
            )
            repository.insert(score)
        }
    }
}
