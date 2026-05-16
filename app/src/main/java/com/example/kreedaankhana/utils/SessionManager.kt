package com.example.kreedaankhana.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val sharedPref: SharedPreferences = context.getSharedPreferences(
        "KreedaSession", 
        Context.MODE_PRIVATE
    )
    
    fun saveUserSession(userId: Int, userName: String) {
        with(sharedPref.edit()) {
            putInt("USER_ID", userId)
            putString("USER_NAME", userName)
            putBoolean("IS_LOGGED_IN", true)
            apply()
        }
    }
    
    fun isUserLoggedIn(): Boolean {
        return sharedPref.getBoolean("IS_LOGGED_IN", false)
    }
    
    fun getUserId(): Int {
        return sharedPref.getInt("USER_ID", -1)
    }
    
    fun getUserName(): String? {
        return sharedPref.getString("USER_NAME", null)
    }
    
    fun clearSession() {
        with(sharedPref.edit()) {
            clear()
            apply()
        }
    }
}
