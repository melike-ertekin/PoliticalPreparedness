package com.example.android.politicalpreparedness.repository.network.jsonadapter

import com.example.android.politicalpreparedness.domain.Division
import com.example.android.politicalpreparedness.repository.network.NetworkDivision
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson

class ElectionAdapter {
    @FromJson
    fun divisionFromJson (ocdDivisionId: String): NetworkDivision {
        val countryDelimiter = "country:"
        val stateDelimiter = "state:"
        val country = ocdDivisionId.substringAfter(countryDelimiter,"")
                .substringBefore("/")
        val state = ocdDivisionId.substringAfter(stateDelimiter,"")
                .substringBefore("/")
        return NetworkDivision(ocdDivisionId, country, state)
    }

    @ToJson
    fun divisionToJson (division: Division): String {
        return division.id
    }
}