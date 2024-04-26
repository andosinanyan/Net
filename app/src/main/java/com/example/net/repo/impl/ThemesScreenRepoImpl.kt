package com.example.net.repo.impl

import com.example.net.entity.AllTestData
import com.example.net.entity.ThemesScreenData
import com.example.net.extentions.mockData
import com.example.net.helpercomponents.GeneralInfoHelper
import com.example.net.repo.ThemesScreenRepo

class ThemesScreenRepoImpl(private val backendMocker: GeneralInfoHelper) : ThemesScreenRepo {
    override suspend fun getThemesMenuData(): ThemesScreenData {
        return backendMocker.getGeneralJson()
    }

    override suspend fun getAllTestData(): AllTestData {
        return backendMocker.getGeneralJson()
    }
}

