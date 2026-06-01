package com.example.quizchallenge.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.quizchallenge.data.entities.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Query("SELECT * FROM category WHERE id = :id")
    fun getCategoryById(id:Int):Flow<CategoryEntity>
    @Query("SELECT * FROM category ORDER BY id ASC")
    fun getAllCategories(): Flow<List<CategoryEntity>>
}