package com.example.kreedaankhana.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.kreedaankhana.data.database.entities.Challenge
import com.example.kreedaankhana.data.database.entities.ChallengeResponse

@Dao
interface ChallengeDao {
    @Insert
    suspend fun insert(challenge: Challenge): Long

    @Update
    suspend fun updateChallenge(challenge: Challenge)
    
    @Query("""
        SELECT * FROM challenges 
        WHERE status = 'OPEN' 
        AND (date > date('now', 'localtime') OR (date = date('now', 'localtime') AND start_time > strftime('%H:%M', 'now', 'localtime')))
        ORDER BY date ASC, start_time ASC
    """)
    fun getOpenChallenges(): LiveData<List<Challenge>>

    @Query("""
        SELECT * FROM challenges 
        WHERE status = 'MATCHED' 
        AND (date > date('now', 'localtime') OR (date = date('now', 'localtime') AND start_time > strftime('%H:%M', 'now', 'localtime')))
        ORDER BY date ASC, start_time ASC
    """)
    fun getMatchedChallenges(): LiveData<List<Challenge>>
    
    @Query("SELECT * FROM challenges WHERE id = :challengeId")
    suspend fun getChallengeById(challengeId: Int): Challenge?
    
    @Insert
    suspend fun insertResponse(response: ChallengeResponse)
    
    @Query("SELECT * FROM challenge_responses WHERE challenge_id = :challengeId ORDER BY timestamp ASC")
    fun getResponses(challengeId: Int): LiveData<List<ChallengeResponse>>
    
    @Delete
    suspend fun deleteChallenge(challenge: Challenge)
}
