package com.example.net.repo

import com.example.net.entity.AllTestData
import com.example.net.entity.ThemesScreenData

interface ThemesScreenRepo {
    suspend fun getThemesMenuData() : ThemesScreenData

    suspend fun getAllTestData(): AllTestData
}