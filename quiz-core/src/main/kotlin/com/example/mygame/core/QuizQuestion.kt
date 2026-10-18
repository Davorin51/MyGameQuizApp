package com.example.mygame.core

/** Android-independent, single-answer question. Answer positions are one-based. */
data class QuizQuestion(
    val id: Int,
    val prompt: String,
    val options: List<String>,
    val correctAnswer: Int
)

object QuestionValidator {
    fun errors(questions: List<QuizQuestion>): List<String> = buildList {
        if (questions.isEmpty()) add("A question pack must not be empty")
        val ids = mutableSetOf<Int>()
        val content = mutableSetOf<Pair<String, List<String>>>()
        questions.forEach { question ->
            val prefix = "Question ${question.id}"
            if (question.id <= 0) add("$prefix: ID must be positive")
            if (!ids.add(question.id)) add("$prefix: duplicate ID")
            if (question.prompt.isBlank()) add("$prefix: prompt must not be blank")
            if (question.options.size !in 2..8) add("$prefix: provide 2 to 8 options")
            if (question.options.any(String::isBlank)) add("$prefix: options must not be blank")
            val normalized = question.options.map { it.trim().lowercase(java.util.Locale.ROOT) }
            if (normalized.toSet().size != normalized.size) add("$prefix: duplicate options")
            if (question.correctAnswer !in question.options.indices.map { it + 1 }) {
                add("$prefix: correct answer is outside the option range")
            }
            val signature = question.prompt.trim().lowercase(java.util.Locale.ROOT) to normalized
            if (!content.add(signature)) add("$prefix: duplicate question content")
        }
    }
}
