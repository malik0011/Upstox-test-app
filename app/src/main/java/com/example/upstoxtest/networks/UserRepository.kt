package com.example.upstoxtest.networks

import com.example.upstoxtest.data.StocksData

class UserRepository {

    private val apiService = RetrofitClient.apiService

    suspend fun getUserHoldings(): StocksData? {
        try {
            val response = apiService.getUserHoldings()
            return if (response.isSuccessful) {
                response.body()
            } else {
                // Handle error
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}