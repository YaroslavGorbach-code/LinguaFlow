package com.example.yaroslavhorach.home.model


sealed class SpeakingLevel(
    val level: Int,
    val experienceRequired: IntRange,
    val title: String,
    val description: String,
    val emoji: String
) {

    companion object {
        fun nextLevel(currentLevel: Int): SpeakingLevel? = when (currentLevel) {
            1 -> Conversationalist()
            2 -> Communicator()
            3 -> Storyteller()
            4 -> ConfidentSpeaker()
            5 ->  Master()
            else -> null
        }

        fun fromExperience(exp: Int): SpeakingLevel =
            allLevels.find { exp in it.experienceRequired } ?: Listener()

        private val allLevels: List<SpeakingLevel> = listOf(
            Listener(), Conversationalist(), Communicator(),
            Storyteller(), ConfidentSpeaker(), Master()
        )
    }

    class Listener : SpeakingLevel(
        level = 1,
        experienceRequired = 0..200,
        title = "Слухач",
        emoji = "🧱",
        description = "Ти починаєш чути мову по-новому. Спостерігаєш, запам’ятовуєш, пробуєш перші фрази. Це фундамент, на якому збудується твоя впевненість. Вітаємо на старті!"
    )

    class Conversationalist : SpeakingLevel(
        level = 2,
        experienceRequired = 200..400,
        title = "Співрозмовник",
        emoji = "💬",
        description = "Ти вже не просто слухаєш — ти вступаєш у діалог. Вмієш підтримати розмову, поставити запитання, сказати “своє слово”. Комунікація стає природнішою."
    )

    class Communicator : SpeakingLevel(
        level = 3,
        experienceRequired = 400..600,
        title = "Комунікатор",
        emoji = "🧭",
        description = "Твої думки стають чіткими, ти легко обираєш слова і будуєш логіку. Ти не губишся у мовленні — ти керуєш ним. Спілкування — вже твоя зона комфорту."
    )

    class Storyteller : SpeakingLevel(
        level = 4,
        experienceRequired = 600..800,
        title = "Оповідач",
        emoji = "🎙️",
        description = "Ти вмієш розповісти історію так, щоб тебе слухали. Працюєш з інтонацією, структурою, емоціями. Слова більше не просто інформація — вони несуть смисл."
    )

    class ConfidentSpeaker : SpeakingLevel(
        level = 5,
        experienceRequired = 800..1200,
        title = "Впевнений оратор",
        emoji = "🔥",
        description = "Ти звучиш переконливо, спокійно й яскраво. Можеш імпровізувати, переконувати, бути лідером розмови. Твоє мовлення — інструмент, яким ти володієш."
    )

    class Master : SpeakingLevel(
        level = 6,
        experienceRequired = 1200..2000,
        title = "Майстер мовлення",
        emoji = "🦾",
        description = "Твоя мова — це мистецтво. Ти використовуєш її свідомо, точно, на рівні майстерності. Відкривається після значного прогресу."
    )

}