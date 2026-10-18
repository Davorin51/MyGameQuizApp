package com.example.mygame

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.example.mygame.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        WindowCompat.getInsetsController(window, window.decorView)
            .hide(WindowInsetsCompat.Type.statusBars())
        binding.buttonStart.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            if (name.isBlank()) {
                binding.etName.error = getString(R.string.enter_name)
            } else {
                startActivity(
                    Intent(this, QuizQuestionsActivity::class.java).apply {
                        putExtra(Constants.USER_NAME, name)
                    }
                )
                finish()
            }
        }
        binding.etName.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.buttonStart.performClick()
                true
            } else {
                false
            }
        }
    }
}
