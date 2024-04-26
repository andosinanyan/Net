package com.example.net.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.example.net.R
import com.example.net.RIGHT_VARIANT_PLACEHOLDER
import com.example.net.databinding.QuestionVariantBinding

@SuppressLint("ViewConstructor")
class QuestionVariantView(context: Context, doubleMargin: Boolean = false) : LinearLayout(context) {

    private var viewBinding: QuestionVariantBinding? = null
    private val scale = context.resources.displayMetrics.density
    private val paddingInPx = (16 * scale + 0.5f).toInt()


    init {
        QuestionVariantBinding.inflate(LayoutInflater.from(context), this, true)
            .apply { viewBinding = this }
        if (doubleMargin) setDoubleMargin()
    }

    private fun setDoubleMargin() {
        val newLayoutParams = viewBinding?.root?.layoutParams as LayoutParams
        newLayoutParams.topMargin = paddingInPx
        viewBinding?.root?.layoutParams = newLayoutParams
    }

    fun setData(variantText: String) {
        viewBinding?.run {
            questionTextView.text = variantText.replace(RIGHT_VARIANT_PLACEHOLDER, "")
            questionTextView.setTextColor(Color.BLACK)
        }
    }

    fun setRightView() {
        viewBinding?.run {
            questionTextView.setTextColor(Color.WHITE)
            root.setBackgroundResource(R.drawable.right_variant_view_background)
        }
    }

    fun setWrongView() {
        viewBinding?.run {
            questionTextView.setTextColor(Color.WHITE)
            root.setBackgroundResource(R.drawable.wrong_variant_view_background)
        }
    }
}