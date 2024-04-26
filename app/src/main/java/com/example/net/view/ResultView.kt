package com.example.net.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.net.R
import com.example.net.databinding.ResultViewLayoutBinding
import com.example.net.extentions.backgroundModifier

class ResultView(context: Context, attributeSet: AttributeSet) :
    ConstraintLayout(context, attributeSet) {

    private var viewBinding: ResultViewLayoutBinding? = null

    init {
        ResultViewLayoutBinding.inflate(LayoutInflater.from(context), this, true)
            .apply { viewBinding = this }
    }

    fun initView(
        backgroundColor: String,
        descriptionText: String,
        doneNumberText: Int,
        totalCountText: Int
    ) {
        viewBinding?.run {
            root.backgroundModifier(
                cornerRadius = 8f,
                backgroundColor = backgroundColor
            )

            doneNumber.text = doneNumberText.toString()
            descriptionTextView.text = descriptionText
            totalCount.text = context.getString(R.string.total_text, totalCountText.toString())
        }
    }

}