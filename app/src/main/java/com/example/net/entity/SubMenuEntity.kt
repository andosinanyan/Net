package com.example.net.entity

import com.google.gson.annotations.SerializedName

data class SubMenuEntity(
    @SerializedName("tag") val tag: String?,
    @SerializedName("text") var text: String?,
    @SerializedName("icon") var icon: String?,
    @SerializedName("ifempty") var ifEmpty: String?,
    @SerializedName("active") var active: String?
)

enum class ThemesSubMenuTypes(val value: String) {
    TESTS("tests"),
    EXAM("exam")
}