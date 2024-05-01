package com.example.bitfitpart1

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

private const val TAG = "DetailActivity"

class FoodInsert : AppCompatActivity() {
    //private lateinit var mediaImageView: ImageView
    private lateinit var foodTextView: EditText
    private lateinit var calorieTextView: EditText
    private lateinit var saveButton: Button
    private lateinit var imageUpload: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.food_insert)


        //mediaImageView = findViewById(R.id.mediaImage)
        foodTextView = findViewById(R.id.foodEdit)
        calorieTextView = findViewById(R.id.calorieEdit)
        saveButton = findViewById(R.id.InsertButton)

        saveButton.setOnClickListener {
            val resultIntent = Intent()


            resultIntent.putExtra("foodName", foodTextView.text.toString())
            resultIntent.putExtra("calories", calorieTextView.text.toString().toInt())
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

    }
}