package com.rahdeva.bencanaapp.api

import com.rahdeva.bencanaapp.data.model.DisasterResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("reports")
    suspend fun getDisasters(
        @Query("timeperiod") timePeriod: Int = 259200
    ): DisasterResponse

    @GET("reports")
    suspend fun getDisastersByLocationAndType(
        @Query("admin") admin: String,
        @Query("disaster") disaster: String,
        @Query("timeperiod") timePeriod: Int = 259200
    ): DisasterResponse

    @GET("reports")
    suspend fun getDisastersByLocation(
        @Query("admin") admin: String,
        @Query("timeperiod") timePeriod: Int = 259200
    ): DisasterResponse

    @GET("reports")
    suspend fun getDisastersByType(
        @Query("disaster") disaster: String,
        @Query("timeperiod") timePeriod: Int = 259200
    ): DisasterResponse

}