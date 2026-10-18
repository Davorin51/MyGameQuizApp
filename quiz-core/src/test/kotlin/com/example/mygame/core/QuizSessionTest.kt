package com.example.mygame.core

import org.junit.Assert.*
import org.junit.Test

class QuizSessionTest {
    private fun questions() = listOf(
        QuizQuestion(1, "First?", listOf("A", "B"), 2),
        QuizQuestion(2, "Second?", listOf("C", "D"), 1)
    )

    @Test fun cannotSubmitOrAdvanceWithoutAnswer() {
        val session = QuizSession(questions())
        assertEquals(SubmitResult.NO_SELECTION, session.submit())
        assertFalse(session.advance())
        assertEquals(0, session.currentIndex)
        assertEquals(0, session.correctAnswers)
    }

    @Test fun answerCannotBeChangedOrScoredTwice() {
        val session = QuizSession(questions())
        assertTrue(session.select(2))
        assertEquals(SubmitResult.CORRECT, session.submit())
        assertTrue(session.isAnswered)
        assertEquals(0, session.currentIndex)
        assertFalse(session.select(1))
        assertEquals(SubmitResult.ALREADY_ANSWERED, session.submit())
        assertEquals(1, session.correctAnswers)
    }

    @Test fun wrongAnswerAndCompletionProduceBoundedScore() {
        val session = QuizSession(questions())
        session.select(1)
        assertEquals(SubmitResult.INCORRECT, session.submit())
        assertTrue(session.advance())
        assertNull(session.selectedAnswer)
        assertEquals(1, session.currentIndex)
        session.select(1)
        session.submit()
        assertTrue(session.advance())
        assertTrue(session.isComplete)
        assertEquals(2, session.answeredCount)
        assertEquals(1, session.correctAnswers)
        assertEquals(50.0, session.correctPercentage, 0.0)
        assertFalse(session.select(1))
        assertFalse(session.advance())
        assertEquals(SubmitResult.COMPLETED, session.submit())
    }

    @Test fun invalidAnswerDoesNotOverwriteValidSelection() {
        val session = QuizSession(questions())
        assertFalse(session.select(0))
        assertFalse(session.select(3))
        assertNull(session.selectedAnswer)
        session.select(2)
        assertFalse(session.select(Int.MAX_VALUE))
        assertEquals(2, session.selectedAnswer)
    }

    @Test fun restoresBeforeSubmissionAfterSubmissionAndBetweenQuestions() {
        var session = QuizSession(questions())
        session.select(2)
        session = QuizSession.restore(questions(), session.snapshot())
        assertEquals(2, session.selectedAnswer)
        assertFalse(session.isAnswered)
        session.submit()
        session = QuizSession.restore(questions(), session.snapshot())
        assertTrue(session.isAnswered)
        assertEquals(SubmitResult.ALREADY_ANSWERED, session.submit())
        assertEquals(1, session.correctAnswers)
        session.advance()
        session = QuizSession.restore(questions(), session.snapshot())
        assertEquals(1, session.currentIndex)
        assertNull(session.selectedAnswer)
        assertFalse(session.isAnswered)
    }

    @Test fun completedStateRestoresWithoutChangingScore() {
        val one = questions().take(1)
        val session = QuizSession(one)
        session.select(2)
        session.submit()
        session.advance()
        val restored = QuizSession.restore(one, session.snapshot())
        assertTrue(restored.isComplete)
        assertEquals(1, restored.correctAnswers)
        assertEquals(100.0, restored.correctPercentage, 0.0)
    }

    @Test fun invalidSnapshotsAreRejected() {
        val invalid = listOf(
            SessionSnapshot(listOf(2, 1, 1), null, false, 1),
            SessionSnapshot(emptyList(), null, true, 0),
            SessionSnapshot(listOf(0), 0, false, 0),
            SessionSnapshot(emptyList(), 3, false, 0),
            SessionSnapshot(listOf(2), 1, false, 0),
            SessionSnapshot(emptyList(), null, false, -1),
            SessionSnapshot(listOf(2, 1), null, true, 0),
            SessionSnapshot(emptyList(), null, false, 1)
        )
        invalid.forEach { snapshot ->
            assertThrows(IllegalArgumentException::class.java) {
                QuizSession.restore(questions(), snapshot)
            }
        }
    }

    @Test fun inputMutationDoesNotAlterSession() {
        val options = mutableListOf("A", "B")
        val pack = mutableListOf(QuizQuestion(1, "Original?", options, 1))
        val session = QuizSession(pack)
        options[0] = "Changed"
        pack.clear()
        assertEquals("A", session.currentQuestion.options[0])
        assertEquals(1, session.total)
    }

    @Test fun seededRandomSessionsMatchIndependentScoreOracle() {
        val random = java.util.Random(51)
        repeat(200) {
            val size = 1 + random.nextInt(25)
            val pack = (1..size).map { id ->
                QuizQuestion(id, "Question $id", listOf("A", "B", "C", "D"), 1 + random.nextInt(4))
            }
            var session = QuizSession(pack)
            var expected = 0
            pack.forEach { question ->
                val answer = 1 + random.nextInt(4)
                if (answer == question.correctAnswer) expected++
                assertTrue(session.select(answer))
                session.submit()
                session = QuizSession.restore(pack, session.snapshot())
                assertEquals(expected, session.correctAnswers)
                assertEquals(SubmitResult.ALREADY_ANSWERED, session.submit())
                assertTrue(session.advance())
            }
            assertTrue(session.isComplete)
            assertEquals(expected, session.correctAnswers)
        }
    }
}
