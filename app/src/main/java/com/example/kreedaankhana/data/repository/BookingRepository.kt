package com.example.kreedaankhana.data.repository

import androidx.lifecycle.LiveData
import com.example.kreedaankhana.data.database.dao.BookingDao
import com.example.kreedaankhana.data.database.entities.Booking

class BookingRepository(private val bookingDao: BookingDao) {
    val allBookings: LiveData<List<Booking>> = bookingDao.getAllBookings()
    
    suspend fun insert(booking: Booking) = bookingDao.insert(booking)
    
    suspend fun checkSlotAvailability(
        groundName: String,
        date: String,
        startTime: String,
        endTime: String
    ): Boolean {
        val conflicts = bookingDao.checkConflict(groundName, date, startTime, endTime)
        return conflicts.isEmpty()
    }
    
    suspend fun delete(booking: Booking) = bookingDao.delete(booking)
}
