package com.example.quizchallenge

import android.text.TextUtils.split
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizchallenge.data.entities.GameEntity
import com.example.quizchallenge.data.entities.QuizEntity
import com.example.quizchallenge.data.entities.QuizWithDifficultyAndCategory
import kotlin.collections.flatten

@Composable
fun GameScreen(gameViewModel: GameViewModel,onBack: () -> Unit){
    val quiz by gameViewModel.gameQuizState.collectAsState()

    LaunchedEffect(Unit) {
        gameViewModel.createGame(points=0)
    }

    var stepQuestion by remember  { mutableIntStateOf(0) }
    var points by remember  { mutableIntStateOf(0) }

    fun incrementPoint (){ points++ }
    fun incrementQuestion () { stepQuestion++ }

    LaunchedEffect(points) {
        if(points != 0){
            gameViewModel.updateGame(points)
        }
    }


    Column(modifier=Modifier.verticalScroll(rememberScrollState())){
        IconButton(onClick = onBack) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                tint=MaterialTheme.colorScheme.tertiary,
                contentDescription = "Back to the previous page"
            )
        }
        if (quiz.isNotEmpty() && stepQuestion < quiz.size) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier=Modifier.fillMaxWidth()){
                Text(text="Points: $points",
                    style= MaterialTheme.typography.bodyLarge
                )
            }

            DisplayQuestion(quiz[stepQuestion],::incrementPoint,::incrementQuestion)
        } else {
            Text(text = "Loading...", modifier = Modifier.padding(16.dp))
        }

    }
}

@Composable
fun DisplayQuestion(quiz: QuizWithDifficultyAndCategory,
                    incrementPoint:()-> Unit,
                    incrementQuestion: ()-> Unit
                    ){


    val answers = (listOf(quiz.quiz.goodAnswer) + quiz.quiz.badAnswer
        .split("|")).shuffled()


    Column(){
        Card(modifier=Modifier.fillMaxWidth()
            .padding(5.dp)){
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier=Modifier.padding(15.dp)){
                Text(text=quiz.quiz.question,
                        style= MaterialTheme.typography.bodyMedium
                )
            }

        }
        FlowRow(modifier = Modifier.padding(8.dp)){
            answers.forEach { answer ->
                Card(modifier=Modifier.fillMaxWidth()
                    .padding(5.dp)
                    .clickable(enabled=true,
                        onClick = {
                            if(answer == quiz.quiz.goodAnswer) {
                                incrementPoint()
                            }
                            incrementQuestion()
                        })
                ){
                    Row(verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier=Modifier.padding(15.dp)) {
                        Text(text = answer,
                            style= MaterialTheme.typography.bodySmall
                            )
                    }
                }
            }
        }
    }
}

