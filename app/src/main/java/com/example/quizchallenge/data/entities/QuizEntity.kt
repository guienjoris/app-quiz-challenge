package com.example.quizchallenge.data.entities

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity("quiz", foreignKeys = [
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
data class QuizEntity (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("id")
    val id: Int = 0,
    @ColumnInfo("question")
    val question: String,
    @ColumnInfo("bad_answer")
    val badAnswer: String,
    @ColumnInfo("good_answer")
    val goodAnswer: String,
    @ColumnInfo("indice")
    val indice: String?,
    @ColumnInfo("additional_information")
    val additionalInfo: String?,
    @ColumnInfo("picture_id")
    val pictureId:String?,
    @ColumnInfo("id_category")
    val idCategory: Int,
    @ColumnInfo("id_difficulty")
    val idDifficulty: Int
)

data class QuizWithDifficultyAndCategory(
    @Embedded val quiz: QuizEntity,

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