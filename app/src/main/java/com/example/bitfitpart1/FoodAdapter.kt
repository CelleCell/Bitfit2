package com.example.bitfitpart1

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FoodAdapter(private val context: Context, private val foods: List<FoodItem>, private val lifecycleScope: LifecycleCoroutineScope, private val foodApplication: FoodApplication) :
    RecyclerView.Adapter<FoodAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.food_item, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val food = foods[position]
        holder.bind(food)

    }

    override fun getItemCount() = foods.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
         {

        private val nameTextView = itemView.findViewById<TextView>(R.id.nameItem)
        private val calorieTextView = itemView.findViewById<TextView>(R.id.calorieNum)

        init {
            itemView.setOnLongClickListener {
                val food = foods[absoluteAdapterPosition]
                val id = food.id

                lifecycleScope.launch(Dispatchers.IO) {
                    (foodApplication).db.foodDao().deleteOne(id)


                    foods.drop(id.toInt())
                }

                true
            }

        }

        fun bind(food: FoodItem) {
            nameTextView.text = food.foodName
            calorieTextView.text = food.calories.toString()
        }
    }
}