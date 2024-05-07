package com.example.net.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.toColorInt
import androidx.recyclerview.widget.RecyclerView
import com.example.net.R
import com.example.net.databinding.TestItemLayoutBinding
import com.example.net.entity.AllTestData
import com.example.net.extentions.backgroundModifier


class AllTestsRecyclerViewAdapter(
    val context: Context,
    private var itemClickListener: (Int) -> Unit
) :
    RecyclerView.Adapter<AllTestsRecyclerViewAdapter.ViewHolder>() {

    private var testData: AllTestData? = null
    private var testTitle = "Թեստ "

    inner class ViewHolder(val view: TestItemLayoutBinding) : RecyclerView.ViewHolder(view.root) {
        fun onBind(itemData: String, key: String) {
            view.run {
                modifyBackground(itemData.isNotEmpty())
                testIconImageView.setImageResource(R.drawable.test_icon)
                titleTextView.text = "Թեստ $key"
                countOfTestsTextView.text =  context.getString(R.string.test_total_count, testData?.count)
                resultTextView.text = if(itemData == "0") "-" else itemData
                checkAndSetResultTextColor(itemData)
                root.setOnClickListener {
                    itemClickListener(key.toInt())
                }
            }
        }

        private fun modifyBackground(isActive: Boolean) {
            view.run {
                root.backgroundModifier(
                    cornerRadius = 8f,
                    backgroundColor = "#F6F6F6",
                    backgroundColorAlpha = if (!isActive) 30 else null
                )
            }
        }

        private fun checkAndSetResultTextColor(itemData: String) {
            view.run {
                if (itemData == DEFAULT_NO_RESULT_ICON || itemData == "0") {
                    resultTextView.setTextColor(
                        testData?.defaultTextColor?.toColorInt()
                            ?: DEFAULT_COLOR_OF_TEST_COUNT_TEXT.toColorInt()
                    )
                } else {
                    if (itemData.isNotEmpty() && itemData.toInt() < (testData?.minimumCorrectsCount ?: 0)) {
                        resultTextView.setTextColor(
                            testData?.testFailTextColor?.toColorInt()
                                ?: FAIL_COLOR_OF_TEST_COUNT_TEXT.toColorInt()
                        )
                    } else {
                        resultTextView.setTextColor(
                            testData?.testSuccessTextColor?.toColorInt()
                                ?: SUCCESS_COLOR_OF_TEST_COUNT_TEXT.toColorInt()
                        )
                    }
                }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = TestItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val keyOfTest = position.plus(1).toString()
        testData?.testItemsList?.find { it.themeId == keyOfTest }?.let {
            holder.onBind(it.rightCount, keyOfTest)
        }
    }

    override fun getItemCount(): Int = testData?.testItemsList?.size ?: 0

    fun setTestData(data: AllTestData, testTitle: String) {
        this.testData = data
        this.testTitle = testTitle
        notifyDataSetChanged()
    }

    companion object {
        const val DEFAULT_NO_RESULT_ICON = "-"
        const val DEFAULT_COLOR_OF_TEST_COUNT_TEXT = "#BABABA"
        const val FAIL_COLOR_OF_TEST_COUNT_TEXT = "#FF3B30"
        const val SUCCESS_COLOR_OF_TEST_COUNT_TEXT = "#34C759"
    }
}