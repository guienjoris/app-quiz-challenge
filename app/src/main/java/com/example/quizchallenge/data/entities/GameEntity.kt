package com.example.quizchallenge.data.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "game",foreignKeys = [
    ForeignKey(
        entity = CategoryEntity::class,
        parentColumns = ["id"],
        childColumns = ["id_category"]
    ),
    ForeignKey(
        entity = DifficultyEntity::class,
        parentColumns = ["id"],
        childColumns = ["id_difficulty"]
    )
])
data class GameEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    val id: Long = 0,
    @ColumnInfo("points", defaultValue = "0")
    val points: Int = 0,
    @ColumnInfo("number_of_questions", defaultValue = "5")
    val numberOfQuestions: Int = 5,
    @ColumnInfo("id_category")
    val idCategory: Int,
    @ColumnInfo("id_difficulty")
    val idDifficulty: Int
)

data class GameWithDifficultyAndCategory(
    @Embedded val game: GameEntity,

    @Relation(
        parentColumn = "id_category",
        entityColumn = "id"
    )
    val category: CategoryEntity,
    @Relation(
        parentColumn = "id_difficulty",
        entityColumn = "id"
    )
    val difficulty: DifficultyEntity
)