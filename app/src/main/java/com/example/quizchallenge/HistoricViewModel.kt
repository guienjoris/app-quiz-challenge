package com.example.quizchallenge

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizchallenge.data.entities.GameWithDifficultyAndCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HistoricViewModel(application: Application): AndroidViewModel(application) {
    private val gameDao = AppDatabase.getDatabase(application).gameDao()
    val gameState: StateFlow<List<GameWithDifficultyAndCategory>> = gameDao.getGameWithDifficultyAndCategory()
        .stateIn(
            scope= viewModelScope,
            started= SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}