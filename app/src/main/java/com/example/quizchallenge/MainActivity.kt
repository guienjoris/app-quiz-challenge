package com.example.quizchallenge

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quizchallenge.ui.theme.QuizChallengeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuizChallengeTheme {
                Scaffold(modifier = Modifier.fillMaxSize(),
                    topBar = { QuizChallengeTopAppBar(modifier=Modifier.padding(bottom = 10.dp)) }
                    ) {  innerPadding ->
                    Column(modifier= Modifier.padding(innerPadding)){
                        AppNavigation()
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizChallengeTopAppBar(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                ) {
                Image(
                    modifier = Modifier.fillMaxWidth(),
                    painter = painterResource(R.drawable.ic_launcher),
                    contentScale = ContentScale.Fit,
                    contentDescription = null
                )
                Spacer(modifier=Modifier.width(10.dp))
                Text(text="Quiz challenge")
            }
        },

        modifier = modifier.height(100.dp)
    )
}

@SuppressLint("DiscouragedApi")
@Composable
fun getResIdByName(resName: String?): Int {
    if (resName == null) return R.drawable.ic_launcher // Image par défaut si vide

    val context = LocalContext.current
    // On nettoie le nom au cas où tu as écrit "R.drawable.nom" au lieu de juste "nom"
    val cleanName = resName.replace("R.drawable.", "")

    // C'est l'équivalent de faire R.drawable.$nom
    val resId = context.resources.getIdentifier(cleanName, "drawable", context.packageName)

    return if (resId != 0) resId else R.drawable.ic_launcher // On gère si l'image n'existe pas
}