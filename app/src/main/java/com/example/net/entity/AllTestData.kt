package com.example.net.entity

import com.google.gson.annotations.SerializedName

data class AllTestData(
    @SerializedName("count") val count: Int,
    @SerializedName("correctsmin") val minimumCorrectsCount: Int,
    @SerializedName("failcolor") val testFailTextColor: String,
    @SerializedName("succescolor") val testSuccessTextColor: String,
    @SerializedName("defaultcolor") val defaultTextColor: String,
    @SerializedName("list_of_answered_data") val testItemsList: List<ListOfVariantEntity>,
    @SerializedName("rightAnswersCount") val rightAnswersCount: Int?,
    @SerializedName("wrongAnswersCount") val wrongAnswersCount: Int?
)