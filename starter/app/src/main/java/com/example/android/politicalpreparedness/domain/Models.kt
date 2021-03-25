package com.example.android.politicalpreparedness.domain

import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.room.Entity
import com.example.android.politicalpreparedness.repository.network.NetworkAddress
import com.example.android.politicalpreparedness.repository.network.NetworkElection
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize
import java.util.*
/*
 *Domain level models
 *
 * */

//------------------------------------ Election -----------------------------//
@Parcelize
data class Election(
        val id: Int,
        val name: String,
        val electionDay: Date,
        val division: Division
): Parcelable

@Parcelize
data class Division(
        val id: String,
        val country: String,
        val state: String
): Parcelable


//------------------------------------ Voter Info -----------------------------//
data class VoterInfo (
        val electionId: Int,
        //val pollingLocations: String? = null, //TODO: Future Use
        // val contests: String? = null, //TODO: Future Use
        val state: State
        //val electionElectionOfficials: List<NetworkElectionOfficial>? = null
)

data class State (
        val name: String,
        val electionAdministrationBody: AdministrationBody
)

data class AdministrationBody (
        val name: String? = null,
        val electionInfoUrl: String? = null,
        val votingLocationFinderUrl: String? = null,
        val ballotInfoUrl: String? = null,
        val address: Address? = null
)


data class Address (
        var line1: String,
        var line2: String ? = null,
        var city: String,
        var state: String,
        var zip: String
) : BaseObservable(){
    fun toFormattedString(): String {
        var output = line1.plus("\n")
        if (!line2.isNullOrEmpty()) output = output.plus(line2).plus("\n")
        output = output.plus("$city, $state $zip")
        return output
    }
}

//------------------------------------ Representative-----------------------------//

data class Channel(
        val type: String,
        val id: String
)

data class Representative (
        val official: Official,
        val office: Office
)


data class Official (
        val name: String,
        val address: List<Address>? = null,
        val party: String? = null,
        val phones: List<String>? = null,
        val urls: List<String>? = null,
        val photoUrl: String? = null,
        val channels: List<Channel>? = null
)

data class Office (
        val name: String,
        @Json(name="divisionId") val division:Division,
        @Json(name="officialIndices") val officials: List<Int>
) {
    fun getRepresentatives(officials: List<Official>): List<Representative> {
        return this.officials.map { index ->
            Representative(officials[index], this)
        }
    }
}

