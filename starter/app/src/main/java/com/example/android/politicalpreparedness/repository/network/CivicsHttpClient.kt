package com.example.android.politicalpreparedness.repository.network

import com.example.android.politicalpreparedness.utils.Constant.API_KEY
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor


class  CivicsHttpClient: OkHttpClient() {

    companion object {



        val interceptor = HttpLoggingInterceptor()
                .apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }

        fun getClient(): OkHttpClient {
            return Builder()
                    .addInterceptor { chain ->
                        val original = chain.request()
                        val url = original
                                .url
                                .newBuilder()
                                .addQueryParameter("key", API_KEY)
                                .build()
                        val request = original
                                .newBuilder()
                                .url(url)
                                .build()
                        chain.proceed(request)
                    }.addInterceptor(interceptor)
                    .build()
        }

    }

}