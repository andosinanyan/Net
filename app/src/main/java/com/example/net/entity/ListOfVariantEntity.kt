package com.example.net.entity

import com.google.gson.annotations.SerializedName

data class ListOfVariantEntity(
    @SerializedName("id")
    val themeId: String,
    @SerializedName("right")
    val rightCount: String,
    @SerializedName("wrong")
    val wrongCount: String
)
