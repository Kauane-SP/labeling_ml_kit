package com.example.mlkitlabeler.ui.theme.screens

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.mlkitlabeler.navigation.NavigationGraph

@Composable
fun ContentApp() {
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) { paddingValues ->
        NavigationGraph(navController, paddingValues)
    }
}
