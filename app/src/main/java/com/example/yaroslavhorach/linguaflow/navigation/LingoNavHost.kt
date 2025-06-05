package com.example.yaroslavhorach.linguaflow.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.example.yaroslavhorach.domain.exercise.model.Skill
import com.example.yaroslavhorach.exercises.exercise_completed.navigation.ExerciseCompletedRoute
import com.example.yaroslavhorach.exercises.exercise_completed.navigation.exerciseCompletedScreen
import com.example.yaroslavhorach.exercises.exercise_completed.navigation.navigateToExerciseCompleted
import com.example.yaroslavhorach.exercises.speaking.navigation.navigateToSpeakingExercise
import com.example.yaroslavhorach.exercises.speaking.navigation.speakingExerciseScreen
import com.example.yaroslavhorach.exercises.tongue_twisters.navigation.navigateToTongueTwistersExercise
import com.example.yaroslavhorach.exercises.tongue_twisters.navigation.tongueTwistersExerciseScreen
import com.example.yaroslavhorach.exercises.vocabulary.navigation.navigateToVocabularyExercise
import com.example.yaroslavhorach.exercises.vocabulary.navigation.vocabularyExerciseScreen
import com.example.yaroslavhorach.games.words_game.navigation.WordsGameNavigationRoute
import com.example.yaroslavhorach.games.words_game.navigation.navigateToWordsGame
import com.example.yaroslavhorach.games.words_game.navigation.wordsGameScreen
import com.example.yaroslavhorach.home.navigation.GamesRoute
import com.example.yaroslavhorach.home.navigation.HomeRoute
import com.example.yaroslavhorach.home.navigation.gamesScreen
import com.example.yaroslavhorach.home.navigation.homeScreen
import com.example.yaroslavhorach.linguaflow.ui.rememberLingoAppState

@Composable
fun LingoNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onChangeColorScheme: (primary: Color, secondary: Color) -> Unit
) {

    NavHost(
        navController = navController,
        startDestination = HomeRoute,
        modifier = modifier,
    ) {
        homeScreen(onNavigateToExercise = { exercise ->
            when (exercise.skill) {
                Skill.COMMUNICATION -> {
                    navController.navigateToSpeakingExercise(exercise.id)
                }
                Skill.VOCABULARY -> {
                    navController.navigateToVocabularyExercise(exercise.id)
                }
                Skill.DICTION -> {
                    navController.navigateToTongueTwistersExercise(exercise.id)
                }
            }
        }, onChangeColorScheme)
        gamesScreen { id ->
            navController.navigateToWordsGame(id)
        }
        wordsGameScreen(onNavigateBack = {
            navController.popBackStack()
        }, onNavigateToExerciseCompleted = { time, xp ->
            navController.navigateToExerciseCompleted(xp, time)
        })
        speakingExerciseScreen(onNavigateBack = {
            navController.popBackStack()
        }, onNavigateToExerciseCompleted = { time, xp ->
            navController.navigateToExerciseCompleted(xp, time)
        })
        tongueTwistersExerciseScreen(onNavigateBack = {
            navController.popBackStack()
        }, onNavigateToExerciseCompleted = { time, xp ->
            navController.navigateToExerciseCompleted(xp, time)
        })
        vocabularyExerciseScreen(onNavigateBack = {
            navController.popBackStack()
        }, onNavigateToExerciseCompleted = { time, xp ->
            navController.navigateToExerciseCompleted(xp, time)
        })
        exerciseCompletedScreen {
            navController.popBackStack()
        }
    }
}
