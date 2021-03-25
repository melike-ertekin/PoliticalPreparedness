package com.example.android.politicalpreparedness.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.android.politicalpreparedness.domain.Election
import com.example.android.politicalpreparedness.domain.VoterInfo
import com.example.android.politicalpreparedness.repository.database.ElectionDatabase
import com.example.android.politicalpreparedness.repository.database.FollowedElection
import com.example.android.politicalpreparedness.repository.database.asDomainModel
import com.example.android.politicalpreparedness.repository.network.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class AppRepository(private val database: ElectionDatabase) {

    var client: CivicsApiService = CivicsApi.retrofitService

    /**
     * savedElections that can be shown on the screen.
     */
    /*val followedElections: LiveData<List<Election>> =
            Transformations.map(database.electionDao.getSavedElections()) {
                it?.asDomainModelForElection()
            }
*/
    var allElections: LiveData<List<Election>> = Transformations.map(database.electionDao.getAllElections()) {
        it?.asDomainModel()
    }

    var followedElections: LiveData<List<Election>> = Transformations.map(database.electionDao.getFollowedElections()) {
        it?.asDomainModel()
    }

    suspend fun isElectionFollowed(id: Int) = database.electionDao.getFollowedElection(id) != null && database.electionDao.getFollowedElection(id) == id
    suspend fun followElection(id: Int) = database.electionDao.followElection(FollowedElection(id))
    suspend fun unfollowElection(id: Int) = database.electionDao.unfollowElection(id)

    suspend fun refreshElectionData(){

        refreshElections()

    }



    /**
     * Refresh the elections stored in the offline cache.
     *
     * This function uses the IO dispatcher to ensure the database insert database operation
     * happens on the IO dispatcher. By switching to the IO dispatcher using `withContext` this
     * function is now safe to call from any thread including the Main thread.
     *
     * To actually load the asteroids for use, observe [allAsteroids]
     */
    private suspend fun refreshElections() {
        withContext(Dispatchers.IO) {

            try {
                val networkElectionResponse = client.getElections()

                val networkElectionsList = networkElectionResponse.elections
                Log.d("hyee", networkElectionsList.size.toString())
                val databaseElectionsList = networkElectionsList.asDatabaseModel()
                Log.d("hye", databaseElectionsList.size.toString())
                database.electionDao.insertAllElections(*databaseElectionsList)

            } catch (e: Exception) {
                Log.d("ExceptionInRepo1",e.toString())
            }


        }
    }





}