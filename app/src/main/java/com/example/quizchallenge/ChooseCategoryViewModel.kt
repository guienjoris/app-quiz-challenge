package com.example.quizchallenge

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizchallenge.data.entities.CategoryEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ChooseCategoryViewModel(application: Application): AndroidViewModel(application){
    private val categoryDao = AppDatabase.getDatabase(application).categoryDao()

    val categoriesState : StateFlow<List<CategoryEntity>> = categoryDao.getAllCategories()
        .stateIn(
            scope=viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue=emptyList()
        )
}