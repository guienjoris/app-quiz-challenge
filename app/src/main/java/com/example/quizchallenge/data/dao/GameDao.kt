package com.example.quizchallenge.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.quizchallenge.data.entities.GameWithDifficultyAndCategory
import com.example.quizchallenge.data.entities.QuizWithDifficultyAndCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {
    @Transaction()
    @Query("SELECT * FROM game ORDER BY id DESC")
    fun getGameWithDifficultyAndCategory(): Flow<List<GameWithDifficultyAndCategory>>
}