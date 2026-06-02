package com.example.quizchallenge

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizchallenge.data.entities.GameWithDifficultyAndCategory

@Composable
fun HistoricScreen(onBack: ()-> Unit,
                   historicViewModel: HistoricViewModel) {

    val historicGame by  historicViewModel.gameState.collectAsState()


    Column(){
        IconButton(onClick = onBack) {
            Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                tint=MaterialTheme.colorScheme.tertiary,
                contentDescription = "Back to the previous page"
            )
        }
        if(historicGame.isNotEmpty()){
            Text(style= MaterialTheme.typography.bodySmall,
                text="L'ordre d'affichage est du plus récent au plus vieux.",
                modifier=Modifier.padding(10.dp)
                )
            FlowRow(maxItemsInEachRow = 1,
                verticalArrangement = Arrangement.Center,
                horizontalArrangement = Arrangement.Center,
                modifier=Modifier.verticalScroll(rememberScrollState())
                ) {
                historicGame.forEach { game ->
                    DisplayHistoric(game)
                }
            }
        }else{
            Text(text="Il n'y a pas d'historique de partie")
        }
    }

}

@Composable
fun DisplayHistoric(game: GameWithDifficultyAndCategory){


            Box(
                modifier= Modifier.fillMaxWidth()
                .padding(5.dp)
                .border(border = BorderStroke(1.dp,Color.Black), shape = RoundedCornerShape(20))
                .background(color=MaterialTheme.colorScheme.surfaceContainer,
                    shape = RoundedCornerShape(20)
                )
            ){
                Row(horizontalArrangement = Arrangement.SpaceBetween){
                    Column(modifier=Modifier.padding(20.dp).weight(1f)){
                        Column{
                            Text(text="Difficulté: ",
                                fontWeight = FontWeight.Bold,
                                fontSize= 15.sp
                            )
                            Text(text=game.difficulty.difficulty,fontSize= 20.sp)
                        }
                        Column{
                            Text(text="Catégorie: ",
                                fontWeight = FontWeight.Bold,
                                fontSize= 15.sp
                            )
                            Text(text=game.category.name,fontSize= 20.sp)
                        }
                    }
                    Column(modifier=Modifier.fillMaxHeight().padding(20.dp).weight(1f)){
                        Column(verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                            ){
                            Text(text="Score:",
                                fontWeight = FontWeight.Bold,
                                fontSize= 15.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(80.dp)
                                    .border(2.dp, MaterialTheme.colorScheme.primaryContainer, CircleShape)
                            ) {
                                    Text(text = "${game.game.points}/${game.game.numberOfQuestions}", fontSize = 30.sp)
                            }
                            }

                        }
                    }
                }
            }


