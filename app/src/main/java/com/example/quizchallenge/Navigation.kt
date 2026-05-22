package com.example.quizchallenge

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

object Routes {
    const val ChooseCategory = "choose_category"
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
            ChooseCategoryScreen(chooseCategoryViewModel)
        }
    }
}