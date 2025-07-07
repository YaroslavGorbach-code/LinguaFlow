package com.korop.yaroslavhorach.domain.exercise_content

import com.korop.yaroslavhorach.domain.exercise.model.ExerciseName
import com.korop.yaroslavhorach.domain.exercise_content.model.Situation
import com.korop.yaroslavhorach.domain.exercise_content.model.Test
import com.korop.yaroslavhorach.domain.exercise_content.model.TongueTwister
import com.korop.yaroslavhorach.domain.exercise_content.model.Vocabulary
import com.korop.yaroslavhorach.domain.game.model.Game

interface ExerciseContentRepository {
    suspend fun getSituation(exerciseName: ExerciseName): Situation
    suspend fun getTongueTwister(difficulty: TongueTwister.Difficulty): TongueTwister
    suspend fun getVocabulary(wordType: Vocabulary.WordType): Vocabulary
    suspend fun getTests(exerciseName: ExerciseName): List<Test>
    suspend fun getGameWords(gameName: Game.GameName): List<String>
    suspend fun getGameSentence(gameName: Game.GameName): String
}