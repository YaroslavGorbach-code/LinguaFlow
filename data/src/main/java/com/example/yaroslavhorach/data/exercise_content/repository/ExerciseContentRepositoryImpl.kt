package com.example.yaroslavhorach.data.exercise_content.repository

import android.content.Context
import com.example.yaroslavhorach.domain.exercise.model.ExerciseName
import com.example.yaroslavhorach.domain.exercise_content.ExerciseContentRepository
import com.example.yaroslavhorach.domain.exercise_content.model.Situation
import com.example.yaroslavhorach.domain.exercise_content.model.Test
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

class ExerciseContentRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ExerciseContentRepository {
    private var cashedSituations: MutableMap<ExerciseName, List<Situation>> = mutableMapOf()

    override suspend fun getSituation(exerciseName: ExerciseName): Situation {
        if (cashedSituations[exerciseName].isNullOrEmpty()) {
            withContext(Dispatchers.IO) {
                val fileName = when (exerciseName) {
                    ExerciseName.ICEBREAKERS -> "situations_ICEBREAKERS.json"
                    ExerciseName.FINISH_THE_THOUGHT -> "situations_FINISH_THE_THOUGHT.json"
                    ExerciseName.WHAT_TO_SAY_NEXT -> "situations_WHAT_TO_SAY_NEXT.json"
                    else -> ""
                }

                val situations: List<Situation> = loadJsonFromAssets(context, fileName)?.let { jsonSituations ->
                    Gson().fromJson(jsonSituations, object : TypeToken<List<Situation>>() {}.type)
                } ?: emptyList()

                cashedSituations[exerciseName] = situations
            }
        }

        return cashedSituations[exerciseName]!!.random()
    }

    override suspend fun getTests(exerciseName: ExerciseName): List<Test> {
        return withContext(Dispatchers.IO) {
            val fileName = when (exerciseName) {
                ExerciseName.ICEBREAKERS -> "tests_ICEBREAKERS.json"
                ExerciseName.WHAT_TO_SAY_NEXT -> "tests_WHAT_TO_SAY_NEXT.json"
                else -> ""
            }

            loadJsonFromAssets(context, fileName)?.let { jsonTest ->
                Gson().fromJson(jsonTest, object : TypeToken<List<Test>>() {}.type)
            } ?: emptyList()
        }
    }

    private fun loadJsonFromAssets(context: Context, fileName: String): String? {
        return try {
            val inputStream = context.assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
            null
        }
    }
}