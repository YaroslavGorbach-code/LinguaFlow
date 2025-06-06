package com.example.yaroslavhorach.data.exercise_content.repository

import android.content.Context
import com.example.yaroslavhorach.common.utill.loadJsonFromAssets
import com.example.yaroslavhorach.domain.exercise.model.ExerciseName
import com.example.yaroslavhorach.domain.exercise_content.ExerciseContentRepository
import com.example.yaroslavhorach.domain.exercise_content.model.Situation
import com.example.yaroslavhorach.domain.exercise_content.model.Test
import com.example.yaroslavhorach.domain.exercise_content.model.TongueTwister
import com.example.yaroslavhorach.domain.exercise_content.model.Vocabulary
import com.example.yaroslavhorach.domain.exercise_content.model.Word
import com.example.yaroslavhorach.domain.game.model.Game
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
    private var cashedWords: MutableMap<Word.WordType, List<Word>> = mutableMapOf()

    override suspend fun getSituation(exerciseName: ExerciseName): Situation {
        if (cashedSituations[exerciseName].isNullOrEmpty()) {
            withContext(Dispatchers.IO) {
                val fileName = when (exerciseName) {
                    ExerciseName.ICEBREAKERS -> "situations/block_one/situations_ICEBREAKERS.json"
                    ExerciseName.FINISH_THE_THOUGHT -> "situations/block_one/situations_FINISH_THE_THOUGHT.json"
                    ExerciseName.WHAT_TO_SAY_NEXT -> "situations/block_one/situations_WHAT_TO_SAY_NEXT.json"
                    ExerciseName.THE_KEY_TO_SMALL_TALK -> "situations/block_one/situations_THE_KEY_TO_SMALL_TALK.json"
                    ExerciseName.FAREWELL_REMARK -> "situations/block_one/situations_FAREWELL_REMARK.json"
                    ExerciseName.THREE_SENTENCES -> "situations/block_two/situations_THREE_SENTENCES.json"
                    ExerciseName.STORY_PUZZLE -> "situations/block_two/situations_STORY_PUZZLE.json"
                    ExerciseName.SAY_DIFFERENTLY -> "situations/block_two/situations_SAY_DIFFERENTLY.json"
                    ExerciseName.ONE_MEMORY -> "situations/block_two/situations_ONE_MEMORY.json"
                    ExerciseName.JOIN_CONVERSATION -> "situations/block_three/situations_JOIN_CONVERSATION.json"
                    ExerciseName.ASK_MORE -> "situations/block_three/situations_ASK_MORE.json"
                    ExerciseName.COMMENT_IN_MOMENT -> "situations/block_three/situations_COMMENT_IN_MOMENT.json"
                    ExerciseName.SMOOTH_RETURN -> "situations/block_three/situations_SMOOTH_RETURN.json"
                    ExerciseName.FIND_THE_TOPIC -> "situations/block_three/situations_FIND_THE_TOPIC.json"
                    ExerciseName.I_THINK_SO_TOO -> "situations/block_three/situations_I_THINK_SO_TOO.json"
                    ExerciseName.GIVE_REASON -> "situations/block_four/situations_GIVE_REASON.json"
                    ExerciseName.WHY_VALUES_MATTER -> "situations/block_four/situations_WHY_VALUES_MATTER.json"
                    ExerciseName.SOFT_DISAGREEMENT -> "situations/block_four/situations_SOFT_DISAGREEMENT.json"
                    ExerciseName.CALM_POSITION -> "situations/block_four/situations_CALM_POSITION.json"
                    ExerciseName.GENTLE_PERSUASION -> "situations/block_four/situations_GENTLE_PERSUASION.json"
                    ExerciseName.EMPATHIC_RESPONSE -> "situations/block_five/situations_EMPATHIC_RESPONSE.json"
                    ExerciseName.EMOTIONAL_TRANSLATOR -> "situations/block_five/situations_EMOTIONAL_TRANSLATOR.json"
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
        withContext(Dispatchers.IO){
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
        }

        // TODO: do not do random here replace in the future
        return cashedTongueTwisters[difficulty]!!.random()
    }

    override suspend fun getVocabulary(wordType: Vocabulary.WordType): Vocabulary {
        withContext(Dispatchers.IO) {
            if (cashedVocabulary[wordType] == null) {
                val fileName = "vocabulary.json"

                val vocabulary: List<Vocabulary> = loadJsonFromAssets(context, fileName)?.let { json ->
                    Gson().fromJson(json, object : TypeToken<List<Vocabulary>>() {}.type)
                } ?: emptyList()

                cashedVocabulary[wordType] = vocabulary.first { it.type == wordType }
            }
        }

        return cashedVocabulary[wordType]!!
    }

    override suspend fun getTests(exerciseName: ExerciseName): List<Test> {
        return withContext(Dispatchers.IO) {
            val fileName = when (exerciseName) {
                ExerciseName.ICEBREAKERS -> "tests/block_one/tests_ICEBREAKERS.json"
                ExerciseName.WHAT_TO_SAY_NEXT -> "tests/block_one/tests_WHAT_TO_SAY_NEXT.json"
                ExerciseName.THE_KEY_TO_SMALL_TALK -> "tests/block_one/tests_THE_KEY_TO_SMALL_TALK.json"
                ExerciseName.FAREWELL_REMARK -> "tests/block_one/tests_FAREWELL_REMARK.json"
                ExerciseName.THREE_SENTENCES -> "tests/block_two/tests_THREE_SENTENCES.json"
                ExerciseName.SAY_DIFFERENTLY -> "tests/block_two/tests_SAY_DIFFERENTLY.json"
                ExerciseName.ASK_MORE -> "tests/block_three/tests_ASK_MORE.json"
                ExerciseName.FIND_THE_TOPIC -> "tests/block_three/tests_FIND_THE_TOPIC.json"
                ExerciseName.I_THINK_SO_TOO -> "tests/block_three/tests_I_THINK_SO_TOO.json"
                ExerciseName.GIVE_REASON -> "tests/block_four/tests_GIVE_REASON.json"
                ExerciseName.WHY_VALUES_MATTER -> "tests/block_four/tests_WHY_VALUES_MATTER.json"
                ExerciseName.SOFT_DISAGREEMENT -> "tests/block_four/tests_SOFT_DISAGREEMENT.json"
                ExerciseName.CALM_POSITION -> "tests/block_four/tests_CALM_POSITTION.json"
                ExerciseName.GENTLE_PERSUASION -> "tests/block_four/tests_GENTLE_PERSUASION.json"
                ExerciseName.EMPATHIC_RESPONSE -> "tests/block_five/tests_EMPATHIC_RESPONSE.json"
                ExerciseName.EMOTIONAL_TRANSLATOR -> "tests/block_five/tests_EMOTIONAL_TRANSLATOR.json"
                else -> ""
            }

            loadJsonFromAssets(context, fileName)?.let { jsonTest ->
                Gson().fromJson(jsonTest, object : TypeToken<List<Test>>() {}.type)
            } ?: emptyList()
        }
    }

    override suspend fun getGameWords(gameName: Game.GameName): List<String> {
        return when (gameName) {
            Game.GameName.RAVEN_LIKE_A_CHAIR -> {
                val words = getWords(Word.WordType.NOUN)
                val uniqueWords = words.distinctBy { it.wordText }

                uniqueWords.shuffled().take(2).map { it.wordText }
            }

            else -> emptyList()
        }
    }

    private suspend fun getWords(wordType: Word.WordType): List<Word> {
        withContext(Dispatchers.IO) {
            if (cashedWords[wordType] == null) {

                val fileName = when (wordType) {
                    Word.WordType.NOUN -> "words/words_nouns.json"
                }

                val words: List<Word> = loadJsonFromAssets(context, fileName)?.let { json ->
                    Gson().fromJson(json, object : TypeToken<List<Word>>() {}.type)
                } ?: emptyList()

                cashedWords[wordType] = words
            }
        }

        return cashedWords[wordType]!!
    }
}