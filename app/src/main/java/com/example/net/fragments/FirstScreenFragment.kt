package com.example.net.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.net.viewmodel.ThemesScreenViewModel
import com.example.net.databinding.FirstScreenFragmentBinding
import com.example.net.entity.SubMenuEntity
import com.example.net.view.SubMenuItemView
import com.example.net.R
import com.example.net.entity.ThemesScreenData
import com.example.net.entity.ThemesSubMenuTypes
import com.example.net.popup.NewsPopUpData
import org.koin.androidx.viewmodel.ext.android.viewModel

class FirstScreenFragment : Fragment() {

    private var viewBinding: FirstScreenFragmentBinding? = null
    private val viewModel: ThemesScreenViewModel by viewModel()

    override fun onResume() {
        super.onResume()
        requireActivity().findViewById<Toolbar>(R.id.toolbar).visibility = View.GONE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.themesScreenDataLiveData.observe(viewLifecycleOwner) {
            initSubMenus(it.subMenuItemEntity)
            initResultViews(it)
        }

        viewModel.getScreenData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FirstScreenFragmentBinding.inflate(LayoutInflater.from(context), container, false)
            .apply {
                viewBinding = this
            }.root
    }

    private fun initSubMenus(
        subMenuItemEntity: List<SubMenuEntity>
    ) {
        viewBinding?.subMenuContainer?.removeAllViews()
        subMenuItemEntity.forEach {
            val view = SubMenuItemView(context ?: return)
            view.id = View.generateViewId()
            view.initSubMenu(itemSubMenu = it)
            view.initSetOnClick(it) { tag, ifEmptyText, active, text ->
                openNextFragment(tag ?: return@initSetOnClick)
            }
            viewBinding?.subMenuContainer?.addView(view)
        }
    }

    private fun initResultViews(terms: ThemesScreenData) {
        viewBinding?.run {
            answeredResultView.initView(
                backgroundColor = "#F6F6F6",
                descriptionText = terms.resultsTriedText,
                doneNumberText = terms.triedCount,
                totalCountText = terms.allQuestionCount
            )
            rightAnsweredResultView.initView(
                backgroundColor = "#4D34C759",
                descriptionText = terms.resultsCorrectsText,
                doneNumberText = terms.correctsCount,
                totalCountText = terms.allQuestionCount
            )
            wrongAnsweredResultView.initView(
                backgroundColor = "#4DFE8A51",
                descriptionText = terms.resultsIncorrectText,
                doneNumberText = terms.incorrectCount,
                totalCountText = terms.allQuestionCount
            )
        }
    }

    private fun openNextFragment(tag: String) {
        findNavController().navigate(determineScreen(tag) ?: return)
    }

    private fun determineScreen(tag: String): Int? {
        return when (tag) {
            ThemesSubMenuTypes.TESTS.value -> {
                R.id.action_firstScreenFragment_to_testsFragment
            }

            ThemesSubMenuTypes.EXAM.value -> {
                R.id.action_firstScreenFragment_to_questionsScreenFragment
            }

            ThemesSubMenuTypes.MEDIA_EDUCATION.value -> {
                R.id.action_firstScreenFragment_to_socialEngineering
            }

            else -> {
                NewsPopUpData(context ?: return null).show()
                null
            }
        }
    }
}