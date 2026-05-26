package com.example.quizchallenge

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

@Composable
fun GameScreen(gameViewModel: GameViewModel,onBack: () -> Unit){
    val quiz by gameViewModel.gameQuizState.collectAsState(
        initial = emptyList()
    )
    Column{
        IconButton(onClick = onBack) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                tint=MaterialTheme.colorScheme.tertiary,
                contentDescription = "Back to the previous page"
            )
        }
    }

    println(quiz)
}