package com.example.quizchallenge

import android.app.Application
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

object Routes {
    const val ChooseCategory = "choose_category"
    const val ChooseDifficulty = "choose_difficulty"
    const val GameScreen = "game_screen"
}

@Composable
fun AppNavigation(){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.ChooseCategory,
        // Animation quand un écran apparaît
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(500)
            ) + fadeIn(animationSpec = tween(500))
        },
        // Animation quand un écran disparaît (vers l'avant)
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(500)
            ) + fadeOut(animationSpec = tween(500))
        },
        // Animation quand on fait un retour arrière (l'écran précédent réapparaît)
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(500)
            )
        },
        // Animation quand l'écran actuel est détruit (retour arrière)
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(500)
            )
        }
    ){
        composable(Routes.ChooseCategory){
            val chooseCategoryViewModel:ChooseCategoryViewModel = viewModel()
            ChooseCategoryScreen(chooseCategoryViewModel, onNavigateToChooseDifficulty={idCategory:Int ->
                navController.navigate("${Routes.ChooseDifficulty}/${idCategory}")
            })
        }

        composable(route="${Routes.ChooseDifficulty}/{idCategory}",
            arguments = listOf(navArgument("idCategory"){type = NavType.StringType})
            ){ backStackEntry ->

            val chooseDifficultyViewModel:ChooseDifficultyViewModel = viewModel()

            val idCategory = backStackEntry.arguments?.getString("idCategory")?.toInt() ?: 0



            ChooseDifficultyScreen(chooseDifficultyViewModel,
                onBack = { navController.popBackStack(Routes.ChooseCategory, inclusive = false) },
                onNavigateToGame = {idDifficult:Int -> navController.navigate("${Routes.GameScreen}/${idCategory}/${idDifficult}")}
                )
        }
        
        composable(route="${Routes.GameScreen}/{idCategory}/{idDifficulty}",
            arguments= 
                listOf(navArgument("idCategory"){type = NavType.StringType}, 
                navArgument("idDifficulty"){type=
                NavType.StringType}
                )
            ){
            backStackEntry -> 
            
            val idCategory = backStackEntry.arguments?.getString("idCategory")?.toInt() ?: 0
            val idDifficulty = backStackEntry.arguments?.getString("idDifficulty")?.toInt() ?: 0
            
            val gameViewModel: GameViewModel = viewModel()

            LaunchedEffect(idCategory,idDifficulty) {
                gameViewModel.getQuizByDifficultyAndCategory(idCategory,idDifficulty)
            }
            
            GameScreen(gameViewModel,
                onBack = { navController.popBackStack("${Routes.ChooseDifficulty}/${idCategory}", inclusive = false) }
                )
            
        }
    }
}