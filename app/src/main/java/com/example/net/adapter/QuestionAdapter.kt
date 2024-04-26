package com.example.net.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.allViews
import androidx.recyclerview.widget.RecyclerView
import com.example.net.databinding.SingleQuestionViewBinding
import com.example.net.entity.QuestionsList
import com.example.net.entity.SingleQuestionItem
import com.example.net.view.QuestionVariantView

class QuestionAdapter(
    val context: Context,
    val clickedOnForwardOrBack: (Int) -> Unit
) :
    RecyclerView.Adapter<QuestionAdapter.ViewHolder>() {

    private var questionsList = mutableListOf<SingleQuestionItem>()
    var alreadyAnsweredQuestionsList = mutableMapOf<Int, SingleQuestionItem>()
    var currentPosition = 0
        set(value) {
            clickedOnForwardOrBack(value)
            field = value
        }

    class ViewHolder(
        private val viewBinding: SingleQuestionViewBinding,
        private val context: Context,
        private val alreadyAnsweredQuestionsList: MutableMap<Int, SingleQuestionItem>
    ) :
        RecyclerView.ViewHolder(viewBinding.root) {

        init {
            setIsRecyclable(false)
        }

        fun onBind(singleQuestionItem: SingleQuestionItem) {
            viewBinding.run {
                questionTextView.text = singleQuestionItem.question

                val listOfVariantsView = mutableSetOf<QuestionVariantView>()
                singleQuestionItem.variantsList.forEach { variantOfQuestion ->
                    val questionVariantView = QuestionVariantView(context = context)
                    questionVariantView.id = View.generateViewId()
                    questionVariantView.setData(variantText = variantOfQuestion)
                    listOfVariantsView.add(questionVariantView)
                    variantsContainer.addView(questionVariantView)
                }

                listOfVariantsView.forEachIndexed { index, questionVariantView ->
                    choosingVariantClickAction(
                        questionVariantView = questionVariantView,
                        index = index,
                        singleQuestionItem = singleQuestionItem,
                        rightVariantView = listOfVariantsView.elementAt(singleQuestionItem.rightVariantIndex)
                    )
                }
                if (alreadyAnsweredQuestionsList.containsKey(singleQuestionItem.id)) {
                    singleQuestionItem.setAlreadyAnsweredQuestions(listOfVariantsView.toMutableList())
                }
            }
        }

        private fun SingleQuestionItem.setAlreadyAnsweredQuestions(listOfVariantsView: MutableList<QuestionVariantView>) {
            val rightVariantView = listOfVariantsView[rightVariantIndex]
            rightVariantView.setRightView()
            val alreadyAnsweredItem = alreadyAnsweredQuestionsList[id]

            if (rightVariantIndex != alreadyAnsweredItem?.chosenVariantIndex) {
                listOfVariantsView[alreadyAnsweredItem?.chosenVariantIndex ?: return].setWrongView()
            }
        }

        private fun choosingVariantClickAction(
            questionVariantView: QuestionVariantView,
            index: Int,
            singleQuestionItem: SingleQuestionItem,
            rightVariantView: QuestionVariantView
        ) {
            questionVariantView.setOnClickListener {
                if (alreadyAnsweredQuestionsList.containsKey(singleQuestionItem.id)) return@setOnClickListener
                if (index != singleQuestionItem.rightVariantIndex) {
                    questionVariantView.setWrongView()
                }
                rightVariantView.setRightView()
                alreadyAnsweredQuestionsList[singleQuestionItem.id] =
                    singleQuestionItem.copy(
                        chosenVariantIndex = index,
                        isAnsweredRight = index == singleQuestionItem.rightVariantIndex
                    )
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            SingleQuestionViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(
            viewBinding = view,
            context = context,
            alreadyAnsweredQuestionsList = alreadyAnsweredQuestionsList
        )
    }

    override fun getItemCount(): Int = questionsList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(questionsList[position])
        holder.setIsRecyclable(false)
        currentPosition = position
    }

    fun setData(questionsList: QuestionsList) {
        this.questionsList.clear()
        this.questionsList.addAll(questionsList.questionsList)
        notifyItemRangeChanged(0, this.questionsList.size)
    }
}