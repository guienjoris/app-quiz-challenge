package com.example.quizchallenge.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category")
data class CategoryEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    val id: Int = 0,
    @ColumnInfo("name")
    val name:String,
    @ColumnInfo("picture_id")
    val pictureId:String
    )