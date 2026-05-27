package com.example.quizchallenge

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizchallenge.data.entities.GameEntity
import com.example.quizchallenge.data.entities.QuizWithDifficultyAndCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GameViewModel(application: Application): AndroidViewModel(application) {
    private val quizDao = AppDatabase.getDatabase(application).quizDao()

    // On utilise un MutableStateFlow privé pour pouvoir modifier sa valeur
    private val _gameQuizState = MutableStateFlow<List<QuizWithDifficultyAndCategory>>(emptyList())
    
    // On expose une version publique immuable (StateFlow) que l'UI va observer
    val gameQuizState: StateFlow<List<QuizWithDifficultyAndCategory>> = _gameQuizState.asStateFlow()

    // Variable privée modifiable pour stocker l'ID de la partie en cours
    private val _currentGameId = MutableStateFlow<Long?>(null)

    // Version publique (en lecture seule) exposée à l'UI si nécessaire
    val currentGameId: StateFlow<Long?> = _currentGameId.asStateFlow()
    private var fetchJob: Job? = null
    private var _idCategory : Int = 0
    private var _idDifficulty : Int = 0

    fun getQuizByDifficultyAndCategory(idCategory: Int, idDifficulty: Int) {
        // On annule la recherche précédente si elle est encore en cours
        fetchJob?.cancel()

        _idCategory = idCategory
        _idDifficulty = idDifficulty
        
        fetchJob = viewModelScope.launch {
            // On collecte le Flow retourné par Room et on met à jour notre StateFlow
            quizDao.getQuizByDifficultyAndCategory(idCategory = idCategory, idDifficulty = idDifficulty)
                .collect { list ->
                    _gameQuizState.value = list.shuffled().slice(IntRange(0,4))
                }
        }
    }

    fun createGame(points: Int) {
        viewModelScope.launch {
            val newGame = GameEntity(
                points = points,
                idCategory = _idCategory,
                idDifficulty = _idDifficulty
            )

            val newGameDatabase = quizDao.insertGame(newGame)
            _currentGameId.value = newGameDatabase
        }
    }

    fun updateGame(points: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val activeId = _currentGameId.value
            if(activeId != null) {
                val updatedGame = GameEntity(
                    id = activeId,
                    idCategory = _idCategory,
                    idDifficulty = _idDifficulty,
                    points = points
                )
                quizDao.updateGame(updatedGame)

            }else {
                // Optionnel : Gérer le cas où l'utilisateur veut faire un update
                // mais qu'aucune partie n'a été insérée au préalable.
                Log.e("GameViewModel", "Impossible d'update : aucun ID de partie stocké.")
            }

        }
    }
}
