package com.korop.yaroslavhorach.linguaflow.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navOptions
import com.korop.yaroslavhorach.avatar_change.navigation.avatarChangeScreen
import com.korop.yaroslavhorach.avatar_change.navigation.navigateToAvatarChange
import com.korop.yaroslavhorach.block_is_locked.navigation.blockIsLockedScreen
import com.korop.yaroslavhorach.block_is_locked.navigation.navigateToBlockIsLocked
import com.korop.yaroslavhorach.block_practice.navigation.blockPracticeScreen
import com.korop.yaroslavhorach.block_practice.navigation.navigateToBlockPractice
import com.korop.yaroslavhorach.designsystem.screens.game_unlocked_success.navigation.gameUnlockedScreen
import com.korop.yaroslavhorach.designsystem.screens.game_unlocked_success.navigation.navigateToGameUnlockedSuccess
import com.korop.yaroslavhorach.designsystem.screens.onboarding.navigation.OnboardingRoute
import com.korop.yaroslavhorach.designsystem.screens.onboarding.navigation.onboardingScreen
import com.korop.yaroslavhorach.designsystem.screens.premium.navigation.navigateToPremium
import com.korop.yaroslavhorach.designsystem.screens.premium.navigation.navigateToPremiumWithPopBack
import com.korop.yaroslavhorach.designsystem.screens.premium.navigation.premiumScreen
import com.korop.yaroslavhorach.designsystem.screens.premium_success.navigation.navigateToPremiumSuccess
import com.korop.yaroslavhorach.designsystem.screens.premium_success.navigation.premiumSuccessScreen
import com.korop.yaroslavhorach.designsystem.screens.rate.navigation.navigateToRateApp
import com.korop.yaroslavhorach.designsystem.screens.rate.navigation.rateAppScreen
import com.korop.yaroslavhorach.domain.exercise.model.ExerciseBlock
import com.korop.yaroslavhorach.domain.exercise.model.Skill
import com.korop.yaroslavhorach.domain.game.model.Game
import com.korop.yaroslavhorach.domain.holders.OpenGameDetailsHolder
import com.korop.yaroslavhorach.exercises.exercise_completed.navigation.exerciseCompletedScreen
import com.korop.yaroslavhorach.exercises.exercise_completed.navigation.navigateToExerciseCompleted
import com.korop.yaroslavhorach.exercises.speaking.navigation.navigateToSpeakingExercise
import com.korop.yaroslavhorach.exercises.speaking.navigation.navigateToSpeakingExerciseWithBack
import com.korop.yaroslavhorach.exercises.speaking.navigation.speakingExerciseScreen
import com.korop.yaroslavhorach.exercises.tongue_twisters.navigation.navigateToTongueTwistersExercise
import com.korop.yaroslavhorach.exercises.tongue_twisters.navigation.tongueTwistersExerciseScreen
import com.korop.yaroslavhorach.exercises.vocabulary.navigation.navigateToVocabularyExercise
import com.korop.yaroslavhorach.exercises.vocabulary.navigation.vocabularyExerciseScreen
import com.korop.yaroslavhorach.games.words_game.navigation.navigateToWordsGame
import com.korop.yaroslavhorach.games.words_game.navigation.wordsGameScreen
import com.korop.yaroslavhorach.games.navigation.HomeRoute
import com.korop.yaroslavhorach.games.navigation.gamesScreen
import com.korop.yaroslavhorach.games.navigation.homeScreen
import com.korop.yaroslavhorach.games.navigation.navigateToGames
import com.korop.yaroslavhorach.profile.navigation.profileScreen
import com.korop.yaroslavhorach.settings.navigation.navigateToSettings
import com.korop.yaroslavhorach.settings.navigation.settingsScreen

@Composable
fun LingoNavHost(
    isOnboarding: Boolean,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onChangeColorScheme: (primary: Color, secondary: Color) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = if (isOnboarding) OnboardingRoute else HomeRoute,
        modifier = modifier,
    ) {
        profileScreen(
            onNavigateToAvatarChange = {
                navController.navigateToAvatarChange()
            }, onNavigateToPremium = {
                navController.navigateToPremium()
            },
            onNavigateToSettings = {
                navController.navigateToSettings()
            }
        )
        settingsScreen { navController.popBackStack() }
        onboardingScreen { navController.navigateToAvatarChange() }
        homeScreen(onNavigateToExercise = { exercise ->
            when (exercise.skill) {
                Skill.COMMUNICATION -> {
                    navController.navigateToSpeakingExercise(exercise.id, null)
                }

                Skill.VOCABULARY -> {
                    navController.navigateToVocabularyExercise(exercise.id)
                }

                Skill.DICTION -> {
                    navController.navigateToTongueTwistersExercise(exercise.id)
                }
            }
        }, onChangeColorScheme = onChangeColorScheme,
            onNavigateToAvatarChange = { navController.navigateToAvatarChange() },
            onNavigateToBlockRepeat = { block: ExerciseBlock -> navController.navigateToBlockPractice(block) },
            onNavigateToBlockIsLocked = { block: ExerciseBlock -> navController.navigateToBlockIsLocked(block) },
        )
        gamesScreen(onNavigateToGame = { id, name ->
            when (name) {
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
                Game.GameName.ONE_LETTER,
                Game.GameName.WORST_IN_THE_WORLD,
                Game.GameName.FIVE_TO_THE_POINT,
                Game.GameName.VOCABULARY_BURST,
                Game.GameName.QUICK_ASSOCIATION,
                Game.GameName.WORD_BY_CATEGORY,
                Game.GameName.UNUSUAL_PROBLEM_SOLVER,
                Game.GameName.BREATHLINE_CHALLENGE,
                Game.GameName.CONSONANT_BATTLE,
                Game.GameName.WORD_CANNON,
                Game.GameName.HOT_WORD -> {
                    navController.navigateToWordsGame(id)
                }

                Game.GameName.VOCABULARY -> {
                    navController.navigateToVocabularyExercise(1004)
                }

                Game.GameName.TONGUE_TWISTERS_EASY -> {
                    navController.navigateToTongueTwistersExercise(1001)
                }

                Game.GameName.TONGUE_TWISTERS_MEDIUM -> {
                    navController.navigateToTongueTwistersExercise(1002)
                }

                Game.GameName.TONGUE_TWISTERS_HARD -> {
                    navController.navigateToTongueTwistersExercise(1003)
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
        exerciseCompletedScreen(onNavigateBack = {
            navController.popBackStack()
        }, onNavigateToGameUnlocked = {
            navController.navigateToGameUnlockedSuccess(it)
        }, onNavigateToRateApp = {
            navController.navigateToRateApp()
        })
        premiumScreen(onNavigateBack = {
            navController.popBackStack()
        }, onNavigateToSuccess = {
            navController.navigateToPremiumSuccess()
        })
        premiumSuccessScreen {
            navController.popBackStack()
        }
        avatarChangeScreen(navigateBack = {
            navController.popBackStack()
        }, navigateToPremium = {
            navController.navigateToPremium()
        })
        rateAppScreen(onNavigateBack = {
            navController.popBackStack()
        })
        gameUnlockedScreen(onNavigateToGame = {
            val topLevelNavOptions = navOptions {
                // Pop up to the start destination of the graph to
                // avoid building up a large stack of destinations
                // on the back stack as users select items
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }

            navController.popBackStack()
            navController.navigateToGames(topLevelNavOptions)

            OpenGameDetailsHolder.gameIdToOpen.value = it


        }, onNavigateBack = {
            navController.popBackStack()
        }
        )
        blockPracticeScreen(onNavigateExercises = {
            navController.navigateToSpeakingExerciseWithBack(exerciseId = null, blockName = it)
        }, onNavigateBack = {
            navController.popBackStack()
        })
        blockIsLockedScreen(onNavigateToPremium = {
            navController.navigateToPremiumWithPopBack()
        }, onNavigateBack = {
            navController.popBackStack()
        })
    }
}
