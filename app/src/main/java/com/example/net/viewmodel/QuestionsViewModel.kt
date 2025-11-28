package com.example.net.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.net.entity.FinishingStates
import com.example.net.entity.QuestionsList
import com.example.net.entity.SingleQuestionItem
import com.example.net.helpercomponents.GeneralInfoHelper
import com.example.net.repo.QuestionsRepo
import kotlinx.coroutines.launch

class QuestionsViewModel(
    private val repo: QuestionsRepo,
    private val backendMocker: GeneralInfoHelper
) : ViewModel() {

    private var _questionsListLiveData = MutableLiveData<QuestionsList>()
    val questionsListLiveData: LiveData<QuestionsList> = _questionsListLiveData

    private var _showFinishingScreenLiveData = MutableLiveData<FinishingStates?>()
    val showFinishingScreenLiveData: LiveData<FinishingStates?> = _showFinishingScreenLiveData
    private var themeId = ""

    var testResult = Triple(0, 0, 0)

    fun getQuestionsById() {
        viewModelScope.launch {
            repo.getThemeQuestionsById("8").let {
                _questionsListLiveData.postValue(it)
            }
        }
    }

    fun getQuestionData(index: String) {
        viewModelScope.launch {
            repo.getThemeQuestionsById(index).let {
                themeId = index
                _questionsListLiveData.postValue(it)
            }
        }
    }

    fun clickedOnNextButton(listOfAnsweredQuestion: List<SingleQuestionItem>?, position: Int) {
        listOfAnsweredQuestion ?: return
        viewModelScope.launch {
            val (answeredRight, answeredWrong) = Pair(
                listOfAnsweredQuestion.filter { it.isAnsweredRight }.size,
                listOfAnsweredQuestion.filter { !it.isAnsweredRight }.size,
            )
            testResult = Triple(
                answeredRight,
                answeredWrong,
                (questionsListLiveData.value?.questionsList?.size
                    ?: 10).minus(answeredRight + answeredWrong)
            )
            if (listOfAnsweredQuestion.size == questionsListLiveData.value?.questionsList?.size) {
                _showFinishingScreenLiveData.postValue(FinishingStates.SHOW_RESULT_OF_TEST)
            } else if (questionsListLiveData.value?.questionsList?.size == position.plus(1)
                && listOfAnsweredQuestion.size != questionsListLiveData.value?.questionsList?.size
            ) {
                _showFinishingScreenLiveData.postValue(FinishingStates.FINISHING_NO_COMPLETE)
            }
        }
    }

    fun resetLiveDataOfFinishTest() {
        _showFinishingScreenLiveData.postValue(null)
    }

    fun saveData() {
        if (themeId.isNotEmpty()) {
            backendMocker.saveData(
                rightAnswer = testResult.first,
                wrongAnswer = testResult.second,
                id = themeId.toInt().plus(1).toString()
            )
        }
    }
}