package com.example.quizchallenge.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.quizchallenge.data.entities.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Query("SELECT * FROM category ORDER BY id DESC")
    fun getAllCategories(): Flow<List<CategoryEntity>>
}