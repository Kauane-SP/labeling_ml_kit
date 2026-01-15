package com.example.mlkitlabeler.module

import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import com.example.mlkitlabeler.ui.theme.viewModel.CameraViewModel

val applicationModule = module {
    viewModelOf(::CameraViewModel)
}