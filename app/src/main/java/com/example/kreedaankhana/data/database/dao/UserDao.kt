package com.example.kreedaankhana.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.kreedaankhana.data.database.entities.User

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User): Long
    
    @Query("SELECT * FROM users WHERE email = :email AND password = :password")
    suspend fun login(email: String, password: String): User?
    
    @Query("SELECT * FROM users WHERE email = :email")
    suspend fun findByEmail(email: String): User?
    
    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: Int): User?
}
