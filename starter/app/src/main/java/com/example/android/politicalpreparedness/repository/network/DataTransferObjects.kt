package com.example.android.politicalpreparedness.repository.network

import com.example.android.politicalpreparedness.domain.*
import com.example.android.politicalpreparedness.repository.database.*
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.*


//------------------------------------ Election -----------------------------//
data class NetworkElectionResponse(
        val kind: String,
        val elections: List<NetworkElection>
)

data class NetworkElection(
        val id: Int,
        val name: String,
        val electionDay: Date,
        @Json(name="ocdDivisionId") val division: NetworkDivision
)

data class NetworkDivision(
        val id: String,
        val country: String,
        val state: String
)

fun NetworkDivision.asDomainModal(): Division{
    return Division(
            id = this.id,
            country = this.country,
            state = this.state

    )

}

fun NetworkDivision.asDatabaseModal(): Division{
    return Division(
            id = this.id,
            country = this.country,
            state = this.state

    )

}

/**
 * Convert network results to domain objects
 */
fun List<NetworkElection>.asDomainModel(): List<Election>{
    return this.map {
        Election(
                id = it.id,
                name = it.name,
                electionDay = it.electionDay,
                division = it.division.asDomainModal()
        )
    }
}

/**
 * Convert data transfer objects(network objects) to database objects
 */
fun List<NetworkElection>.asDatabaseModel(): Array<DatabaseElection>{
    return this.map {
        DatabaseElection(
                id = it.id,
                name = it.name,
                electionDay = it.electionDay,
                division = it.division.asDatabaseModal()
        )
    }.toTypedArray()
}





//------------------------------------ Voter Info -----------------------------//
class NetworkVoterInfoResponse (
        val election: NetworkElection,
        //val pollingLocations: String? = null, //TODO: Future Use
       // val contests: String? = null, //TODO: Future Use
        val state: List<NetworkState>
        //val electionElectionOfficials: List<NetworkElectionOfficial>? = null
)

data class NetworkState (
        val name: String,
        val electionAdministrationBody: NetworkAdministrationBody
)

data class NetworkAdministrationBody (
        val name: String? = null,
        val electionInfoUrl: String? = null,
        val votingLocationFinderUrl: String? = null,
        val ballotInfoUrl: String? = null,
        val correspondenceAddress: NetworkAddress? = null
)


data class NetworkAddress (
        val line1: String,
        val line2: String? = null,
        val city: String,
        val state: String,
        val zip: String
) {
    fun toFormattedString(): String {
        var output = line1.plus("\n")
        if (!line2.isNullOrEmpty()) output = output.plus(line2).plus("\n")
        output = output.plus("$city, $state $zip")
        return output
    }
}

fun NetworkVoterInfoResponse.asDomainModel(): VoterInfo{
    return VoterInfo(
            electionId = this.election.id,
            state = this.state[0].asDomainModelForNetworkState()
    )
}
/*fun NetworkVoterInfoResponse.asDatabaseModel(): DatabaseVoterInfo{
    return DatabaseVoterInfo(
            electionId = this.election.id,
            state = this.state[0].asDatabaseModel()
    )
}*/


fun NetworkState.asDomainModelForNetworkState(): State{
    return State(
                name = this.name,
                electionAdministrationBody = this.electionAdministrationBody.asDomainModel()
        )

}

/*
fun NetworkState.asDatabaseModel(): DatabaseState {
    return DatabaseState(
            name = this.name,
            electionAdministrationBody = this.electionAdministrationBody.asDatabaseModel()
    )

}*/

fun NetworkAdministrationBody.asDomainModel(): AdministrationBody{
    return AdministrationBody(
            //name = this.name,
            electionInfoUrl = this.electionInfoUrl,
            votingLocationFinderUrl = this.votingLocationFinderUrl,
            ballotInfoUrl = this.ballotInfoUrl,
            address = this.correspondenceAddress?.asDomainModel()

    )
}
/*
fun NetworkAdministrationBody.asDatabaseModel(): DatabaseAdministrationBody{
    return DatabaseAdministrationBody(
            //name = this.name,
            electionInfoUrl = this.electionInfoUrl,
            votingLocationFinderUrl = this.votingLocationFinderUrl,
            ballotInfoUrl = this.ballotInfoUrl,
            address = this.correspondenceAddress?.asDatabaseModel()

    )
}
*/
fun NetworkAddress.asDomainModel(): Address{
    return Address(
             line1 = this.line1,
     line2 = this.line2,
     city = this.city,
     state =this.state,
     zip =this.zip

    )
}

/*
fun NetworkAddress.asDatabaseModel(): DatabaseAddress {
    return DatabaseAddress(
            line1 = this.line1,
            line2 = this.line2,
            city = this.city,
            state =this.state,
            zip =this.zip

    )
}
*/


/*data class NetworkElectionOfficial(
        val name: String,
        val title: String,
        @Json(name="officePhoneNumber") val phone: String,
        @Json(name="faxNumber") val fax: String,
        val emailAddress: String
)

*/



//------------------------------------ Representative -----------------------------//
data class NetworkRepresentativeResponse(
        val offices: List<Office>,
        val officials: List<Official>
)
    val NetworkRepresentativeResponse.representatives
        get() = offices.flatMap { it.getRepresentatives(officials) }




