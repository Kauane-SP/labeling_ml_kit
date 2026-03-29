package com.example.mlkitlabeler.module

import androidx.room.Room
import com.example.mlkitlabeler.data.ImageDataBase
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module
import com.example.mlkitlabeler.ui.theme.viewModel.CameraViewModel
import org.koin.android.ext.koin.androidApplication

val applicationModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            ImageDataBase::class.java,
            "labeler.bd"
        ).build()
    }

    single { get<ImageDataBase>().getRunDao() }

    viewModelOf(::CameraViewModel)
}