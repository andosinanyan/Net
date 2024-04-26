package com.example.net.popup

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import com.example.net.databinding.NewsPopupLayoutBinding
import com.example.net.databinding.TestFinishedPopupBinding
import com.example.net.entity.FinishingStates

class NewsPopUpData(
    private val context: Context,
) : Dialog(context) {

    private var viewBinding: NewsPopupLayoutBinding? = null

    @SuppressLint("UseGetLayoutInflater")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.decorView?.setBackgroundResource(android.R.color.transparent)
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        viewBinding = NewsPopupLayoutBinding.inflate(inflater)
        viewBinding?.run {
            setContentView(root)
            dismissButton.setOnClickListener {
                dismiss()
            }
        }
    }
}