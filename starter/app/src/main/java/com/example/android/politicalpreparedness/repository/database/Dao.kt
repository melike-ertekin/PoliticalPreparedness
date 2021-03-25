package com.example.android.politicalpreparedness.repository.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import retrofit2.http.DELETE

@Dao
interface ElectionDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllElections(vararg elections: DatabaseElection)

    @Query("SELECT * FROM elections")
    fun getAllElections(): LiveData<List<DatabaseElection>>  // No need for a suspend function since LiveData is already asynchronous.

   @Query("SELECT * FROM elections INNER JOIN followed_elections ON elections.id = followed_elections.id ")
   fun getFollowedElections():LiveData<List<DatabaseElection>>

    @Query("SELECT elections.id FROM elections WHERE id =:id AND  id IN followed_elections ")
    suspend fun getFollowedElection(id: Int): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun followElection(id: FollowedElection)

    @Query("DELETE FROM followed_elections WHERE id = :id")
    suspend fun unfollowElection(id: Int)


}

