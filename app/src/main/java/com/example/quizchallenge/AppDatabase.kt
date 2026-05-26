package com.example.quizchallenge

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.quizchallenge.data.dao.CategoryDao
import com.example.quizchallenge.data.dao.DifficultyDao
import com.example.quizchallenge.data.dao.QuizDao
import com.example.quizchallenge.data.entities.CategoryEntity
import com.example.quizchallenge.data.entities.DifficultyEntity
import com.example.quizchallenge.data.entities.GameEntity
import com.example.quizchallenge.data.entities.PlayerEntity
import com.example.quizchallenge.data.entities.QuizEntity

@Database(entities = [
    CategoryEntity::class,
    DifficultyEntity::class,
    GameEntity::class,
    PlayerEntity::class,
    QuizEntity::class,
],
    version = 1,
    exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun categoryDao() : CategoryDao
    abstract fun difficultyDao(): DifficultyDao
    abstract fun quizDao(): QuizDao


    // Singleton pour éviter d'ouvrir plusieurs instances de la base de données en même temps
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "database.db"
                ).createFromAsset("database_default.db")
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}