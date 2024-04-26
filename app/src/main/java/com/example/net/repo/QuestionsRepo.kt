package com.example.net.repo

import com.example.net.entity.QuestionsList

interface QuestionsRepo {

    suspend fun getExamQuestions() : QuestionsList

    suspend fun getThemeQuestionsById(id: String) : QuestionsList

}