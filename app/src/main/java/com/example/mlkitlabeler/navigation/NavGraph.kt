package com.example.mlkitlabeler.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.mlkitlabeler.ui.theme.screens.BottonContentApp
import com.example.mlkitlabeler.ui.theme.screens.CameraPreview
import com.example.mlkitlabeler.ui.theme.screens.OpenCamera

@Composable
fun NavigationGraph(navHostController: NavHostController, paddingValues: PaddingValues) {
    NavHost(navController = navHostController, startDestination = "Home") {
        composable("Home") {
            OpenCamera(navHostController, paddingValues = paddingValues)
        }
        composable("Camera") {
            CameraPreview()
        }
    }
}