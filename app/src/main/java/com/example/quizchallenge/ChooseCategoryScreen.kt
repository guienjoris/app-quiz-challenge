package com.example.quizchallenge

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.quizchallenge.data.entities.CategoryEntity


@Composable
fun ChooseCategoryScreen(chooseCategoryViewModel: ChooseCategoryViewModel) {
    val categories = chooseCategoryViewModel.categoriesState.collectAsState()

    Column{
        Text(text="Bienvenue sur Quiz Challenge, sur cette application vous allez mettre votre culture générale à rude épreuve")
        Text(text = "Choisissez votre catégorie")
        Box(){
            FlowRow(modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
                maxItemsInEachRow = 2,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                categories.value.forEach{ category ->
                    CategoryCard(category)
                }
            }
        }
    }


}

@Composable
fun CategoryCard(category: CategoryEntity){
    Card() {
        Row(horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier= Modifier.padding(5.dp)
            ) {
            Image(painter = painterResource(getResIdByName(category.pictureId)),
                contentDescription = "Image d'illustration des catégories",
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier=Modifier.width(8.dp))
            Text(text=category.name)
        }
    }
}