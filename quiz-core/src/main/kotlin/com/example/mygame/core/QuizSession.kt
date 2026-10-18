package com.example.mygame.core

enum class SubmitResult { NO_SELECTION, CORRECT, INCORRECT, ALREADY_ANSWERED, COMPLETED }

/** One point per correct answer. A question can be scored only once. */
class QuizSession(questions: List<QuizQuestion>) {
    // Copy nested lists so callers cannot mutate a running quiz through their input pack.
    val questions: List<QuizQuestion> = questions.map { it.copy(options = it.options.toList()) }
    private val answers = mutableListOf<Int>()
    var selectedAnswer: Int? = null
        private set
    var isComplete: Boolean = false
        private set

    init {
        val errors = QuestionValidator.errors(this.questions)
        require(errors.isEmpty()) { errors.joinToString("; ") }
    }

    var currentIndex: Int = 0
        private set
    val currentQuestion: QuizQuestion get() = questions[currentIndex]
    val isAnswered: Boolean get() = !isComplete && answers.size > currentIndex
    val total: Int get() = questions.size
    val correctAnswers: Int get() = answers.indices.count {
        answers[it] ==
            questions[it].correctAnswer
    }
    val answeredCount: Int get() = answers.size
    val correctPercentage: Double get() = 100.0 * correctAnswers / total

    fun select(answer: Int): Boolean {
        if (isComplete || isAnswered || answer !in 1..currentQuestion.options.size) return false
        selectedAnswer = answer
        return true
    }

    fun submit(): SubmitResult {
        if (isComplete) return SubmitResult.COMPLETED
        if (isAnswered) return SubmitResult.ALREADY_ANSWERED
        val answer = selectedAnswer ?: return SubmitResult.NO_SELECTION
        answers.add(answer)
        return if (answer ==
            currentQuestion.correctAnswer
        ) {
            SubmitResult.CORRECT
        } else {
            SubmitResult.INCORRECT
        }
    }

    fun advance(): Boolean {
        if (isComplete || !isAnswered) return false
        if (answers.size == total) isComplete = true else currentIndex++
        selectedAnswer = null
        return true
    }

    fun snapshot(): SessionSnapshot =
        SessionSnapshot(answers.toList(), selectedAnswer, isComplete, currentIndex)

    companion object {
        /** Invalid or incompatible saved state is rejected instead of trusting a saved score. */
        fun restore(questions: List<QuizQuestion>, snapshot: SessionSnapshot): QuizSession {
            val session = QuizSession(questions)
            require(snapshot.answers.size <= questions.size) { "Too many saved answers" }
            require(!snapshot.complete || snapshot.answers.size == questions.size) {
                "Incomplete saved quiz"
            }
            snapshot.answers.forEachIndexed { index, answer ->
                require(answer in 1..session.questions[index].options.size) {
                    "Invalid saved answer"
                }
            }
            require(snapshot.index in questions.indices) { "Invalid saved position" }
            require(snapshot.answers.size in snapshot.index..snapshot.index + 1) {
                "Inconsistent saved position"
            }
            require(!snapshot.complete || snapshot.index == questions.lastIndex) {
                "Invalid completed position"
            }
            session.currentIndex = snapshot.index
            session.answers.addAll(snapshot.answers)
            session.isComplete = snapshot.complete
            session.selectedAnswer = snapshot.selection
            require(
                snapshot.selection == null ||
                    (
                        !snapshot.complete &&
                            snapshot.selection in 1..session.currentQuestion.options.size
                        )
            ) {
                "Invalid saved selection"
            }
            require(!session.isAnswered || snapshot.selection == session.answers.last()) {
                "Inconsistent answered selection"
            }
            return session
        }
    }
}

data class SessionSnapshot(
    val answers: List<Int>,
    val selection: Int?,
    val complete: Boolean,
    val index: Int
)
