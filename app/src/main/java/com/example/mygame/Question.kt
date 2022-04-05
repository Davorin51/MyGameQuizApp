package com.example.mygame

import android.view.View

data class Question (
    
    val id: Int,
    val question : String,
    val image: Int,
    val optionOne : String,
    val optionTwo : String,
    val optionThree : String,
    val optionFour : String,
    val correctAnswer : Int
)