package com.example.net.helpercomponents

import android.content.Context
import com.example.net.GENERAL_JSON_DATA
import com.example.net.MAIN_SHARED_PREFERENCES
import com.example.net.entity.ListOfVariantEntity
import com.example.net.entity.getData
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken

class GeneralInfoHelper(val context: Context) {

    inline fun <reified T> getGeneralJson(): T {
        val sharedPreferences =
            context.getSharedPreferences(MAIN_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val myType = object : TypeToken<T>() {}.type
        val generalJsonFromSharedPreferences = sharedPreferences.getString(GENERAL_JSON_DATA, "")
        val jsonObject = if (generalJsonFromSharedPreferences.isNullOrEmpty()) {
            val jsonData = context.getData("generalJson.json")
            with(sharedPreferences.edit()) {
                putString(GENERAL_JSON_DATA, jsonData)
                apply()
            }
            jsonData
        } else {
            generalJsonFromSharedPreferences
        }

        return Gson().fromJson(jsonObject, myType)
    }

    fun saveData(rightAnswer: Int, wrongAnswer: Int, id: String) {
        val sharedPreferences =
            context.getSharedPreferences(MAIN_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val generalJsonFromSharedPreferences = sharedPreferences.getString(GENERAL_JSON_DATA, "")
        if (!generalJsonFromSharedPreferences.isNullOrEmpty()) {
            val generalJsonAsObject =
                Gson().fromJson(generalJsonFromSharedPreferences, JsonObject::class.java)
            val modifiedJson = addAllRightAndWrongAnswersCount(
                generalJsonAsObject = generalJsonAsObject,
                themeId = id,
                rightAnswer = rightAnswer,
                wrongAnswer = wrongAnswer
            )

            val countOfAnswers = getRightAndWrongAnswersCount(modifiedJson)

            modifiedJson.addProperty("Corrects", countOfAnswers.first)
            modifiedJson.addProperty("Incorrects", countOfAnswers.second)
            modifiedJson.addProperty(
                "Tried",
                countOfAnswers.third
            )
            with(sharedPreferences.edit()) {
                putString(GENERAL_JSON_DATA, Gson().toJson(modifiedJson))
                apply()
            }
        }
    }

    private fun addAllRightAndWrongAnswersCount(
        generalJsonAsObject: JsonObject,
        themeId: String,
        wrongAnswer: Int,
        rightAnswer: Int
    ): JsonObject {
        val json = Gson().toJson(generalJsonAsObject["list_of_answered_data"])
        val myType = object : TypeToken<List<ListOfVariantEntity>>() {}.type
        val objectOfListRightAndWrongAnswers =
            Gson().fromJson<List<ListOfVariantEntity>>(json, myType)
        val newModifiedRightAndWrongAnswersCount =
            objectOfListRightAndWrongAnswers.find { it.themeId == themeId }?.copy(
                themeId = themeId,
                wrongCount = wrongAnswer.toString(),
                rightCount = rightAnswer.toString()
            )
        val newFinalData =
            objectOfListRightAndWrongAnswers.filter { it.themeId != themeId }.toMutableList()
        newModifiedRightAndWrongAnswersCount?.let { newFinalData.add(it) }
        val newListOfJsonElement: JsonElement = Gson().toJsonTree(newFinalData.toList())
        generalJsonAsObject.add("list_of_answered_data", newListOfJsonElement)
        return generalJsonAsObject
    }

    private fun getRightAndWrongAnswersCount(generalJsonAsObject: JsonObject): Triple<String, String, String> {
        val json = Gson().toJson(generalJsonAsObject["list_of_answered_data"])
        val myType = object : TypeToken<List<ListOfVariantEntity>>() {}.type
        val objectOfListRightAndWrongAnswers =
            Gson().fromJson<List<ListOfVariantEntity>>(json, myType)
        var (rightAnswersCount, wrongAnswersCount, triedCount) = Triple(0, 0, 0)
        objectOfListRightAndWrongAnswers?.forEach {
            rightAnswersCount += it.rightCount.toInt()
            wrongAnswersCount += it.wrongCount.toInt()
        }
        triedCount = rightAnswersCount + wrongAnswersCount
        return Triple(
            rightAnswersCount.toString(),
            wrongAnswersCount.toString(),
            triedCount.toString()
        )
    }
}