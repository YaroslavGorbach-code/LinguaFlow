package com.korop.yaroslavhorach.data.exercise_content.repository

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.korop.yaroslavhorach.common.utill.loadJsonFromAssets
import com.korop.yaroslavhorach.domain.exercise.model.ExerciseName
import com.korop.yaroslavhorach.domain.exercise_content.ExerciseContentRepository
import com.korop.yaroslavhorach.domain.exercise_content.model.Sentence
import com.korop.yaroslavhorach.domain.exercise_content.model.Situation
import com.korop.yaroslavhorach.domain.exercise_content.model.Test
import com.korop.yaroslavhorach.domain.exercise_content.model.TongueTwister
import com.korop.yaroslavhorach.domain.exercise_content.model.Vocabulary
import com.korop.yaroslavhorach.domain.exercise_content.model.Word
import com.korop.yaroslavhorach.domain.game.model.Game
import com.korop.yaroslavhorach.domain.prefs.PrefsRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.korop.yaroslavhorach.datastore.prefs.LinguaPrefsDataSource
import com.korop.yaroslavhorach.domain.exercise.model.ExerciseBlock
import com.korop.yaroslavhorach.domain.exercise.model.getExercisesByBlockName
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject
import kotlin.random.Random

class ExerciseContentRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val prefsRepository: PrefsRepository
) : ExerciseContentRepository {
    private var cashedSituations: MutableMap<ExerciseName, List<Situation>> = mutableMapOf()
    private var cashedTongueTwisters: MutableMap<TongueTwister.Difficulty, List<TongueTwister>> = mutableMapOf()
    private var cashedVocabulary: MutableMap<Vocabulary.WordType, Vocabulary> = mutableMapOf()
    private var cashedWords: MutableMap<Word.WordType, List<Word>> = mutableMapOf()
    private var cashedSentences: MutableMap<Sentence.SentenceType, List<Sentence>> = mutableMapOf()

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
                    ExerciseName.CONSPIRACY_BUILDER -> "situations/block_six/situations_CONSPIRACY_BUILDER.json"
                    ExerciseName.REVERSE_STORYTELLING -> "situations/block_six/situations_REVERSE_STORYTELLING.json"
                    ExerciseName.MAKE_IT_FASCINATING -> "situations/block_six/situations_MAKE_IT_FASCINATING.json"
                    ExerciseName.OVERDRAMATIC_TRIVIALITY -> "situations/block_six/situations_OVERDRAMATIC_TRIVIALITY.json"
                    ExerciseName.IRONIC_RESPONSE -> "situations/block_seven/situations_IRONIC_RESPONSE.json"
                    ExerciseName.AWKWARD_SITUATION -> "situations/block_seven/situations_AWKWARD_SITUATION.json"
                    ExerciseName.SUPPORT_WITH_HUMOR -> "situations/block_seven/situations_SUPPORT_WITH_HUMOR.json"
                    ExerciseName.IRONIC_DOMESTIC_REACTION -> "situations/block_seven/situations_IRONIC_DOMESTIC_REACTION.json"
                    ExerciseName.FLIRTY_REPLY -> "situations/block_seven/situations_FLIRTY_REPLY.json"
                    ExerciseName.FLIRT_SUSPECT -> "situations/block_seven/situations_FLIRTY_SUSPECT.json"
                    else -> ""
                }

                val situations: List<Situation> = loadJsonFromAssets(context, fileName)?.let { jsonSituations ->
                    Gson().fromJson(jsonSituations, object : TypeToken<List<Situation>>() {}.type)
                } ?: emptyList()

                cashedSituations[exerciseName] = situations
            }
        }

        return getAndUseSituation(exerciseName)
    }

    override suspend fun getSituation(block: ExerciseBlock): Situation {
        return getSituation(getExercisesByBlockName(block).random())
    }

    override suspend fun getTongueTwister(difficulty: TongueTwister.Difficulty): TongueTwister {
        withContext(Dispatchers.IO) {
            if (cashedTongueTwisters[difficulty].isNullOrEmpty()) {
                val fileName = when (difficulty) {
                    TongueTwister.Difficulty.EASY -> "tongue_twisters_easy.json"
                    TongueTwister.Difficulty.MEDIUM -> "tongue_twisters_medium.json"
                    TongueTwister.Difficulty.HARD -> "tongue_twisters_hard.json"
                }

                val twisters: List<TongueTwister> = loadJsonFromAssets(context, fileName)?.let { json ->
                    Gson().fromJson(json, object : TypeToken<List<TongueTwister>>() {}.type)
                } ?: emptyList()

                cashedTongueTwisters[difficulty] = twisters
            }
        }

        return getAndUseTongueTwister(difficulty)
    }

    private suspend fun getAndUseSituation(exerciseName: ExerciseName): Situation {
        val used = prefsRepository.getUsedContent(exerciseName.name).first()

        val unusedSituations = cashedSituations[exerciseName]
            ?.filter { sit -> sit.id !in used }

        if (unusedSituations.isNullOrEmpty()) {
            prefsRepository.clearUsedExerciseContent(exerciseName.name)

            val allSituations = cashedSituations[exerciseName].orEmpty()
            val situation = allSituations.random()

            prefsRepository.useExerciseContent(situation.id, exerciseName.name)

            return situation
        } else {
            val situation = unusedSituations.random()
            prefsRepository.useExerciseContent(situation.id, exerciseName.name)

            return situation
        }
    }

    private suspend fun getAndUseTongueTwister(difficulty: TongueTwister.Difficulty): TongueTwister {
        val used = prefsRepository.getUsedContent(difficulty.name).first()

        val unusedTongueTwisters = cashedTongueTwisters[difficulty]
            ?.filter { tt -> tt.id !in used }

        if (unusedTongueTwisters.isNullOrEmpty()) {
            prefsRepository.clearUsedExerciseContent(difficulty.name)

            val allTwisters = cashedTongueTwisters[difficulty].orEmpty()
            val twister = allTwisters.random()

            prefsRepository.useExerciseContent(twister.id, difficulty.name)

            return twister
        } else {
            val twister = unusedTongueTwisters.random()

            prefsRepository.useExerciseContent(twister.id, difficulty.name)

            return twister
        }
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
        val lang: String = (AppCompatDelegate.getApplicationLocales()[0] ?: Locale.getDefault()).language

        return when (gameName) {
            Game.GameName.RAVEN_LIKE_A_CHAIR -> {
                val words = getWords(Word.WordType.NOUN)
                val uniqueWords = words.distinctBy { it.getWordText(lang) }

                uniqueWords.shuffled().take(2).map { it.getWordText(lang) }
            }
            Game.GameName.FOUR_WORDS_ONE_STORY -> {
                val words = getWords(Word.WordType.NOUN)
                val uniqueWords = words.distinctBy { it.getWordText(lang) }

                uniqueWords.shuffled().take(4).map { it.getWordText(lang) }
            }
            Game.GameName.TALK_TILL_EXHAUSTED -> {
                val words = getWords(Word.WordType.NOUN)
                val uniqueWords = words.distinctBy { it.getWordText(lang) }

                uniqueWords.shuffled().take(1).map { it.getWordText(lang) }
            }
            Game.GameName.SELL_THIS_THING -> {
                val words = getWords(Word.WordType.NOUN)
                val uniqueWords = words.distinctBy { it.getWordText(lang) }

                uniqueWords.shuffled().take(1).map { it.getWordText(lang) }
            }
            Game.GameName.DEFINE_PRECISELY -> {
                val words = getWords(Word.WordType.NOUN)
                val uniqueWords = words.distinctBy { it.getWordText(lang) }

                uniqueWords.shuffled().take(1).map { it.getWordText(lang) }
            }
            Game.GameName.RAP_IMPROV -> {
                val words = getWords(Word.WordType.NOUN)
                    .distinctBy { it.getWordText(lang) }
                    .shuffled()
                    .map { it.getWordText(lang) }

                words.take(Random.nextInt(3, 6)).toSet().toList()
            }
            Game.GameName.ONE_WORD_MANY_MEANINGS -> {
                val words = getWords(Word.WordType.NOUN)
                val uniqueWords = words.distinctBy { it.getWordText(lang) }

                uniqueWords.shuffled().take(1).map { it.getWordText(lang) }
            }
            Game.GameName.FLIRTING_WITH_OBJECT -> {
                val words = getWords(Word.WordType.NOUN)
                val uniqueWords = words.distinctBy { it.getWordText(lang) }

                uniqueWords.shuffled().take(1).map { it.getWordText(lang) }
            }
            Game.GameName.BOTH_THERE_AND_IN_BED -> {
                val words = getWords(Word.WordType.PLACE)
                val uniqueWords = words.distinctBy { it.getWordText(lang) }

                uniqueWords.shuffled().take(1).map { it.getWordText(lang) }
            }
            Game.GameName.HOT_WORD -> {
                val words = getWords(Word.WordType.HOT)
                val uniqueWords = words.distinctBy { it.getWordText(lang) }

                uniqueWords.shuffled().take(1).map { it.getWordText(lang) }
            }
            Game.GameName.QUICK_ASSOCIATION -> {
                val words = getWords(Word.WordType.NOUN)
                val uniqueWords = words.distinctBy { it.getWordText(lang) }

                uniqueWords.shuffled().take(1).map { it.getWordText(lang) }
            }
            Game.GameName.ONE_LETTER -> {
                val words = getWords(Word.WordType.ALPHABET)
                val uniqueWords = words.distinctBy { it.getWordText(lang) }

                uniqueWords.shuffled().take(1).map { it.getWordText(lang) }
            }
            Game.GameName.WORST_IN_THE_WORLD -> {
                val words = getWords(Word.WordType.PROFESSIONS)
                val uniqueWords = words.distinctBy { it.getWordText(lang) }

                uniqueWords.shuffled().take(1).map { it.getWordText(lang) }
            }
            Game.GameName.FIVE_TO_THE_POINT -> {
                val words = getWords(Word.WordType.CATEGORIES)
                val uniqueWords = words.distinctBy { it.getWordText(lang) }

                uniqueWords.shuffled().take(1).map { it.getWordText(lang) }
            }
            Game.GameName.UNUSUAL_PROBLEM_SOLVER -> {
                val words = getWords(Word.WordType.CATEGORIES)
                val uniqueWords = words.distinctBy { it.getWordText(lang) }

                uniqueWords.shuffled().take(1).map { it.getWordText(lang) }
            }
            Game.GameName.VOCABULARY_BURST -> {
                val words = getWords(Word.WordType.ALPHABET)
                val uniqueWords = words.distinctBy { it.getWordText(lang) }

                uniqueWords.shuffled().take(1).map { it.getWordText(lang) }
            }
            Game.GameName.ANTONYM_BATTLE -> {
                val words = getWords(Word.WordType.ANTONIM)
                val uniqueWords = words.distinctBy { it.getWordText(lang) }

                uniqueWords.shuffled().take(1).map { it.getWordText(lang) }
            }
            Game.GameName.RHYME_LIGHTNING -> {
                val words = getWords(Word.WordType.NOUN)
                val uniqueWords = words.distinctBy { it.getWordText(lang) }

                uniqueWords.shuffled().take(1).map { it.getWordText(lang) }
            }
            Game.GameName.WORD_BY_CATEGORY -> {
                val words = getWords(Word.WordType.ALPHABET)
                val uniqueAlphabet = words.distinctBy { it.getWordText(lang) }

                val categories = getWords(Word.WordType.CATEGORIES)
                val uniqueCategories = categories.distinctBy { it.getWordText(lang) }

                listOf(uniqueAlphabet.shuffled().take(1).map { it.getWordText(lang) }.first().toString() +
                        "\n" + uniqueCategories.shuffled().take(1).map { it.getWordText(lang) }.first().toString())
            }
            else -> emptyList()
        }
    }

    override suspend fun getGameSentence(gameName: Game.GameName): String {
        val lang: String = (AppCompatDelegate.getApplicationLocales()[0] ?: Locale.getDefault()).language

        return when (gameName) {
            Game.GameName.BIG_ANSWER -> {
                val sentences = getSentences(Sentence.SentenceType.SIMPLE_QUESTION)
                val uniqueSentences = sentences.distinctBy { it.getText(lang) }

                uniqueSentences.shuffled().first().getText(lang)
            }
            Game.GameName.EMOTIONAL_TRANSLATOR -> {
                val sentences = getSentences(Sentence.SentenceType.EMOTIONAL_TRANSLATION)
                val uniqueSentences = sentences.distinctBy { it.getText(lang) }

                uniqueSentences.shuffled().first().getText(lang)
            }
            Game.GameName.DEVILS_ADVOCATE -> {
                val sentences = getSentences(Sentence.SentenceType.DEVILS_ADVOCATE)
                val uniqueSentences = sentences.distinctBy { it.getText(lang) }

                uniqueSentences.shuffled().first().getText(lang)
            }
            Game.GameName.DIALOGUE_WITH_SELF -> {
                val sentences = getSentences(Sentence.SentenceType.DIALOGUE_WITH_SELF)
                val uniqueSentences = sentences.distinctBy { it.getText(lang) }

                uniqueSentences.shuffled().first().getText(lang)
            }
            Game.GameName.IMAGINARY_SITUATION -> {
                val sentences = getSentences(Sentence.SentenceType.IMAGINARY_SITUATION)
                val uniqueSentences = sentences.distinctBy { it.getText(lang) }

                uniqueSentences.shuffled().first().getText(lang)
            }
            Game.GameName.EMOTION_TO_FACT -> {
                val sentences = getSentences(Sentence.SentenceType.EMOTION_TO_FACT)
                val uniqueSentences = sentences.distinctBy { it.getText(lang) }

                uniqueSentences.shuffled().first().getText(lang)
            }
            Game.GameName.WHO_AM_I_MONOLOGUE -> {
                val sentences = getSentences(Sentence.SentenceType.WHO_AM_I)
                val uniqueSentences = sentences.distinctBy { it.getText(lang) }

                uniqueSentences.shuffled().first().getText(lang)
            }
            Game.GameName.I_AM_EXPERT -> {
                val sentences = getSentences(Sentence.SentenceType.I_AM_EXPERT)
                val uniqueSentences = sentences.distinctBy { it.getText(lang) }

                uniqueSentences.shuffled().first().getText(lang)
            }
            Game.GameName.FORBIDDEN_WORDS -> {
                val sentences = getSentences(Sentence.SentenceType.FORBIDDEN_WORDS)
                val uniqueSentences = sentences.distinctBy { it.getText(lang) }

                uniqueSentences.shuffled().first().getText(lang)
            }
            Game.GameName.BODY_LANGUAGE_EXPRESS -> {
                val sentences = getSentences(Sentence.SentenceType.BODY_LANGUAGE)
                val uniqueSentences = sentences.distinctBy { it.getText(lang) }

                uniqueSentences.shuffled().first().getText(lang)
            }
            Game.GameName.PERSUASIVE_SHOUT -> {
                val sentences = getSentences(Sentence.SentenceType.PERSUASIVE_SHOUT)
                val uniqueSentences = sentences.distinctBy { it.getText(lang) }

                uniqueSentences.shuffled().first().getText(lang)
            }
            Game.GameName.SUBTLE_MANIPULATION -> {
                val sentences = getSentences(Sentence.SentenceType.SUBTLE_MANIPULATION)
                val uniqueSentences = sentences.distinctBy { it.getText(lang) }

                uniqueSentences.shuffled().first().getText(lang)
            }
            Game.GameName.UNUSUAL_PROBLEM_SOLVER -> {
                val sentences = getSentences(Sentence.SentenceType.PROBLEMS)
                val uniqueSentences = sentences.distinctBy { it.getText(lang) }

                uniqueSentences.shuffled().first().getText(lang)
            }
            Game.GameName.ONE_SYNONYM_PLEASE -> {
                val sentences = getSentences(Sentence.SentenceType.ONE_SYNONYM_PLEASE)
                val uniqueSentences = sentences.distinctBy { it.getText(lang) }

                uniqueSentences.shuffled().first().getText(lang)
            }
            Game.GameName.INTONATION_MASTER -> {
                val sentences = getSentences(Sentence.SentenceType.INTONATION_MASTER)
                val uniqueSentences = sentences.distinctBy { it.getText(lang) }

                uniqueSentences.shuffled().first().getText(lang)
            }
            Game.GameName.FUNNIEST_ANSWER -> {
                val sentences = getSentences(Sentence.SentenceType.FUNNIEST_ANSWER)
                val uniqueSentences = sentences.distinctBy { it.getText(lang) }

                uniqueSentences.shuffled().first().getText(lang)
            }
            Game.GameName.MADMAN_ANNOUNCEMENT -> {
                val sentences = getSentences(Sentence.SentenceType.SELL_THE_MADNESS)
                val uniqueSentences = sentences.distinctBy { it.getText(lang) }

                uniqueSentences.shuffled().first().getText(lang)
            }
            Game.GameName.FUNNY_EXCUSE -> {
                val sentences = getSentences(Sentence.SentenceType.FUNNY_EXCUSE)
                val uniqueSentences = sentences.distinctBy { it.getText(lang) }

                uniqueSentences.shuffled().first().getText(lang)
            }
            else -> ""
        }
    }

    private suspend fun getWords(wordType: Word.WordType): List<Word> {
        withContext(Dispatchers.IO) {
            if (cashedWords[wordType] == null) {

                val fileName = when (wordType) {
                    Word.WordType.NOUN -> "words/words_nouns.json"
                    Word.WordType.PLACE -> "words/words_places.json"
                    Word.WordType.HOT -> "words/words_hot.json"
                    Word.WordType.ANTONIM -> "words/words_antonims.json"
                    Word.WordType.ALPHABET -> "words/alphabet.json"
                    Word.WordType.CATEGORIES -> "words/words_categories.json"
                    Word.WordType.PROFESSIONS -> "words/words_proffesions.json"
                }

                val words: List<Word> = loadJsonFromAssets(context, fileName)?.let { json ->
                    Gson().fromJson(json, object : TypeToken<List<Word>>() {}.type)
                } ?: emptyList()

                cashedWords[wordType] = words
            }
        }

        return cashedWords[wordType]!!
    }

    private suspend fun getSentences(sentenceType: Sentence.SentenceType): List<Sentence> {
        withContext(Dispatchers.IO) {
            if (cashedSentences[sentenceType] == null) {

                val fileName = when (sentenceType) {
                    Sentence.SentenceType.SIMPLE_QUESTION -> "sentences/sentences_simple_question.json"
                    Sentence.SentenceType.EMOTIONAL_TRANSLATION -> "sentences/sentences_emotional_translator.json"
                    Sentence.SentenceType.DEVILS_ADVOCATE -> "sentences/sentences_devils_advocate.json"
                    Sentence.SentenceType.DIALOGUE_WITH_SELF -> "sentences/sentences_dialogue_with_self.json"
                    Sentence.SentenceType.IMAGINARY_SITUATION -> "sentences/sentences_imaginary_situation.json"
                    Sentence.SentenceType.EMOTION_TO_FACT -> "sentences/sentences_emotions_to_facts.json"
                    Sentence.SentenceType.WHO_AM_I -> "sentences/sentences_who_am_i.json"
                    Sentence.SentenceType.I_AM_EXPERT -> "sentences/sentences_i_am_expert.json"
                    Sentence.SentenceType.FORBIDDEN_WORDS -> "sentences/sentences_forbiden_words_.json"
                    Sentence.SentenceType.BODY_LANGUAGE -> "sentences/sentences_body_language.json"
                    Sentence.SentenceType.PERSUASIVE_SHOUT -> "sentences/sentences_persuative_shout.json"
                    Sentence.SentenceType.SUBTLE_MANIPULATION -> "sentences/sentences_subtle_manipulation.json"
                    Sentence.SentenceType.ONE_SYNONYM_PLEASE -> "sentences/sentences_one_sinonim_please.json"
                    Sentence.SentenceType.INTONATION_MASTER -> "sentences/sentences_intonation_master.json"
                    Sentence.SentenceType.FUNNIEST_ANSWER -> "sentences/sentences_funniest_answer.json"
                    Sentence.SentenceType.SELL_THE_MADNESS -> "sentences/sentences_sell_the_madness.json"
                    Sentence.SentenceType.FUNNY_EXCUSE -> "sentences/sentences_funny_excuse.json"
                    Sentence.SentenceType.PROBLEMS -> "sentences/sentences_problems.json"
                }

                val sentences: List<Sentence> = loadJsonFromAssets(context, fileName)?.let { json ->
                    Gson().fromJson(json, object : TypeToken<List<Sentence>>() {}.type)
                } ?: emptyList()

                cashedSentences[sentenceType] = sentences
            }
        }

        return cashedSentences[sentenceType]!!
    }
}