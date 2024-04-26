package com.example.net.adapter

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager

class NoScrollLayoutManager : LinearLayoutManager {

    constructor(context: Context, orientation: Int, reverseLayout: Boolean) : super(
        context,
        orientation,
        reverseLayout
    )


    private var isScrollEnabled = false

    override fun canScrollVertically(): Boolean {
        return isScrollEnabled
    }

    override fun canScrollHorizontally(): Boolean {
        return isScrollEnabled
    }

    fun setScrollEnabled(enabled: Boolean) {
        isScrollEnabled = enabled
    }
}