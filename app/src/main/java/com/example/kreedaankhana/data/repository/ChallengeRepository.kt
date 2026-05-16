package com.example.kreedaankhana.data.repository

import androidx.lifecycle.LiveData
import com.example.kreedaankhana.data.database.dao.ChallengeDao
import com.example.kreedaankhana.data.database.entities.Challenge
import com.example.kreedaankhana.data.database.entities.ChallengeResponse

class ChallengeRepository(private val challengeDao: ChallengeDao) {
    val openChallenges: LiveData<List<Challenge>> = challengeDao.getOpenChallenges()
    val matchedChallenges: LiveData<List<Challenge>> = challengeDao.getMatchedChallenges()
    
    suspend fun insert(challenge: Challenge): Long = challengeDao.insert(challenge)
    suspend fun update(challenge: Challenge) = challengeDao.updateChallenge(challenge)
    suspend fun getChallengeById(challengeId: Int): Challenge? = challengeDao.getChallengeById(challengeId)
    
    suspend fun insertResponse(response: ChallengeResponse) = challengeDao.insertResponse(response)
    fun getResponses(challengeId: Int): LiveData<List<ChallengeResponse>> = challengeDao.getResponses(challengeId)
    
    suspend fun deleteChallenge(challenge: Challenge) = challengeDao.deleteChallenge(challenge)
}
