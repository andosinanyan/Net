package com.example.net.entity

import android.content.Context
import com.google.gson.annotations.SerializedName


data class QuestionsList(
    @SerializedName("questions_list")
    val questionsList: List<SingleQuestionItem>
)

data class SingleQuestionItem(
    @SerializedName("id")
    val id: Int,
    @SerializedName("question")
    val question: String,
    @SerializedName("variant")
    val variantsList: List<String>,
    @SerializedName("seconds")
    var hintSeconds: List<Int>? = null,
    @SerializedName("explanation")
    var explanation: String? = null,
    var chosenVariantIndex: Int? = null,
    val rightVariantIndex: Int,
    var isAnsweredRight: Boolean
)


val listOfThemesIndex = mutableListOf(
    0 to 10,
    10 to 20,
    20 to 30,
    30 to 40,
    40 to 50,
    50 to 60,
    60 to 70,
    70 to 82,
    82 to 92
)

fun Context.getData(fileName: String): String {
    return assets.open(fileName).bufferedReader().use {
        it.readText()
    }
}

