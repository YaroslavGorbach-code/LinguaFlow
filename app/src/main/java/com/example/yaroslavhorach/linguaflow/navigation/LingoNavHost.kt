package com.example.yaroslavhorach.linguaflow.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.yaroslavhorach.domain.exercise.model.Skill
import com.example.yaroslavhorach.exercises.speaking.navigation.navigateToSpeakingExercise
import com.example.yaroslavhorach.exercises.speaking.navigation.speakingExerciseScreen
import com.example.yaroslavhorach.home.navigation.homeScreen

@Composable
fun LingoNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: String = TopLevelDestination.Home.navigationRoute ?: ""
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
    ) {
        homeScreen(onNavigateToExercise = { exercise ->
            when (exercise.skill) {
                Skill.COMMUNICATION -> {
                    navController.navigateToSpeakingExercise(exercise.id)
                }
                Skill.VOCABULARY -> {

                }
                Skill.DICTION -> {

                }
            }
        })
        speakingExerciseScreen()
    }
}
