package com.example.upstoxtest

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.upstoxtest.adapters.StocksAdapter
import com.example.upstoxtest.databinding.ActivityMainBinding
import com.example.upstoxtest.networks.UserRepository
import com.example.upstoxtest.viewmodels.StockViewModel
import com.example.upstoxtest.viewmodels.StockViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: StockViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Change status bar color
        val window: Window = this.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = resources.getColor(R.color.magenta_2) // Change to your desired color

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeViewModel()
        setUpRecyclerview()
        setUpObservers()
        setUpListener()
        viewModel.getUserHoldings()
    }

    private fun showTotalPnL() {
        val adapter = (binding.rcv.adapter as StocksAdapter)
        binding.tvTotalProfitLoss.text = getString(R.string.rs_amount, (adapter.getCurrentValue() - adapter.getTotalInvestmentValue()))
    }

    private fun setUpListener() {
        binding.upArrow.setOnClickListener {
            if (binding.otherDataContainer.isVisible) {
                showAndHide(false)
            } else {
                showAndHide(true)
                val adapter = (binding.rcv.adapter as StocksAdapter)
                binding.tvCurrentValue.text = getString(R.string.rs_amount, adapter.getCurrentValue())
                binding.tvTotalInvestment.text = getString(R.string.rs_amount, adapter.getTotalInvestmentValue())
                binding.tvTodaySProfitLoss.text = getString(R.string.rs_amount, adapter.getTodayProfitAndLoss())
            }
        }
    }

    private fun setUpRecyclerview() {
        binding.rcv.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = StocksAdapter()
        }
    }

    private fun setUpObservers() {
        viewModel.userHoldings.observe(this){
            binding.pBar.isVisible = false
            if (it != null) {
                (binding.rcv.adapter as StocksAdapter).updateData(it)
                showTotalPnL()
            } else {
                viewModel.parseLocalJson(this)
                Toast.makeText(
                    this,
                    "Something went wrong, please try after sometime.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun initializeViewModel() {
        val userRepository = UserRepository()
        val viewModelFactory = StockViewModelFactory(userRepository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(StockViewModel::class.java)
    }

    private fun showAndHide(isVisible: Boolean) {
        binding.apply {
            currentValue.isVisible = isVisible
            totalInvestment.isVisible = isVisible
            totalProfitLoss.isVisible = isVisible
            otherDataContainer.isVisible = isVisible
            upArrow.rotation = if (isVisible) 180f else 360f
        }
    }
}