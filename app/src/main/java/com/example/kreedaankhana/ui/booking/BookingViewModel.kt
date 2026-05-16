package com.example.kreedaankhana.ui.booking

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kreedaankhana.data.database.KreedaDatabase
import com.example.kreedaankhana.data.database.entities.Booking
import com.example.kreedaankhana.data.repository.BookingRepository
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class BookingViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: BookingRepository
    val allBookings: LiveData<List<Booking>>
    
    private val _bookingStatus = MutableLiveData<String>()
    val bookingStatus: LiveData<String> = _bookingStatus
    
    init {
        val bookingDao = KreedaDatabase.getDatabase(application).bookingDao()
        repository = BookingRepository(bookingDao)
        allBookings = repository.allBookings
    }
    
    fun bookSlot(
        teamName: String,
        groundName: String,
        date: String,
        startTime: String,
        endTime: String,
        userId: Int
    ) {
        viewModelScope.launch {
            if (teamName.isBlank()) {
                _bookingStatus.value = "Team name is required"
                return@launch
            }

            if (groundName.isBlank()) {
                _bookingStatus.value = "Ground name is required"
                return@launch
            }
            
            if (!isValidTimeRange(startTime, endTime)) {
                _bookingStatus.value = "End time must be after start time"
                return@launch
            }
            
            val isAvailable = repository.checkSlotAvailability(groundName, date, startTime, endTime)
            if (!isAvailable) {
                _bookingStatus.value = "This ground is already booked for this slot!"
                return@launch
            }
            
            val booking = Booking(
                teamName = teamName,
                groundName = groundName,
                date = date,
                startTime = startTime,
                endTime = endTime,
                userId = userId
            )
            repository.insert(booking)
            _bookingStatus.value = "Slot booked successfully!"
        }
    }
    
    private fun isValidTimeRange(start: String, end: String): Boolean {
        val format = SimpleDateFormat("HH:mm", Locale.getDefault())
        return try {
            val startTime = format.parse(start)!!
            val endTime = format.parse(end)!!
            endTime.after(startTime)
        } catch (e: Exception) {
            false
        }
    }
}
