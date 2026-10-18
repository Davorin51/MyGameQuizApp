package com.example.mygame

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.mygame.core.QuizSession
import com.example.mygame.core.SessionSnapshot
import com.example.mygame.core.SubmitResult
import com.example.mygame.databinding.ActivityQuizQuestionsBinding

/** XML screens remain intact; scoring and transitions belong to the JVM domain module. */
class QuizQuestionsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityQuizQuestionsBinding
    private lateinit var session: QuizSession
    private val questions = Constants.getQuestions()
    private val options: List<TextView> get() = with(binding) {
        listOf(tvOptionOne, tvOptionTwo, tvOptionThree, tvOptionFour)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizQuestionsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val domainQuestions = questions.map(Question::toDomain)
        session = if (savedInstanceState?.containsKey(SAVED_INDEX) == true) {
            val selection = savedInstanceState.getInt(SAVED_SELECTION, 0).takeIf { it != 0 }
            val snapshot = SessionSnapshot(
                savedInstanceState.getIntArray(SAVED_ANSWERS)?.toList().orEmpty(),
                selection,
                savedInstanceState.getBoolean(SAVED_COMPLETE),
                savedInstanceState.getInt(SAVED_INDEX)
            )
            try {
                QuizSession.restore(domainQuestions, snapshot)
            } catch (_: IllegalArgumentException) {
                // Stale state must never produce an invalid score or crash the screen.
                QuizSession(domainQuestions)
            }
        } else {
            QuizSession(domainQuestions)
        }
        options.forEachIndexed { index, option ->
            option.isFocusable = true
            option.setOnClickListener {
                if (session.select(index + 1)) render()
            }
        }
        binding.btnSubmit.setOnClickListener {
            if (session.isAnswered) {
                session.advance()
                if (session.isComplete) showResult() else render()
            } else {
                when (session.submit()) {
                    SubmitResult.NO_SELECTION -> {
                        binding.tvFeedback.setText(R.string.choose_answer)
                        binding.tvFeedback.announceForAccessibility(
                            getString(R.string.choose_answer)
                        )
                    }
                    SubmitResult.CORRECT, SubmitResult.INCORRECT -> render()
                    else -> Unit
                }
            }
        }
        if (session.isComplete) showResult() else render()
    }

    private fun render() = with(binding) {
        val question = questions[session.currentIndex]
        tvQuestion.text = question.question
        tvImage.visibility = if (question.image == 0) View.GONE else View.VISIBLE
        if (question.image != 0) tvImage.setImageResource(question.image)
        progressBar.max = session.total
        progressBar.progress = session.currentIndex + 1
        tvProgress.text =
            getString(R.string.question_progress, session.currentIndex + 1, session.total)
        options.forEachIndexed { index, option ->
            val position = index + 1
            option.text = session.currentQuestion.options[index]
            option.isEnabled = !session.isAnswered
            option.isSelected = position == session.selectedAnswer
            option.setTextColor(
                ContextCompat.getColor(this@QuizQuestionsActivity, R.color.option_text)
            )
            option.setTypeface(null, if (option.isSelected) Typeface.BOLD else Typeface.NORMAL)
            val background = when {
                session.isAnswered && position == question.correctAnswer ->
                    R.drawable.corrected_option
                session.isAnswered && option.isSelected -> R.drawable.wrong_option
                option.isSelected -> R.drawable.selected_option
                else -> R.drawable.default_option
            }
            option.setBackgroundResource(background)
        }
        tvFeedback.text = when {
            !session.isAnswered -> ""
            session.selectedAnswer == question.correctAnswer -> getString(R.string.answer_correct)
            else -> getString(
                R.string.answer_incorrect,
                session.currentQuestion.options[
                    question.correctAnswer -
                        1
                ]
            )
        }
        btnSubmit.setText(
            when {
                !session.isAnswered -> R.string.submit_answer
                session.currentIndex == session.total - 1 -> R.string.finish_quiz
                else -> R.string.next_question
            }
        )
    }

    override fun onSaveInstanceState(outState: Bundle) {
        val snapshot = session.snapshot()
        outState.putInt(SAVED_INDEX, snapshot.index)
        outState.putIntArray(SAVED_ANSWERS, snapshot.answers.toIntArray())
        outState.putInt(SAVED_SELECTION, snapshot.selection ?: 0)
        outState.putBoolean(SAVED_COMPLETE, snapshot.complete)
        super.onSaveInstanceState(outState)
    }

    private fun showResult() {
        startActivity(
            Intent(this, ResultActivity::class.java).apply {
                putExtra(Constants.USER_NAME, intent.getStringExtra(Constants.USER_NAME))
                putExtra(Constants.CORRECT_ANSWERS, session.correctAnswers)
                putExtra(Constants.TOTAL_QUESTIONS, session.total)
            }
        )
        finish()
    }

    private companion object {
        const val SAVED_INDEX = "quiz.index"
        const val SAVED_ANSWERS = "quiz.answers"
        const val SAVED_SELECTION = "quiz.selection"
        const val SAVED_COMPLETE = "quiz.complete"
    }
}
