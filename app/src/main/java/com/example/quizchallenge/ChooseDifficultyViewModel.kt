package com.example.quizchallenge

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizchallenge.data.entities.DifficultyEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ChooseDifficultyViewModel (application: Application): AndroidViewModel(application){
    private val difficultyDao = AppDatabase.getDatabase(application).difficultyDao()

    val difficultyState: StateFlow<List<DifficultyEntity>> = difficultyDao.getAllDifficulty()
        .stateIn(
            scope=viewModelScope,
            started= SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}