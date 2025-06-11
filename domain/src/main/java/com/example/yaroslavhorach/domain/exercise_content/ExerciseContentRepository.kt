package com.example.yaroslavhorach.domain.exercise_content

import com.example.yaroslavhorach.domain.exercise.model.ExerciseName
import com.example.yaroslavhorach.domain.exercise_content.model.Situation
import com.example.yaroslavhorach.domain.exercise_content.model.Test
import com.example.yaroslavhorach.domain.exercise_content.model.TongueTwister
import com.example.yaroslavhorach.domain.exercise_content.model.Vocabulary
import com.example.yaroslavhorach.domain.game.model.Game

interface ExerciseContentRepository {
    suspend fun getSituation(exerciseName: ExerciseName): Situation
    suspend fun getTongueTwister(difficulty: TongueTwister.Difficulty): TongueTwister
    suspend fun getVocabulary(wordType: Vocabulary.WordType): Vocabulary
    suspend fun getTests(exerciseName: ExerciseName): List<Test>
    suspend fun getGameWords(gameName: Game.GameName): List<String>
    suspend fun getGameSentence(gameName: Game.GameName): String
}