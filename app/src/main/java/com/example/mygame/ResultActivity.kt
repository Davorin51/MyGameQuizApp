package com.example.mygame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import org.w3c.dom.Text

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val username = intent.getStringExtra(Constants.USER_NAME)
        val tvName = findViewById<TextView>(R.id.tv_name)
        tvName.text = username

        val totalQuestion = intent.getIntExtra(Constants.TOTAL_QUESTIONS,0)
        val correctAnswers = intent.getIntExtra(Constants.CORRECT_ANSWERS, 0)

        val tvScore = findViewById<TextView>(R.id.tv_score)
        tvScore.text = "Your score is $correctAnswers out of $totalQuestion"

        val buttonFInish = findViewById<Button>(R.id.btn_finish)
        buttonFInish.setOnClickListener{
            startActivity(Intent(this, MainActivity::class.java))
        }

    }
}