package com.example.mygame

object Constants {
    const val USER_NAME = "user name"
    const val TOTAL_QUESTIONS = "total_questions"
    const val CORRECT_ANSWERS = "correct_answers"

    /** Small original sample pack. Resource ID zero means a text-only question. */
    fun getQuestions(): ArrayList<Question> = arrayListOf(
        Question(
            1,
            "Which country does this flag represent?",
            R.drawable.brazil_flag,
            "Argentina",
            "Austria",
            "Brazil",
            "Germany",
            3
        ),
        Question(
            2,
            "Which country has Canberra as its capital?",
            0,
            "Canada",
            "Australia",
            "New Zealand",
            "Ireland",
            2
        ),
        Question(
            3,
            "On which continent is Kenya located?",
            0,
            "Asia",
            "Europe",
            "Africa",
            "South America",
            3
        ),
        Question(
            4,
            "Which ocean lies between Africa and Australia?",
            0,
            "Atlantic",
            "Arctic",
            "Pacific",
            "Indian",
            4
        ),
        Question(
            5,
            "Which of these countries is landlocked?",
            0,
            "Nepal",
            "Portugal",
            "Japan",
            "Chile",
            1
        ),
        Question(
            6,
            "Which country borders both Spain and the Atlantic Ocean?",
            0,
            "Italy",
            "Portugal",
            "Greece",
            "Austria",
            2
        ),
        Question(
            7,
            "Which continent contains the South Pole?",
            0,
            "Europe",
            "Africa",
            "Antarctica",
            "Australia",
            3
        )
    )
}
