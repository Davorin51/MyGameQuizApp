package com.example.mygame

object Constants {

    const val USER_NAME: String = "user name"
    const val TOTAL_QUESTIONS:String = "total_questions"
    const val CORRECT_ANSWERS: String = "correct_answers"
    fun getQuestions(): ArrayList<Question>{
        val questionList = ArrayList<Question>()

        val que1 = Question(1, "Whose country this flag belongs?"
        , R.drawable.brazil1, optionOne = "Argentina", optionTwo = "Austria", optionThree = "Brazil", optionFour = "Germany",
            3
        )
questionList.add(que1)

        val que2 = Question(2, "Whose country this flag belongs?"
            , R.drawable.brazil1, optionOne = "Argentina", optionTwo = "Austria", optionThree = "Brazil", optionFour = "Germany",
            3
        )
        questionList.add(que2)

        val que3 = Question(3, "Whose country this flag belongs?"
            , R.drawable.brazil1, optionOne = "Argentina", optionTwo = "Austria", optionThree = "Brazil", optionFour = "Germany",
            3
        )
        questionList.add(que3)

        val que4 = Question(1, "Whose country this flag belongs?"
            , R.drawable.brazil1, optionOne = "Argentina", optionTwo = "Austria", optionThree = "Brazil", optionFour = "Germany",
            3
        )
        questionList.add(que4)

        val que5 = Question(2, "Whose country this flag belongs?"
            , R.drawable.brazil1, optionOne = "Argentina", optionTwo = "Austria", optionThree = "Brazil", optionFour = "Germany",
            3
        )
        questionList.add(que5)

        val que6 = Question(4, "Whose country this flag belongs?"
            , R.drawable.brazil1, optionOne = "Argentina", optionTwo = "Austria", optionThree = "Brazil", optionFour = "Germany",
            3
        )
        questionList.add(que6)

        val que7= Question(1, "Whose country this flag belongs?"
            , R.drawable.brazil1, optionOne = "Argentina", optionTwo = "Austria", optionThree = "Brazil", optionFour = "Germany",
            3
        )
        questionList.add(que7)
        return questionList
    }
}