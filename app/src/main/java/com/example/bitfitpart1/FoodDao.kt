package com.example.bitfitpart1

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodDao {
    @Query("SELECT * FROM food_table")
    fun getAll(): Flow<List<FoodEntity>>

    @Insert
    fun insertOne(foodItem: FoodEntity)

    @Query("DELETE FROM food_table WHERE id = :itemId")
    fun deleteOne(itemId: Long)

    @Query("DELETE FROM food_table")
    fun deleteAll()
}
