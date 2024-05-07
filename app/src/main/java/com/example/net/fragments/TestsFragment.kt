package com.example.net.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.net.MAIN_SHARED_PREFERENCES
import com.example.net.R
import com.example.net.TEST_ID_KEY
import com.example.net.adapter.AllTestsRecyclerViewAdapter
import com.example.net.databinding.TestsListLayoutBinding
import com.example.net.viewmodel.ThemesScreenViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class TestsFragment : Fragment() {

    private val viewModel: ThemesScreenViewModel by viewModel()
    private var viewBinding: TestsListLayoutBinding? = null


    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.title = "Թեստեր"
        requireActivity().findViewById<Toolbar>(R.id.toolbar).visibility = View.VISIBLE
    }

    override fun onPause() {
        super.onPause()
        (activity as AppCompatActivity).supportActionBar?.title = ""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return TestsListLayoutBinding.inflate(LayoutInflater.from(context), container, false)
            .apply { viewBinding = this }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = context?.let {
            AllTestsRecyclerViewAdapter(context = it) { chosenQuizId ->
                openNextFragment(chosenQuizId.minus(1).toString())
            }
        }
        viewBinding?.run {
            allTestRecyclerView.layoutManager = GridLayoutManager(context, 1)
            allTestRecyclerView.adapter = adapter
        }

        viewModel.allTestInfoLiveData.observe(viewLifecycleOwner) {
            adapter?.setTestData(data = it, testTitle = "")
        }

        viewModel.getQuizList()
    }

    private fun openNextFragment(chosenVariant: String) {
        val bundle = Bundle()
        bundle.putString(TEST_ID_KEY, chosenVariant)
        findNavController().navigate(
            resId = R.id.action_testsFragment_to_questionsScreenFragment,
            args = bundle
        )
    }
}