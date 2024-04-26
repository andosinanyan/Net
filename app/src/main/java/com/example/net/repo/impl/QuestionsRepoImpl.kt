package com.example.net.repo.impl

import android.content.Context
import com.example.net.RIGHT_VARIANT_PLACEHOLDER
import com.example.net.entity.QuestionsList
import com.example.net.entity.SingleQuestionItem
import com.example.net.entity.getData
import com.example.net.entity.listOfThemesIndex
import com.example.net.extentions.mockEncryptedData
import com.example.net.repo.QuestionsRepo

class QuestionsRepoImpl(val context: Context) : QuestionsRepo {
    override suspend fun getExamQuestions(): QuestionsList {
        val mockedData = mockEncryptedData<QuestionsList>(context.getData("backup.json"))
        val listOfNumbers = mutableSetOf<Int>()
        val singleQuestionItemsList = mutableListOf<SingleQuestionItem>()

        while (listOfNumbers.size < 10) {
            listOfNumbers.add((0..82).random())
        }

        listOfNumbers.forEach { serialNumber ->
            singleQuestionItemsList.add(element = mockedData.questionsList.find { it.id == serialNumber }
                ?: return@forEach)
        }

        val data = QuestionsList(singleQuestionItemsList)
        val newListOfQuestionData = mutableListOf<SingleQuestionItem>()

        data.shuffleVariantsOfQuestion().questionsList.forEach { questionItem ->
            questionItem.variantsList.forEachIndexed { index, variantOfQuestion ->
                if (variantOfQuestion.contains(RIGHT_VARIANT_PLACEHOLDER)) {
                    println(index)
                    newListOfQuestionData.add(
                        questionItem.copy(
                            rightVariantIndex = index,
                            chosenVariantIndex = null
                        )
                    )
                }
            }
        }
        return QuestionsList(questionsList = newListOfQuestionData)
    }

    override suspend fun getThemeQuestionsById(id: String): QuestionsList {
        val mockedData = mockEncryptedData<QuestionsList>(context.getData("backup.json"))
        val getIndexesOfThemes = listOfThemesIndex[id.toInt()]
        val data = QuestionsList(
            questionsList = mockedData.questionsList.subList(
                fromIndex = getIndexesOfThemes.first,
                toIndex = getIndexesOfThemes.second,
            )
        )
        val newListOfQuestionData = mutableListOf<SingleQuestionItem>()

        data.shuffleVariantsOfQuestion().questionsList.forEach { questionItem ->
            questionItem.variantsList.forEachIndexed { index, variantOfQuestion ->
                if (variantOfQuestion.contains(RIGHT_VARIANT_PLACEHOLDER)) {
                    println(index)
                    newListOfQuestionData.add(
                        questionItem.copy(
                            rightVariantIndex = index,
                            chosenVariantIndex = null
                        )
                    )
                }
            }
        }
        return QuestionsList(questionsList = newListOfQuestionData)
    }


    private fun QuestionsList.shuffleVariantsOfQuestion(): QuestionsList {
        val tmpList = mutableListOf<SingleQuestionItem>()
        questionsList.forEach {
            tmpList.add(it.copy(variantsList = it.variantsList.shuffled()))
        }
        return QuestionsList(questionsList = tmpList)
    }
}