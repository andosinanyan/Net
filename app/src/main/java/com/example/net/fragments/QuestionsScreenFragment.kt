package com.example.net.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.net.R
import com.example.net.TEST_ID_KEY
import com.example.net.adapter.NoScrollLayoutManager
import com.example.net.adapter.QuestionAdapter
import com.example.net.databinding.QuestionsScreenFragmentBinding
import com.example.net.popup.TestFinishDialog
import com.example.net.viewmodel.QuestionsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class QuestionsScreenFragment(private val openedFromCyber: Boolean = false, private val clickedOnHintCallBack: ((Int, Int) -> Unit)? = null,) : Fragment() {

    private var binding: QuestionsScreenFragmentBinding? = null
    private val viewModel: QuestionsViewModel by viewModel()
    private var questionViewAdapter: QuestionAdapter? = null
    private var questionsCount = ""
    private val testId: String? by lazy { arguments?.getString(TEST_ID_KEY) }
    private var testFinishDialog: TestFinishDialog? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return QuestionsScreenFragmentBinding.inflate(
            LayoutInflater.from(context),
            container,
            false
        ).apply {
            binding = this
        }.root
    }

    override fun onResume() {
        super.onResume()
        if (testId != null) {
            (activity as AppCompatActivity).supportActionBar?.title =
                "Թեստ ${testId?.toInt()?.plus(1)}"
        } else {
            (activity as AppCompatActivity).supportActionBar?.title = "Թվային գրագիտություն"
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().findViewById<Toolbar>(R.id.toolbar).visibility = View.VISIBLE

        if (testId != null) {
            viewModel.getQuestionData(testId ?: "")
        } else {
            viewModel.getQuestionsById()
        }
        viewModel.questionsListLiveData.observe(viewLifecycleOwner) {
            questionViewAdapter?.setData(it)
            questionsCount = it.questionsList.size.toString()
        }

        viewModel.showFinishingScreenLiveData.observe(viewLifecycleOwner) { screenType ->
            screenType ?: return@observe
            viewModel.resetLiveDataOfFinishTest()
            testFinishDialog = TestFinishDialog(
                context = context ?: return@observe,
                screenType = screenType,
                triple = viewModel.testResult,
                finishScreen = ::finishTest
            )
            testFinishDialog?.show()
        }

        binding?.questionsRecyclerView?.run {
            questionViewAdapter = QuestionAdapter(context, openedCyberSecurity = openedFromCyber, ::changeCountOfNumber, ::clickedOnHint)
            this.adapter = questionViewAdapter
            layoutManager = NoScrollLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            val pager = PagerSnapHelper()
            pager.attachToRecyclerView(this)
        }

        binding?.nextButton?.setOnClickListener {
            if (questionsCount == questionViewAdapter?.currentPosition?.plus(1).toString()) {
                viewModel.clickedOnNextButton(
                    questionViewAdapter?.alreadyAnsweredQuestionsList?.values?.toList(),
                    questionViewAdapter?.currentPosition ?: return@setOnClickListener
                )
            } else {
                binding?.questionsRecyclerView?.scrollToPosition(
                    questionViewAdapter?.currentPosition?.plus(
                        1
                    ) ?: return@setOnClickListener
                )
            }

        }

        binding?.backButton?.setOnClickListener {
            binding?.questionsRecyclerView?.scrollToPosition(
                questionViewAdapter?.currentPosition?.minus(
                    1
                ) ?: return@setOnClickListener
            )
        }
    }

    private fun clickedOnHint(startIndex: Int, endIndex: Int) {
        clickedOnHintCallBack?.invoke(startIndex, endIndex)
    }

    private fun finishTest() {
        findNavController().popBackStack()
        viewModel.saveData()
        testFinishDialog?.dismiss()
    }

    override fun onPause() {
        super.onPause()
        (activity as AppCompatActivity).supportActionBar?.title = ""
    }

    private fun changeCountOfNumber(
        position: Int,
    ) {
        binding?.run {
            nextButton.text =
                if (questionsCount == position.plus(1).toString()) "Ավարտել" else "Առաջ"
            countTextView.text = "${position + 1}/$questionsCount"
        }
    }
}