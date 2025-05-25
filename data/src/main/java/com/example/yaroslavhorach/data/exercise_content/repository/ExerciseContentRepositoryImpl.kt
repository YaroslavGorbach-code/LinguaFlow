package com.example.yaroslavhorach.data.exercise_content.repository

import android.content.Context
import com.example.yaroslavhorach.common.utill.loadJsonFromAssets
import com.example.yaroslavhorach.domain.exercise.model.ExerciseName
import com.example.yaroslavhorach.domain.exercise_content.ExerciseContentRepository
import com.example.yaroslavhorach.domain.exercise_content.model.Situation
import com.example.yaroslavhorach.domain.exercise_content.model.Test
import com.example.yaroslavhorach.domain.exercise_content.model.TongueTwister
import com.example.yaroslavhorach.domain.exercise_content.model.Vocabulary
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ExerciseContentRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ExerciseContentRepository {
    private var cashedSituations: MutableMap<ExerciseName, List<Situation>> = mutableMapOf()
    private var cashedTongueTwisters: MutableMap<TongueTwister.Difficulty, List<TongueTwister>> = mutableMapOf()
    private var cashedVocabulary: MutableMap<Vocabulary.WordType, Vocabulary> = mutableMapOf()

    override suspend fun getSituation(exerciseName: ExerciseName): Situation {
        if (cashedSituations[exerciseName].isNullOrEmpty()) {
            withContext(Dispatchers.IO) {
                val fileName = when (exerciseName) {
                    ExerciseName.ICEBREAKERS -> "situations_ICEBREAKERS.json"
                    ExerciseName.FINISH_THE_THOUGHT -> "situations_FINISH_THE_THOUGHT.json"
                    ExerciseName.WHAT_TO_SAY_NEXT -> "situations_WHAT_TO_SAY_NEXT.json"
                    ExerciseName.THE_KEY_TO_SMALL_TALK -> "situations_THE_KEY_TO_SMALL_TALK.json"
                    else -> ""
                }

                val situations: List<Situation> = loadJsonFromAssets(context, fileName)?.let { jsonSituations ->
                    Gson().fromJson(jsonSituations, object : TypeToken<List<Situation>>() {}.type)
                } ?: emptyList()

                cashedSituations[exerciseName] = situations
            }
        }

        // TODO: do not do random here replace in the future
        return cashedSituations[exerciseName]!!.random()
    }

    override suspend fun getTongueTwister(difficulty: TongueTwister.Difficulty): TongueTwister {
        if (cashedTongueTwisters[difficulty].isNullOrEmpty()) {
            val fileName = when (difficulty) {
                TongueTwister.Difficulty.EASY -> "tongue_twisters_easy.json"
                TongueTwister.Difficulty.MEDIUM -> "tongue_twisters_easy.json"
                TongueTwister.Difficulty.HARD -> "tongue_twisters_easy.json"
            }

            val twisters: List<TongueTwister> = loadJsonFromAssets(context, fileName)?.let { json ->
                Gson().fromJson(json, object : TypeToken<List<TongueTwister>>() {}.type)
            } ?: emptyList()

            cashedTongueTwisters[difficulty] = twisters
        }

        // TODO: do not do random here replace in the future
        return cashedTongueTwisters[difficulty]!!.random()
    }

    override suspend fun getVocabulary(wordType: Vocabulary.WordType): Vocabulary {
        if (cashedVocabulary[wordType] == null) {
            val fileName = "vocabulary.json"

            val vocabulary: List<Vocabulary> = loadJsonFromAssets(context, fileName)?.let { json ->
                Gson().fromJson(json, object : TypeToken<List<Vocabulary>>() {}.type)
            } ?: emptyList()

            cashedVocabulary[wordType] = vocabulary.first { it.type == wordType }
        }

        return cashedVocabulary[wordType]!!
    }

    override suspend fun getTests(exerciseName: ExerciseName): List<Test> {
        return withContext(Dispatchers.IO) {
            val fileName = when (exerciseName) {
                ExerciseName.ICEBREAKERS -> "tests_ICEBREAKERS.json"
                ExerciseName.WHAT_TO_SAY_NEXT -> "tests_WHAT_TO_SAY_NEXT.json"
                ExerciseName.THE_KEY_TO_SMALL_TALK -> "tests_THE_KEY_TO_SMALL_TALK.json"
                else -> ""
            }

            loadJsonFromAssets(context, fileName)?.let { jsonTest ->
                Gson().fromJson(jsonTest, object : TypeToken<List<Test>>() {}.type)
            } ?: emptyList()
        }
    }
}