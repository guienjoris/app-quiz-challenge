package com.example.quizchallenge

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizchallenge.data.entities.DifficultyEntity

@Composable
fun ChooseDifficultyScreen(
    chooseDifficultyViewModel: ChooseDifficultyViewModel,
    onBack: ()-> Unit,
    onNavigateToGame: (idDifficulty: Int,numberOfQuestions:String)-> Unit
                           ){
    val difficulties = chooseDifficultyViewModel.difficultyState.collectAsState()

    val options = listOf("5", "10", "15", "20")

    var difficultyChoice by remember { mutableIntStateOf(1) }
    var numberOfQuestions by remember { mutableStateOf(options[0]) }

    val setNumberOfQuestion = { number:String -> numberOfQuestions = number }
    val setDifficultyChoice = { number: Int -> difficultyChoice = number }

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
                    DifficultyCard(difficulty,setDifficultyChoice)
                }
            }
        }
        Text(text="Choisissez votre nombre de questions")
        ChooseNumberOfQuestions(options,numberOfQuestions,setNumberOfQuestion)
        Spacer(modifier=Modifier.height(10.dp))

        Text(text="Difficulté: ${difficulties.value.find{it.id == difficultyChoice}?.difficulty}")
        Text(text="Nombre de questions: $numberOfQuestions")


        Button(onClick = {onNavigateToGame(difficultyChoice,numberOfQuestions)}) {
            Text(text="C'est parti!")
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseNumberOfQuestions(options: List<String>,numberOfQuestions:String,setNumberOfQuestion : (number:String) -> Unit ){


    var expanded by remember { mutableStateOf(false) }


    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {

        TextField(
            modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable, enabled=true), // Important pour positionner le menu
            readOnly = true,
            value = numberOfQuestions,
            onValueChange = {},
            label = { Text("Choisir une option") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )

        // Le menu qui s'ouvre
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        setNumberOfQuestion (option)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}
@Composable
fun DifficultyCard(difficulty: DifficultyEntity,
                   setDifficultyChoice: (idDifficulty:Int) -> Unit
                   ){

    var color: Color = MaterialTheme.colorScheme.secondary

    when(difficulty.difficulty){
        "Facile" -> color = MaterialTheme.colorScheme.secondary
        "Moyenne" -> color = MaterialTheme.colorScheme.tertiary
        "Difficile" -> color = MaterialTheme.colorScheme.error
    }

    Card(modifier=Modifier.clickable(onClick={setDifficultyChoice(difficulty.id)})){
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