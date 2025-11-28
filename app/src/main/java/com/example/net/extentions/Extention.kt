package com.example.net.extentions

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.TextView
import androidx.core.graphics.toColorInt
import com.example.net.Test2
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken


fun Int.toDp(scale: Float): Int {
    return (this * scale + 0.5).toInt()
}

fun View.backgroundModifier(
    cornerRadius: Float? = null,
    strokeWidth: Int? = null,
    strokeColor: String? = null,
    backgroundColor: String? = null,
    backgroundColorAlpha: Int? = null,
    cornerRadiusArray: MutableList<Float>? = null
) {
    if (background is Drawable && background is GradientDrawable) {
        val scale = resources.displayMetrics.density
        val gradientDrawable = GradientDrawable()
        if (cornerRadius != null) {
            gradientDrawable.cornerRadius = (cornerRadius * scale + 0.5f)
        }
        if (cornerRadiusArray != null) {
            gradientDrawable.cornerRadii = cornerRadiusArray.toFloatArray()
        }
        if (strokeWidth != null) gradientDrawable.setStroke(
            strokeWidth,
            strokeColor?.toColorInt() ?: Color.BLACK
        )
        if (backgroundColor != null) {
            if (backgroundColorAlpha == null) {
                gradientDrawable.setColor(Color.parseColor(backgroundColor))
            } else {
                gradientDrawable.setColor(
                    backgroundColor.toColorInt() getColorWithAlpha backgroundColorAlpha
                )
            }
        }
        this.background = gradientDrawable
    }
}

infix fun Int.getColorWithAlpha(alpha: Int): Int {
    val red = Color.red(this)
    val blue = Color.blue(this)
    val green = Color.green(this)
    return Color.argb(alpha, red, green, blue)
}

inline fun <reified T> mockData(json: String): T {
    val myType = object : TypeToken<T>() {}.type
    return Gson().fromJson(json, myType)
}

//inline fun <reified T> mockEncryptedData(json: String): T {
//    val myType = object : TypeToken<T>() {}.type
//    val finalJson = Gson().fromJson(json, JsonObject::class.java)?.run {
//        Test2.decryptAESFromBase64(
//            input = get("ciphertext").asString,
//            ivBase64 = get("iv").asString,
//            tagBase64 = get("tag").asString
//        )
//    }
//    return Gson().fromJson(finalJson, myType)
//}