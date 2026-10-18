package com.example.mygame

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mygame.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.tvName.text = intent.getStringExtra(Constants.USER_NAME).orEmpty()
        val total = intent.getIntExtra(Constants.TOTAL_QUESTIONS, 0).coerceAtLeast(0)
        val correct = intent.getIntExtra(Constants.CORRECT_ANSWERS, 0).coerceIn(0, total)
        binding.tvScore.text = getString(R.string.result_score, correct, total)
        binding.btnFinish.setOnClickListener {
            startActivity(
                Intent(this, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                }
            )
            finish()
        }
    }
}
