package com.example.upstoxtest.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.upstoxtest.R
import com.example.upstoxtest.data.UserHolding
import com.example.upstoxtest.databinding.ItemStockBinding

class StocksAdapter() : RecyclerView.Adapter<StocksAdapter.StockItemViewHolder>() {

    private var userHoldings: MutableList<UserHolding> = mutableListOf()

    private var currentValue :Double = 0.0;
    private var totalInvestmentValue :Double = 0.0;
    private var totalProfitLossValue :Double = 0.0;


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockItemViewHolder {
        val binding = ItemStockBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StockItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StockItemViewHolder, position: Int) {
        holder.bind(userHoldings[position])
    }

    override fun getItemCount(): Int {
        return userHoldings.size
    }

    fun updateData(newData: List<UserHolding>) {
        userHoldings.clear()
        userHoldings = newData.toMutableList()
        notifyDataSetChanged()
        calculateAllValues()
    }

    class StockItemViewHolder(private val binding: ItemStockBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(userHolding: UserHolding) {
            binding.apply {
                tvStockName.text = userHolding.symbol
                tvQuantity.text = userHolding.quantity.toString()
                tvLtp.text = root.context.getString(R.string.rs_amount, userHolding.ltp)
                tvPnl.text = root.context.getString(R.string.rs_amount,
                    ((userHolding.ltp * userHolding.quantity) - (userHolding.avgPrice * userHolding.quantity))
                )
            }
        }
    }

    private fun calculateAllValues() {
        for (data in userHoldings){
            currentValue += (data.quantity * data.ltp)
            totalInvestmentValue += (data.quantity * data.avgPrice)
            totalProfitLossValue += (data.quantity * (data.close - data.ltp))
        }
    }

    fun getCurrentValue() = currentValue

    fun getTotalInvestmentValue() = totalInvestmentValue

    fun getTodayProfitAndLoss() = totalProfitLossValue
}