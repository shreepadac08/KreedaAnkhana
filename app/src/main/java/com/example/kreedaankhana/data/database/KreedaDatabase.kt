package com.example.kreedaankhana.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.kreedaankhana.data.database.dao.BookingDao
import com.example.kreedaankhana.data.database.dao.ChallengeDao
import com.example.kreedaankhana.data.database.dao.ScoreDao
import com.example.kreedaankhana.data.database.dao.UserDao
import com.example.kreedaankhana.data.database.entities.Booking
import com.example.kreedaankhana.data.database.entities.Challenge
import com.example.kreedaankhana.data.database.entities.ChallengeResponse
import com.example.kreedaankhana.data.database.entities.Score
import com.example.kreedaankhana.data.database.entities.User

@Database(
    entities = [
        User::class,
        Booking::class,
        Challenge::class,
        ChallengeResponse::class,
        Score::class
    ],
    version = 4,
    exportSchema = false
)
abstract class KreedaDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun bookingDao(): BookingDao
    abstract fun challengeDao(): ChallengeDao
    abstract fun scoreDao(): ScoreDao
    
    companion object {
        @Volatile
        private var INSTANCE: KreedaDatabase? = null
        
        fun getDatabase(context: Context): KreedaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    KreedaDatabase::class.java,
                    "kreeda_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
