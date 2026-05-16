package com.example.kreedaankhana.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.kreedaankhana.data.database.KreedaDatabase
import com.example.kreedaankhana.data.database.entities.User
import com.example.kreedaankhana.data.repository.UserRepository
import com.example.kreedaankhana.utils.PasswordUtils
import com.example.kreedaankhana.utils.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: UserRepository
    private val sessionManager: SessionManager
    
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    init {
        val userDao = KreedaDatabase.getDatabase(application).userDao()
        repository = UserRepository(userDao)
        sessionManager = SessionManager(application)
    }

    fun signUp(name: String, email: String, password: String) {
        val trimmedEmail = email.trim()
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(trimmedEmail).matches() || !trimmedEmail.endsWith("@gmail.com")) {
            _authState.value = AuthState.Error("Please enter a valid Gmail address (e.g., user@gmail.com)")
            return
        }

        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val existingUser = repository.findByEmail(trimmedEmail)
            if (existingUser != null) {
                _authState.value = AuthState.Error("Email already registered")
                return@launch
            }
            val hashedPassword = PasswordUtils.hashPassword(password)
            val user = User(name = name, email = trimmedEmail, password = hashedPassword)
            repository.insert(user)
            _authState.value = AuthState.Success("Registration successful! Please login")
        }
    }

    fun login(email: String, password: String) {
        val trimmedEmail = email.trim()
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val hashedPassword = PasswordUtils.hashPassword(password)
            val user = repository.login(trimmedEmail, hashedPassword)
            if (user != null) {
                sessionManager.saveUserSession(user.id, user.name)
                _authState.value = AuthState.LoggedIn
            } else {
                _authState.value = AuthState.Error("Invalid email or password")
            }
        }
    }
}

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val message: String) : AuthState()
    data class Error(val message: String) : AuthState()
    object LoggedIn : AuthState()
}
