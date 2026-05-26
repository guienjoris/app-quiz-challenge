package com.example.quizchallenge.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.example.quizchallenge.data.entities.QuizWithDifficultyAndCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizDao {
    @Transaction()
    @Query("SELECT * FROM quiz WHERE id_category = :idCategory AND id_difficulty = :idDifficulty")
    fun getQuizByDifficultyAndCategory(idDifficulty: Int,idCategory:Int): Flow<List<QuizWithDifficultyAndCategory>>
}