package com.example.quizchallenge

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.quizchallenge.data.entities.QuizWithDifficultyAndCategory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

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
    var showSignalmentDialog by remember { mutableStateOf(false)}

    var indexClique by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(quiz) {
        indexClique = null
    }


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
            answers.forEachIndexed  { index , answer ->

                val colorBox = if(indexClique == index){
                    if(answer == quiz.quiz.goodAnswer){
                        Color.Green
                    }else {
                        Color.Red
                    }
                }else{
                    Color.DarkGray
                }
                Box(modifier=Modifier
                    .fillMaxWidth()
                    .padding(5.dp)
                    .border(border = BorderStroke(1.dp,Color.Black), shape = RoundedCornerShape(20))
                    .background(color=colorBox,shape = RoundedCornerShape(20))

                    .clickable(
                        enabled = true,
                        onClick = {
                            indexClique = index
                            if (answer == quiz.quiz.goodAnswer) {
                                incrementPoint()
                            }
                            if (quiz.quiz.additionalInfo != null) {
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
    Column(verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier=Modifier.fillMaxSize()
    ){
        Button(onClick={showSignalmentDialog=true}) {
            Text(text="Signaler une erreur")
        }

        if(showSignalmentDialog){
            ShowSignalmentDialog(idQuiz = quiz.quiz.id,
                onBack={showSignalmentDialog = false}
            )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowSignalmentDialog(
    idQuiz: Int,
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    val options = listOf("Erreur de texte", "Information erronée", "Réponse en doublon", "Autre")
    var expanded by remember { mutableStateOf(false) }
    var errorType by remember { mutableStateOf(options[0]) }
    var textState by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onBack,
        title = {
                    Text(text = "Signalement erreur formulaire",
                        style= MaterialTheme.typography.bodyLarge
                        )
                },
        text = {
            Column{
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    // Le champ de texte qui affiche l'option sélectionnée (comme le bouton du select)
                    TextField(
                        modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable, enabled=true), // Important pour positionner le menu
                        readOnly = true, // Empêche le clavier de s'ouvrir (comportement <select>)
                        value = errorType,
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
                                    errorType = option
                                    expanded = false
                                },
                                contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                            )
                        }
                    }
                }
                Text(text="Commentaire")
                OutlinedTextField(
                    value = textState,
                    onValueChange = { textState = it },
                    label = { Text("Votre message") },
                    modifier = Modifier.fillMaxWidth(),

                    // Configuration pour le transformer en TextArea :
                    singleLine = false,
                    minLines = 4,       // Hauteur initiale de 4 lignes
                    maxLines = 8        // S'agrandit jusqu'à 8 lignes max, puis défile
                )
            }

        },
        dismissButton = {
            Button(onClick = onBack) {
                Text("Annuler")
            }
        },
        confirmButton = {
            Button(onClick = {
                envoyerMailErreurBdd(context,idQuiz,errorType, comment=textState)
                onBack()
            }) {
                Text("Envoyer")
            }
        },
    )
}

fun envoyerMailErreurBdd(context: Context,
                         id: Int,
                         errorType:String,
                         comment:String) {

    Log.d("DEBUG_MAIL", "ID envoyé : |${BuildConfig.SERVICE_ID}|")
    CoroutineScope(Dispatchers.IO).launch {

        val client = OkHttpClient()
        val url = "https://api.emailjs.com/api/v1.0/email/send"


        val serviceId = BuildConfig.SERVICE_ID
        val templateId = BuildConfig.TEMPLATE_ID
        val userId = BuildConfig.USER_ID

        // Le JSON que l'on envoie à EmailJS
        val json = """
            {
              "service_id": "$serviceId",
              "template_id": "$templateId",
              "user_id": "$userId",
              "template_params": {
                "id": "$id",
                "error_type": "$errorType",
                "comment": "$comment"
              }
            }
        """.trimIndent()

        val body = json.toRequestBody("application/json; charset=utf-8".toMediaType())
        val request = Request.Builder().url(url).post(body).build()

        try {
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Signalement pris en compte", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        println(response)
                        Toast.makeText(context, "Erreur serveur : ${response.code}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}



