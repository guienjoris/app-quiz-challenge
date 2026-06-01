package com.example.quizchallenge

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizchallenge.data.entities.DifficultyEntity

@Composable
fun ChooseDifficultyScreen(
    chooseDifficultyViewModel: ChooseDifficultyViewModel,
    onBack: ()-> Unit,
    onNavigateToGame: (idDifficulty: Int)-> Unit
                           ){
    val difficulties = chooseDifficultyViewModel.difficultyState.collectAsState()

    Column{
        IconButton(onClick = onBack) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                tint=MaterialTheme.colorScheme.tertiary,
                contentDescription = "Back to the previous page"
            )
        }
        Text(text="Choisissez votre difficulté")
        Box{
            FlowRow(modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
                maxItemsInEachRow = 3,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)) {
                difficulties.value.forEach { difficulty ->
                    DifficultyCard(difficulty,onNavigateToGame)
                }
            }
        }
    }
}

@Composable
fun DifficultyCard(difficulty: DifficultyEntity,
                   onNavigateToGame: (idDifficulty: Int)-> Unit){

    var color: Color = MaterialTheme.colorScheme.secondary

    when(difficulty.difficulty){
        "Facile" -> color = MaterialTheme.colorScheme.secondary
        "Moyenne" -> color = MaterialTheme.colorScheme.tertiary
        "Difficile" -> color = MaterialTheme.colorScheme.error
    }

    Card(modifier=Modifier.clickable(onClick={onNavigateToGame(difficulty.id)})){
        Row(horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier= Modifier.background(color=color).padding(5.dp)){
            Text(text=difficulty.difficulty,
                style= MaterialTheme.typography.bodyLarge,
                color= Color.Black
                )
        }
    }
}