package com.example.mygame

import com.example.mygame.core.QuizQuestion

/** Existing UI model keeps drawable references out of the domain module. */
data class Question(
    val id: Int,
    val question: String,
    val image: Int,
    val optionOne: String,
    val optionTwo: String,
    val optionThree: String,
    val optionFour: String,
    val correctAnswer: Int
) {
    fun toDomain(): QuizQuestion = QuizQuestion(
        id,
        question,
        listOf(optionOne, optionTwo, optionThree, optionFour),
        correctAnswer
    )
}
