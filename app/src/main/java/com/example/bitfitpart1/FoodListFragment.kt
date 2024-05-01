package com.example.bitfitpart1

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FoodListFragment : Fragment() {
    private val foods = mutableListOf<FoodItem>()
    private lateinit var foodsRecyclerView: RecyclerView
    private lateinit var foodAdapter : FoodAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_food_list, container, false)

        val insertButton = view.findViewById<Button>(R.id.button)

        // Add these configurations for the recyclerView and to configure the adapter
        val layoutManager = LinearLayoutManager(context)
        foodsRecyclerView = view.findViewById(R.id.foodRv)
        foodsRecyclerView.layoutManager = layoutManager
        foodsRecyclerView.setHasFixedSize(true)

        // Use the class-level variable instead of defining a new local variable
        foodAdapter = FoodAdapter(view.context, foods, viewLifecycleOwner.lifecycleScope, requireActivity().application as FoodApplication)
        foodsRecyclerView.adapter = foodAdapter

        lifecycleScope.launch {
            (requireContext().applicationContext as FoodApplication).db.foodDao().getAll().collect { databaseList ->
                databaseList.map { entity ->
                    FoodItem(
                        entity.id,
                        entity.foodName,
                        entity.calories,
                    )
                }.also { mappedList ->
                    foods.clear()
                    foods.addAll(mappedList)
                    foodAdapter.notifyDataSetChanged()
                }
            }
        }

        val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Intent? = result.data
                val resultFood = data?.getStringExtra("foodName")
                val resultCalorie = data?.getIntExtra("calories", 0)
                if (resultFood != null && resultCalorie != null) {
                    lifecycleScope.launch(Dispatchers.IO) {
                        (requireContext().applicationContext as FoodApplication).db.foodDao().insertOne(
                            FoodEntity(foodName = resultFood, calories = resultCalorie)
                        )
                    }
                }
            }
        }

        insertButton.setOnClickListener {
            val intent = Intent(context, FoodInsert::class.java)
            startForResult.launch(intent)
        }

        // Update the return statement to return the inflated view from above
        return view
    }

    companion object {
        fun newInstance(): FoodListFragment {
            return FoodListFragment()
        }
    }
}