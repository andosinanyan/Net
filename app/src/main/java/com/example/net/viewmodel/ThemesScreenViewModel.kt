package com.example.net.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.net.entity.AllTestData
import com.example.net.entity.ThemesScreenData
import com.example.net.repo.ThemesScreenRepo
import kotlinx.coroutines.launch

class ThemesScreenViewModel(
    private val repo: ThemesScreenRepo
) : ViewModel() {


    private var _themesScreenDataLiveData = MutableLiveData<ThemesScreenData>()
    val themesScreenDataLiveData: LiveData<ThemesScreenData> = _themesScreenDataLiveData


    private var _allTestInfoLiveData = MutableLiveData<AllTestData>()
    val allTestInfoLiveData: LiveData<AllTestData> = _allTestInfoLiveData

    fun getQuizList() {
        viewModelScope.launch {
            repo.getAllTestData().let {
                _allTestInfoLiveData.postValue(it)
            }
        }
    }

    fun getScreenData() {
        viewModelScope.launch {
            repo.getThemesMenuData().let {
                _themesScreenDataLiveData.postValue(it)
            }
        }
    }
}