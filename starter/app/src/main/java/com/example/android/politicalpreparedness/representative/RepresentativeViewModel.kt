package com.example.android.politicalpreparedness.representative

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.domain.Address
import com.example.android.politicalpreparedness.domain.Representative
import com.example.android.politicalpreparedness.repository.network.CivicsApi
import com.example.android.politicalpreparedness.repository.network.CivicsApiService
import com.example.android.politicalpreparedness.repository.network.representatives
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class RepresentativeViewModel: ViewModel() {

    var client: CivicsApiService = CivicsApi.retrofitService

    private val _address = MutableLiveData<Address>()
    val address: LiveData<Address>
        get() = _address

    private val _representatives = MutableLiveData<List<Representative>>()
    val representatives: LiveData<List<Representative>>
        get() = _representatives


    fun updateAddress(address: Address){
        _address.postValue(address)
        Log.d("selected address", address.toFormattedString())
    }
    fun findMyRepresentative(address: Address){
        getRepresentatives(address)
        Log.d("selected address", address.toFormattedString())

    }

    private fun getRepresentatives(address: Address)  = viewModelScope.launch(Dispatchers.IO) {


        try {
            val networkRepresentativeResponse = client.getRepresentativeInfoByAddress(address.toFormattedString())

            val representatives = networkRepresentativeResponse.representatives

            _representatives.postValue(representatives)


        } catch (e: Exception) {

            Log.d("ExceptionInGetRepresentatives",e.toString())
        }

    }

    /**
     *  The following code will prove helpful in constructing a representative from the API. This code combines the two nodes of the RepresentativeResponse into a single official :
    val (offices, officials) = getRepresentativesDeferred.await()
    _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }
    Note: getRepresentatives in the above code represents the method used to fetch data from the API
    Note: _representatives in the above code represents the established mutable live data housing representatives
     */


}