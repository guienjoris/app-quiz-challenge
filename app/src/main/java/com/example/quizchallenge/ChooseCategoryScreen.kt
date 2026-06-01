package com.example.quizchallenge

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quizchallenge.data.entities.CategoryEntity


@Composable
fun ChooseCategoryScreen(chooseCategoryViewModel: ChooseCategoryViewModel,
                         onNavigateToChooseDifficulty:(Int)->Unit) {
    val categories = chooseCategoryViewModel.categoriesState.collectAsState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier= Modifier.fillMaxSize().padding(10.dp)
        ){
        Text(
            text="Bienvenue sur Quiz Challenge, sur cette application vous allez mettre votre culture générale à rude épreuve.",
            style= MaterialTheme.typography.labelSmall
            )
        Spacer(modifier=Modifier.height(10.dp))
        Text(
            text="Prenez votre temps il n'y a pas de minuteur c'est surtout pour apprendre des choses à chaque question.",
            style= MaterialTheme.typography.labelSmall
        )
        Spacer(modifier=Modifier.height(25.dp))
        Text(
            text = "Choisissez votre catégorie",
            style= MaterialTheme.typography.bodyLarge,
        )
        Box(modifier= Modifier.verticalScroll(rememberScrollState())){
            FlowRow(modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
                maxItemsInEachRow = 2,
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                categories.value.forEach{ category ->
                    CategoryCard(category,onNavigateToChooseDifficulty)
                }
            }
        }
    }


}

@Composable
fun CategoryCard(category: CategoryEntity,onNavigateToChooseDifficulty: (Int) -> Unit){
    Card(modifier=Modifier.clickable(enabled = true,
        onClick = { onNavigateToChooseDifficulty(category.id) }
        )) {
        Row(horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier= Modifier.padding(5.dp)
            ) {
            Image(painter = painterResource(getResIdByName(category.pictureId)),
                contentDescription = "Image d'illustration des catégories",
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.tint(color= MaterialTheme.colorScheme.primary),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier=Modifier.width(8.dp))
            Text(text=category.name,
                style= MaterialTheme.typography.bodyLarge)
        }
    }
}

