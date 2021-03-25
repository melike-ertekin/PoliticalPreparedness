package com.example.android.politicalpreparedness.repository.network


import com.example.android.politicalpreparedness.repository.network.jsonadapter.ElectionAdapter
import com.example.android.politicalpreparedness.repository.network.jsonadapter.RepresentativeAdapter
import com.example.android.politicalpreparedness.utils.Constant.BASE_URL
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*


// TODO: Add adapters for Java Date and custom adapter ElectionAdapter (included in project)
private val moshi = Moshi.Builder()
        .add(ElectionAdapter())
        .add(RepresentativeAdapter())
        .add(Date::class.java, Rfc3339DateJsonAdapter().nullSafe())
        .add(KotlinJsonAdapterFactory())
        .build()

private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(CivicsHttpClient.getClient())
        .baseUrl(BASE_URL)
        .build()

/**
 *  Documentation for the Google Civics API Service can be found at https://developers.google.com/civic-information/docs/v2
 */

interface CivicsApiService {

    @GET("elections")
    suspend fun getElections(): NetworkElectionResponse


    @GET("voterinfo")
    suspend fun getVoterInfo(
            @Query("electionId") electionId: Int,
            @Query("address") address: String
    ): NetworkVoterInfoResponse


    @GET("representatives")
    suspend fun getRepresentativeInfoByAddress(
            @Query("address") address: String): NetworkRepresentativeResponse
}

object CivicsApi {
    val retrofitService: CivicsApiService by lazy {
        retrofit.create(CivicsApiService::class.java)
    }
}