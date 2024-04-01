package com.example.upstoxtest.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.upstoxtest.data.UserHolding
import com.example.upstoxtest.networks.UserRepository
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream

class StockViewModel (private val userRepository: UserRepository) : ViewModel() {

    private val _userHoldings = MutableLiveData<List<UserHolding>>()
    val userHoldings: LiveData<List<UserHolding>> get() = _userHoldings

    fun getUserHoldings() {
        viewModelScope.launch {
            val holdings = userRepository.getUserHoldings()
            _userHoldings.value = holdings?.data?.userHolding
        }
    }

    fun parseLocalJson(context: Context) {

        val localUserHoldingList = mutableListOf<UserHolding>()
        val jsonString = readJsonFromAssets("sample_json.json", context)

        try {
            val jsonObject = JSONObject(jsonString)
            val dataArray = jsonObject.getJSONObject("data").getJSONArray("userHolding")

            for (i in 0 until dataArray.length()) {
                val item = dataArray.getJSONObject(i)
                val symbol = item.getString("symbol")
                val quantity = item.getInt("quantity")
                val ltp = item.getDouble("ltp")
                val avgPrice = item.getDouble("avgPrice")
                val close = item.getDouble("close")

                val userHolding = UserHolding(avgPrice, close, ltp, quantity, symbol)
                localUserHoldingList.add(userHolding)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        _userHoldings.value = localUserHoldingList
    }

    private fun readJsonFromAssets(fileName: String, context: Context): String {
        return try {
            val inputStream: InputStream = context.assets.open(fileName)
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
            ""
        }
    }

}