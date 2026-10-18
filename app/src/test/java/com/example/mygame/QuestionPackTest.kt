package com.example.mygame

import com.example.mygame.core.QuestionValidator
import com.example.mygame.core.QuizSession
import org.junit.Assert.*
import org.junit.Test

class QuestionPackTest {
    @Test fun bundledQuestionsHaveUniqueIdsAndValidAnswers() {
        val questions = Constants.getQuestions().map(Question::toDomain)
        assertEquals(7, questions.size)
        assertTrue(QuestionValidator.errors(questions).isEmpty())
        val session = QuizSession(questions)
        questions.forEach { question ->
            session.select(question.correctAnswer)
            session.submit()
            session.advance()
        }
        assertTrue(session.isComplete)
        assertEquals(7, session.correctAnswers)
    }
}
