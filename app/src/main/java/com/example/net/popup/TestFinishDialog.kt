package com.example.net.popup

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import androidx.core.graphics.toColorInt
import com.example.net.R
import com.example.net.databinding.TestFinishedPopupBinding
import com.example.net.entity.FinishingStates

class TestFinishDialog(
    private val context: Context,
    val screenType: FinishingStates,
    val triple: Triple<Int, Int, Int>,
    var finishScreen: () -> Unit
) : Dialog(context) {

    private var viewBinding: TestFinishedPopupBinding? = null
    private var isFirstTimeToClick = true

    @SuppressLint("UseGetLayoutInflater")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.decorView?.setBackgroundResource(android.R.color.transparent)
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        viewBinding = TestFinishedPopupBinding.inflate(inflater)
        viewBinding?.run {
            setContentView(root)
            dismissButton.setOnClickListener {
                dismiss()
            }
            if (screenType == FinishingStates.FINISHING_NO_COMPLETE) {
                finishTestButton.setOnClickListener {
                    if (isFirstTimeToClick) {
                        isFirstTimeToClick = false
                    } else {
                        finishScreen()
                    }
                    messageTextView.text = getResultTexts()
                    dismissButton.visibility = View.GONE
                    setCancelable(false)
                }
            } else if (screenType == FinishingStates.SHOW_RESULT_OF_TEST) {
                messageTextView.text = getResultTexts()
                dismissButton.visibility = View.GONE
                setCancelable(false)
                finishTestButton.setOnClickListener {
                    finishScreen.invoke()
                }
            }
        }
    }

    private fun getResultTexts(): SpannableStringBuilder {
        val correctText = SpannableStringBuilder("Ճիշտ պատասխանված հարցեր՝ ${triple.first} \n")
        val incorrectText = SpannableStringBuilder("Սխալ պատասխանած հարցեր՝ ${triple.second} ")
        val unansweredText =
            SpannableStringBuilder((if (triple.third != 0) "\nՉպատասխանած հարցեր՝ ${triple.third}" else ""))


        val correctColorSpan = ForegroundColorSpan("#9939C118".toColorInt())
        val incorrectColorSpan = ForegroundColorSpan("#99FF3B30".toColorInt())
        val unansweredColorSpan = ForegroundColorSpan(Color.GRAY)

        correctText.setSpan(
            correctColorSpan,
            0,
            correctText.lastIndex + 1,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        incorrectText.setSpan(
            incorrectColorSpan,
            0,
            incorrectText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        if (unansweredText.isNotEmpty()) {
            unansweredText.setSpan(
                unansweredColorSpan,
                unansweredText.lastIndex + 1,
                unansweredText.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        return SpannableStringBuilder(correctText).append(incorrectText).append(unansweredText)
    }
}