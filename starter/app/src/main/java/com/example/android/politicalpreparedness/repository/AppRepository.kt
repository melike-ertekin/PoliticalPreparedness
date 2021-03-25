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

    var allElections: LiveData<List<Election>> = Transformations.map(database.electionDao.getAllElections()) {
        it?.asDomainModel()
    }

    var followedElections: LiveData<List<Election>> = Transformations.map(database.electionDao.getFollowedElections()) {
        it?.asDomainModel()
    }

    suspend fun isElectionFollowed(id: Int) = database.electionDao.getFollowedElection(id) != null && database.electionDao.getFollowedElection(id) == id
    suspend fun followElection(id: Int) = database.electionDao.followElection(FollowedElection(id))
    suspend fun unfollowElection(id: Int) = database.electionDao.unfollowElection(id)

    suspend fun refreshElectionData() {

        refreshElections()

    }


    private suspend fun refreshElections() {
        withContext(Dispatchers.IO) {

            try {
                val networkElectionResponse = client.getElections()

                val networkElectionsList = networkElectionResponse.elections

                val databaseElectionsList = networkElectionsList.asDatabaseModel()

                database.electionDao.insertAllElections(*databaseElectionsList)

            } catch (e: Exception) {
                Log.d("ExceptionInRepo", e.toString())
            }


        }
    }


}