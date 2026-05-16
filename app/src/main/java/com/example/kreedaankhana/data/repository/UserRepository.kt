package com.example.kreedaankhana.data.repository

import com.example.kreedaankhana.data.database.dao.UserDao
import com.example.kreedaankhana.data.database.entities.User

class UserRepository(private val userDao: UserDao) {
    suspend fun insert(user: User): Long = userDao.insert(user)
    suspend fun login(email: String, password: String): User? = userDao.login(email, password)
    suspend fun findByEmail(email: String): User? = userDao.findByEmail(email)
    suspend fun getUserById(userId: Int): User? = userDao.getUserById(userId)
}
