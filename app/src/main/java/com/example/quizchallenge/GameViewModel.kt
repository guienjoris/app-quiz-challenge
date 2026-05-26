package com.example.quizchallenge

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizchallenge.data.entities.QuizWithDifficultyAndCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlin.collections.emptyList

class GameViewModel(application: Application): AndroidViewModel(application) {
    private val quizDao = AppDatabase.getDatabase(application).quizDao()

    var gameQuizState : Flow<List<QuizWithDifficultyAndCategory>> = MutableStateFlow(
        emptyList()
    )




    fun getQuizByDifficultyAndCategory(idCategory:Int,idDifficulty: Int){
        println(idCategory)
        println(idDifficulty)
        viewModelScope.launch{
            gameQuizState =
                quizDao.getQuizByDifficultyAndCategory(idCategory = idCategory ,idDifficulty=idDifficulty)

        }
    }

}