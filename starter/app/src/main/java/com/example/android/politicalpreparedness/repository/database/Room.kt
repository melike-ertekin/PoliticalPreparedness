package com.example.android.politicalpreparedness.repository.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.android.politicalpreparedness.domain.Election

@Database(entities = [DatabaseElection::class, FollowedElection::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ElectionDatabase: RoomDatabase() {

    abstract val electionDao: ElectionDao

    //companion object allows clients to access the methods for creating or getting the database without instantiating the class
    companion object{

        //@Volatile annotation means value of variable is always up to date and same to all execution threads
        //Value of a volatile variable will never be cached and all writes and reads will be done to and from the main memory
        //It means changes made by one thread to INSTANCE are visible to all other threads immediately.

        @Volatile
        private var INSTANCE: ElectionDatabase? = null

        fun getDatabase(context: Context): ElectionDatabase {
            synchronized(this){
                var instance = INSTANCE

                if(instance == null){
                    instance = Room.databaseBuilder(
                            context.applicationContext,
                            ElectionDatabase::class.java,
                            "election_database")
                            .fallbackToDestructiveMigration()
                            .build()
                    INSTANCE =instance
                }

                return instance
            }
        }
    }

}