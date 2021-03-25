package com.example.android.politicalpreparedness.election

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.android.politicalpreparedness.domain.Election
import com.example.android.politicalpreparedness.repository.AppRepository
import com.example.android.politicalpreparedness.repository.database.ElectionDatabase.Companion.getDatabase
import kotlinx.coroutines.launch

/**
 * ElectionsViewModel designed to store and manage UI-related data in a lifecycle conscious way. This
 * allows data to survive configuration changes such as screen rotations. In addition, background
 * work such as fetching network results can continue through configuration changes and deliver
 * results after the new Fragment or Activity is available.
 *
 * @param application The application that this viewmodel is attached to, it's safe to hold a
 * reference to applications across rotation since Application is never recreated during activity
 * or fragment lifecycle events.
 */


//Provides election datasource
class ElectionsViewModel(application: Application): AndroidViewModel(application) {

    private val database = getDatabase(application)

    private val appRepository: AppRepository = AppRepository(database)

    val allElections = appRepository.allElections

    val followedElections = appRepository.followedElections

    /**
     * init{} is called immediately when this ViewModel is created.
     */
    init {

        viewModelScope.launch {

            appRepository.refreshElectionData()

        }

    }

    private val _navigateToSelectedElection = MutableLiveData<Election>()

    val navigateToSelectedElection: LiveData<Election>
        get() = _navigateToSelectedElection

    fun displayElectionDetails(election: Election) {
        _navigateToSelectedElection.value = election
    }
    fun displayElectionDetailsComplete() {
        _navigateToSelectedElection.value = null
    }


    /**
     * Factory for constructing ElectionsViewModel with parameter
     */
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ElectionsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ElectionsViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

}