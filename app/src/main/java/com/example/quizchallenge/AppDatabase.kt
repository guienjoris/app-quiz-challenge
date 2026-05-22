package com.example.quizchallenge

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.quizchallenge.data.dao.CategoryDao
import com.example.quizchallenge.data.entities.CategoryEntity

@Database(entities = [
    CategoryEntity::class
],
    version = 1,
    exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun categoryDao() : CategoryDao

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