package com.example.quizchallenge.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.quizchallenge.data.entities.GameEntity
import com.example.quizchallenge.data.entities.QuizWithDifficultyAndCategory
import kotlinx.coroutines.flow.Flow
import kotlin.reflect.KClass

@Dao
interface QuizDao {
    @Transaction()
    @Query("SELECT * FROM quiz WHERE id_category = :idCategory AND id_difficulty = :idDifficulty")
    fun getQuizByDifficultyAndCategory(idDifficulty: Int,idCategory:Int): Flow<List<QuizWithDifficultyAndCategory>>

    @Insert(entity = GameEntity::class)
    suspend fun insertGame(entity: GameEntity): Long

    @Update(entity = GameEntity::class,
        onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateGame(entity: GameEntity)
}