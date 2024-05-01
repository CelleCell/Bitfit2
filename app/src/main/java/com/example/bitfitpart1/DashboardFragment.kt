package com.example.bitfitpart1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode

class DashboardFragment : Fragment() {

    private lateinit var averageCal: TextView
    private lateinit var minCal: TextView
    private lateinit var maxCal: TextView
    private lateinit var deleteButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        averageCal = view.findViewById(R.id.averageCaloriesTextView)
        minCal = view.findViewById(R.id.foodLeastCaloriesTextView)
        maxCal = view.findViewById(R.id.foodMostCaloriesTextView)
        deleteButton = view.findViewById(R.id.buttonDelete)

        refreshUI()

        deleteButton.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                (requireContext().applicationContext as FoodApplication).db.foodDao().deleteAll()
                launch(Dispatchers.Main) {
                    // After deleting, trigger a refresh of the UI
                    refreshUI()
                }
            }
        }

        return view
    }

    private fun refreshUI() {
        lifecycleScope.launch {
            (requireContext().applicationContext as FoodApplication).db.foodDao().getAll()
                .collect { databaseList ->
                    if (databaseList.isNotEmpty()) {
                        // Calculate average calories
                        val totalCalories = databaseList.sumOf { it.calories!!.toDouble()}
                        val averageCalories = totalCalories / databaseList.size
                        val roundedAverage =
                            BigDecimal(averageCalories).setScale(2, RoundingMode.HALF_EVEN)
                        averageCal.text = "$roundedAverage"

                        // Find food item with minimum calories
                        val foodWithMinCalories = databaseList.minByOrNull { it.calories!! }
                        minCal.text = "${foodWithMinCalories?.foodName}"

                        // Find food item with maximum calories
                        val foodWithMaxCalories = databaseList.maxByOrNull { it.calories!! }
                        maxCal.text = "${foodWithMaxCalories?.foodName}"
                    } else {
                        // If there are no items in the database after deletion, update UI accordingly
                        averageCal.text = "0"
                        minCal.text = "0"
                        maxCal.text = "0"
                    }
                }
        }
    }
}
