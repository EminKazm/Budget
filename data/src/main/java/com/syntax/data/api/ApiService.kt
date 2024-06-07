package com.syntax.data.api

import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("listquotes")
    suspend fun getCurrencies(): List<String>

    @GET("exchange")
    suspend fun exchangeCurrency(
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("q") amount: Double
    ): Double
}