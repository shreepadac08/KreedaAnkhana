package com.example.kreedaankhana.ui.challenge

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.kreedaankhana.data.database.KreedaDatabase
import com.example.kreedaankhana.data.database.entities.Challenge
import com.example.kreedaankhana.data.database.entities.ChallengeResponse
import com.example.kreedaankhana.data.repository.ChallengeRepository
import kotlinx.coroutines.launch

class ChallengeViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ChallengeRepository
    val openChallenges: LiveData<List<Challenge>>
    val matchedChallenges: LiveData<List<Challenge>>
    
    init {
        val challengeDao = KreedaDatabase.getDatabase(application).challengeDao()
        repository = ChallengeRepository(challengeDao)
        openChallenges = repository.openChallenges
        matchedChallenges = repository.matchedChallenges
    }
    
    fun postChallenge(
        teamName: String,
        userName: String,
        sportType: String,
        ground: String,
        date: String,
        startTime: String,
        description: String,
        userId: Int
    ) {
        viewModelScope.launch {
            val challenge = Challenge(
                teamName = teamName,
                userName = userName,
                sportType = sportType,
                ground = ground,
                date = date,
                startTime = startTime,
                description = description,
                postedById = userId,
                timestamp = System.currentTimeMillis()
            )
            repository.insert(challenge)
        }
    }
    
    fun acceptChallenge(challenge: Challenge, responderTeamName: String, responderName: String, userId: Int, responseText: String) {
        viewModelScope.launch {
            // Update challenge status
            val updatedChallenge = challenge.copy(
                status = "MATCHED",
                matchedWithTeam = responderTeamName
            )
            repository.update(updatedChallenge)

            // Insert response
            val response = ChallengeResponse(
                challengeId = challenge.id,
                responderTeamName = responderTeamName,
                responderName = responderName,
                userId = userId,
                responseText = responseText,
                timestamp = System.currentTimeMillis()
            )
            repository.insertResponse(response)
        }
    }

    fun getResponses(challengeId: Int): LiveData<List<ChallengeResponse>> {
        return repository.getResponses(challengeId)
    }
}
