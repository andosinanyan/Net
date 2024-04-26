package com.example.net

import com.example.net.helpercomponents.GeneralInfoHelper
import com.example.net.repo.QuestionsRepo
import com.example.net.repo.ThemesScreenRepo
import com.example.net.repo.impl.QuestionsRepoImpl
import com.example.net.repo.impl.ThemesScreenRepoImpl
import com.example.net.viewmodel.QuestionsViewModel
import com.example.net.viewmodel.ThemesScreenViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val koinModule = module {
    viewModel { ThemesScreenViewModel(repo = get()) }
    viewModel { QuestionsViewModel(repo = get(), backendMocker = get()) }
    single { GeneralInfoHelper(context = androidContext()) }
    single<QuestionsRepo> { QuestionsRepoImpl(context = androidContext()) }
    single<ThemesScreenRepo> { ThemesScreenRepoImpl(backendMocker = get()) }
}