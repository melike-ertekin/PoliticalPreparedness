package com.example.android.politicalpreparedness.repository.database

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.android.politicalpreparedness.domain.*
import com.example.android.politicalpreparedness.repository.network.NetworkElection
import com.squareup.moshi.Json
import java.util.*

@Entity(tableName = "elections")
data class DatabaseElection constructor(
        @PrimaryKey
        val id: Int,
        val name: String,
        val electionDay: Date,
        @Embedded(prefix = "division_") val division: Division
)

@Entity(tableName = "followed_elections")
data class FollowedElection constructor(
        @PrimaryKey
        val id: Int
)

/**
 * Convert database objects to domain objects
 */
fun List<DatabaseElection>.asDomainModel(): List<Election>{
    return map{
        Election(
                id = it.id,
                name = it.name,
                electionDay = it.electionDay,
                division = it.division
        )
    }
}
