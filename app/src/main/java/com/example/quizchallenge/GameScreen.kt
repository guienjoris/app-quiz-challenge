package com.example.quizchallenge

import android.text.TextUtils.split
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizchallenge.data.entities.GameEntity
import com.example.quizchallenge.data.entities.QuizEntity
import com.example.quizchallenge.data.entities.QuizWithDifficultyAndCategory
import kotlin.collections.flatten

@Composable
fun GameScreen(gameViewModel: GameViewModel,
               onBack: () -> Unit,
               onNavigateToHome:()-> Unit){
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
        }else if(quiz.isNotEmpty() && stepQuestion == quiz.size){
            GameEndResult(points, quiz.size,onNavigateToHome)
        }else {
            Text(text = "Loading...", modifier = Modifier.padding(16.dp))
        }

    }
}

@Composable
fun GameEndResult(points:Int,
                  sizeQuiz:Int,
                  onNavigateToHome:()-> Unit){
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(10.dp)
    ){
        Text(text="Félicitations vous avez terminé le quiz!",
            style= MaterialTheme.typography.bodyLarge
            )
        Spacer(modifier=Modifier.height(10.dp))
        Text(text="Avec un résultat de: $points/$sizeQuiz",
            style= MaterialTheme.typography.bodyMedium
            )
        Button(onClick = onNavigateToHome) {
            Text(text="Retourner à l'accueil")
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

    var showDialog by remember { mutableStateOf(false) }


    Column(){
        Card(modifier=Modifier
            .fillMaxWidth()
            .padding(5.dp)){
            Column(verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(8.dp)
                ){
                if(quiz.quiz.pictureId != null) {
                    Image(painter= painterResource(getResIdByName(quiz.quiz.pictureId)),
                        contentDescription = null
                        )
                }
                Row(verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier=Modifier.padding(15.dp)){
                    Text(text=quiz.quiz.question,
                        style= MaterialTheme.typography.bodyMedium
                    )
                }
            }


        }
        FlowRow(modifier = Modifier.padding(8.dp)){
            answers.forEach { answer ->
                Card(modifier=Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
                    .clickable(
                        enabled = true,
                        onClick = {
                            if (answer == quiz.quiz.goodAnswer) {
                                incrementPoint()
                            }
                            if(quiz.quiz.additionalInfo != null){
                                showDialog = true
                            }
                        })
                ){
                    if(showDialog && quiz.quiz.additionalInfo != null){
                        ShowDetailDialog(title= {Text(text="Bonne réponse: ",
                            style= MaterialTheme.typography.bodyLarge
                            )},
                            description= {
                                Column() {
                                    Text(text=quiz.quiz.goodAnswer,
                                        style= MaterialTheme.typography.bodyLarge
                                        )
                                    Text(text=quiz.quiz.additionalInfo,
                                        style= MaterialTheme.typography.bodySmall
                                    )
                                }

                            },
                            onBack = {showDialog = false},
                            onConfirm = {showDialog = false ; incrementQuestion()}
                            )
                    }
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

@Composable
fun ShowDetailDialog(
    title: @Composable () -> Unit,
    description: @Composable () -> Unit,
    onBack: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onConfirm,
        title = title,
        text = description,
        dismissButton = {
            Button(onClick = onBack) {
                Text("Annuler")
            }
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Ok")
            }
        },
    )
}



