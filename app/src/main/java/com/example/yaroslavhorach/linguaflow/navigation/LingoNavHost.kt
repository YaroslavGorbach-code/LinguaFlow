package com.example.yaroslavhorach.linguaflow.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.yaroslavhorach.avatar_change.navigation.avatarChangeScreen
import com.example.yaroslavhorach.avatar_change.navigation.navigateToAvatarChange
import com.example.yaroslavhorach.designsystem.screens.premium.navigation.navigateToPremium
import com.example.yaroslavhorach.designsystem.screens.premium.navigation.premiumScreen
import com.example.yaroslavhorach.domain.exercise.model.Skill
import com.example.yaroslavhorach.domain.game.model.Game
import com.example.yaroslavhorach.exercises.exercise_completed.navigation.exerciseCompletedScreen
import com.example.yaroslavhorach.exercises.exercise_completed.navigation.navigateToExerciseCompleted
import com.example.yaroslavhorach.exercises.speaking.navigation.navigateToSpeakingExercise
import com.example.yaroslavhorach.exercises.speaking.navigation.speakingExerciseScreen
import com.example.yaroslavhorach.exercises.tongue_twisters.navigation.navigateToTongueTwistersExercise
import com.example.yaroslavhorach.exercises.tongue_twisters.navigation.tongueTwistersExerciseScreen
import com.example.yaroslavhorach.exercises.vocabulary.navigation.navigateToVocabularyExercise
import com.example.yaroslavhorach.exercises.vocabulary.navigation.vocabularyExerciseScreen
import com.example.yaroslavhorach.games.words_game.navigation.navigateToWordsGame
import com.example.yaroslavhorach.games.words_game.navigation.wordsGameScreen
import com.example.yaroslavhorach.home.navigation.HomeRoute
import com.example.yaroslavhorach.home.navigation.gamesScreen
import com.example.yaroslavhorach.home.navigation.homeScreen
import com.example.yaroslavhorach.profile.navigation.profileScreen

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
        profileScreen(onNavigateToAvatarChange = {
            navController.navigateToAvatarChange()
        }, onNavigateToPremium = {
            navController.navigateToPremium()
        })

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
        }, onChangeColorScheme = onChangeColorScheme, onNavigateToAvatarChange = {
            navController.navigateToAvatarChange()
        })
        gamesScreen(onNavigateToGame = { id, name ->
                when(name){
                    Game.GameName.RAVEN_LIKE_A_CHAIR,
                    Game.GameName.FOUR_WORDS_ONE_STORY,
                    Game.GameName.TALK_TILL_EXHAUSTED,
                    Game.GameName.SELL_THIS_THING,
                    Game.GameName.DEFINE_PRECISELY,
                    Game.GameName.BIG_ANSWER,
                    Game.GameName.EMOTIONAL_TRANSLATOR,
                    Game.GameName.DEVILS_ADVOCATE,
                    Game.GameName.DIALOGUE_WITH_SELF,
                    Game.GameName.IMAGINARY_SITUATION,
                    Game.GameName.EMOTION_TO_FACT,
                    Game.GameName.WHO_AM_I_MONOLOGUE,
                    Game.GameName.I_AM_EXPERT,
                    Game.GameName.FORBIDDEN_WORDS,
                    Game.GameName.BODY_LANGUAGE_EXPRESS,
                    Game.GameName.RAP_IMPROV,
                    Game.GameName.PERSUASIVE_SHOUT,
                    Game.GameName.SUBTLE_MANIPULATION,
                    Game.GameName.ONE_SYNONYM_PLEASE,
                    Game.GameName.INTONATION_MASTER,
                    Game.GameName.ANTONYM_BATTLE,
                    Game.GameName.RHYME_LIGHTNING,
                    Game.GameName.FUNNIEST_ANSWER,
                    Game.GameName.MADMAN_ANNOUNCEMENT,
                    Game.GameName.FUNNY_EXCUSE,
                    Game.GameName.ONE_WORD_MANY_MEANINGS,
                    Game.GameName.FLIRTING_WITH_OBJECT,
                    Game.GameName.BOTH_THERE_AND_IN_BED,
                    Game.GameName.HOT_WORD -> {
                        navController.navigateToWordsGame(id)
                    }
                    Game.GameName.WORD_IN_TEMPO -> {
                        navController.navigateToVocabularyExercise(4)
                    }
                    Game.GameName.TONGUE_TWISTER_EASY -> {
                        navController.navigateToTongueTwistersExercise(3)
                    }
                    Game.GameName.TONGUE_TWISTER_MEDIUM -> {
                        navController.navigateToTongueTwistersExercise(20)
                    }
                    Game.GameName.TONGUE_TWISTER_HARD -> {
                        navController.navigateToTongueTwistersExercise(37)
                    }
                }
            }, onNavigateToPremium = {
                navController.navigateToPremium()
        }
        )
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
        premiumScreen {
            navController.popBackStack()
        }
        avatarChangeScreen {
            navController.popBackStack()
        }
    }
}
