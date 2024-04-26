package com.example.net.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.example.net.viewmodel.ThemesScreenViewModel
import com.example.net.databinding.FirstScreenFragmentBinding
import com.example.net.entity.SubMenuEntity
import com.example.net.entity.ThemesScreenGeneralSettingsData
import com.example.net.view.SubMenuItemView
import com.example.net.adapter.ImageSlider
import com.example.net.R
import com.example.net.entity.ThemesScreenData
import com.example.net.entity.ThemesSubMenuTypes
import com.example.net.popup.NewsPopUpData
import com.example.net.popup.TestFinishDialog
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

        viewBinding?.imagesSliderRecyclerView?.run {
            val adapter = ImageSlider()
            val snapHelper = PagerSnapHelper()
            this.adapter = adapter
            adapter.setImagesData(
                listOf(
                    "https://ca-times.brightspotcdn.com/dims4/default/606e851/2147483647/strip/false/crop/5716x3811+0+0/resize/1486x991!/quality/75/?url=https%3A%2F%2Fcalifornia-times-brightspot.s3.amazonaws.com%2F5c%2Fdc%2F37693ad24a18b20c84335d62ebaf%2Fap22174736555411.jpg",
                    "https://azatazen.am/images/photos/heroPic2.png"
                )
            )
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            snapHelper.attachToRecyclerView(this)
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

            else -> {
                NewsPopUpData(context ?: return null).show()
                null
            }
        }
    }
}