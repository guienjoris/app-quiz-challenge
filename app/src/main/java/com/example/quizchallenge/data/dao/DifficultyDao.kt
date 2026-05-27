package com.example.quizchallenge.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.quizchallenge.data.entities.CategoryEntity
import com.example.quizchallenge.data.entities.DifficultyEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DifficultyDao {
    @Query("SELECT * FROM difficulty ORDER BY id ASC")
    fun getAllDifficulty(): Flow<List<DifficultyEntity>>
}