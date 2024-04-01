package com.example.upstoxtest.networks

import com.example.upstoxtest.data.StocksData
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("your_endpoint")
    suspend fun getUserHoldings(): Response<StocksData>
}