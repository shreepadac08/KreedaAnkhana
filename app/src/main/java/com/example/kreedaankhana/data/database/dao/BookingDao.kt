package com.example.kreedaankhana.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.kreedaankhana.data.database.entities.Booking

@Dao
interface BookingDao {
    @Insert
    suspend fun insert(booking: Booking)
    
    @Query("""
        SELECT * FROM bookings 
        WHERE date > date('now', 'localtime') 
        OR (date = date('now', 'localtime') AND end_time > strftime('%H:%M', 'now', 'localtime'))
        ORDER BY date ASC, start_time ASC
    """)
    fun getAllBookings(): LiveData<List<Booking>>
    
    @Query("""
        SELECT * FROM bookings 
        WHERE ground_name = :groundName
        AND date = :date 
        AND start_time < :endTime 
        AND end_time > :startTime
    """)
    suspend fun checkConflict(
        groundName: String,
        date: String, 
        startTime: String, 
        endTime: String
    ): List<Booking>
    
    @Delete
    suspend fun delete(booking: Booking)
}
