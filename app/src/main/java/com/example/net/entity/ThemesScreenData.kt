package com.example.net.entity


import com.example.net.GenerateJsonSchema
import com.google.gson.annotations.SerializedName

@GenerateJsonSchema
data class ThemesScreenData(
    @SerializedName("submenu")
    val subMenuItemEntity: List<SubMenuEntity>,
    @SerializedName("subscribed")
    val isUserSubscribed: String,
    @SerializedName("Allquestion") val allQuestionCount : Int,
    @SerializedName("Tried") val triedCount : Int,
    @SerializedName("Corrects") val correctsCount : Int,
    @SerializedName("Incorrects") val incorrectCount : Int,
    @SerializedName("resultsTried") val resultsTriedText : String,
    @SerializedName("resultsCorrects") val resultsCorrectsText : String,
    @SerializedName("resultsIncorrects") val resultsIncorrectText : String,
    @SerializedName("testn") val testNumberText : String,
    @SerializedName("popup_locked") //todo change back
    val lockedResultsText : List<String> = listOf(),
    @SerializedName("fail") val failText : String,
    @SerializedName("success") val successText : String,
    @SerializedName("failDalt") val failDaltText : String,
    @SerializedName("ExamResPart1") val examResPart1 : String,
    @SerializedName("ExamResPart2") val examResPart2 : String,
    @SerializedName("ExamPassMin") val examPassMin : String,
    @SerializedName("to_incorrects") val toIncorrectText : String,
    @SerializedName("tryAgain") val tryAgainText : String,
    @SerializedName("theme") val theme : String,
    @SerializedName("videolesson") val videoLessonText : String,
    @SerializedName("questions") val questionsText : String,
    @SerializedName("device_changed")
    val deviceChanged: Int?

)

data class ThemesScreenGeneralSettingsData(
    @SerializedName("Allquestion") val allQuestionCount : Int,
    @SerializedName("Tried") val triedCount : Int,
    @SerializedName("Corrects") val correctsCount : Int,
    @SerializedName("Incorrects") val incorrectCount : Int,
    @SerializedName("resultsTried") val resultsTriedText : String,
    @SerializedName("resultsCorrects") val resultsCorrectsText : String,
    @SerializedName("resultsIncorrects") val resultsIncorrectText : String,
    @SerializedName("testn") val testNumberText : String,
    @SerializedName("popup_locked") //todo change back
    val lockedResultsText : List<String> = listOf(),
    @SerializedName("fail") val failText : String,
    @SerializedName("success") val successText : String,
    @SerializedName("failDalt") val failDaltText : String,
    @SerializedName("ExamResPart1") val examResPart1 : String,
    @SerializedName("ExamResPart2") val examResPart2 : String,
    @SerializedName("ExamPassMin") val examPassMin : String,
    @SerializedName("to_incorrects") val toIncorrectText : String,
    @SerializedName("tryAgain") val tryAgainText : String,
    @SerializedName("theme") val theme : String,
    @SerializedName("videolesson") val videoLessonText : String,
    @SerializedName("questions") val questionsText : String
): java.io.Serializable