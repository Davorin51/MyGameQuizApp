package com.example.mygame.core

import org.junit.Assert.*
import org.junit.Test

class QuestionValidatorTest {
    private val valid = QuizQuestion(1, "Choose one", listOf("Yes", "No"), 1)

    @Test fun validPackHasNoErrors() {
        assertTrue(QuestionValidator.errors(listOf(valid)).isEmpty())
    }

    @Test fun malformedQuestionsAreRejected() {
        val invalid = listOf(
            valid.copy(id = 0), valid.copy(id = -1), valid.copy(prompt = " "),
            valid.copy(options = emptyList()), valid.copy(options = listOf("Yes")),
            valid.copy(options = (1..9).map(Int::toString)),
            valid.copy(options = listOf("Yes", " ")),
            valid.copy(options = listOf("Yes", " yes ")),
            valid.copy(correctAnswer = 0), valid.copy(correctAnswer = 3)
        )
        invalid.forEach { question ->
            assertFalse(QuestionValidator.errors(listOf(question)).isEmpty())
            assertThrows(IllegalArgumentException::class.java) { QuizSession(listOf(question)) }
        }
    }

    @Test fun emptyPackIsRejected() {
        assertFalse(QuestionValidator.errors(emptyList()).isEmpty())
        assertThrows(IllegalArgumentException::class.java) { QuizSession(emptyList()) }
    }

    @Test fun duplicateIdsAreRejected() {
        assertTrue(
            QuestionValidator.errors(listOf(valid, valid.copy(prompt = "Another?")))
                .any { it.contains("duplicate ID") }
        )
    }

    @Test fun normalizedDuplicateContentIsRejected() {
        val duplicate = valid.copy(id = 2, prompt = " CHOOSE ONE ", options = listOf(" YES", "no "))
        assertTrue(
            QuestionValidator.errors(listOf(valid, duplicate)).any {
                it.contains("duplicate question content")
            }
        )
    }

    @Test fun normalizationIsIndependentOfDeviceLocale() {
        val previous = java.util.Locale.getDefault()
        try {
            java.util.Locale.setDefault(java.util.Locale.forLanguageTag("tr-TR"))
            val pack = listOf(valid.copy(options = listOf("I", "i")))
            assertTrue(QuestionValidator.errors(pack).any { it.contains("duplicate options") })
        } finally {
            java.util.Locale.setDefault(previous)
        }
    }
}
