package com.example.mlkitlabeler

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.example.mlkitlabeler.common.UsersPermissions.PERMISSIONS_CAMERA
import com.example.mlkitlabeler.common.UsersPermissions.PERMISSIONS_RECORD_AUDIO
import com.example.mlkitlabeler.ui.theme.MlKitLabelerTheme
import com.example.mlkitlabeler.ui.theme.screens.ContentApp
import com.example.mlkitlabeler.ui.theme.screens.OpenCamera

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        checkRequestPermission()
        enableEdgeToEdge()
        setContent {
            MlKitLabelerTheme {
                ContentApp()
            }
        }
    }

    fun checkRequestPermission() {
        if (!hasRequiredPermissions()) {
            ActivityCompat.requestPermissions(this, Permissions, 0)
        }
    }

    fun hasRequiredPermissions(): Boolean {
        return Permissions.all {
            ContextCompat.checkSelfPermission(
                application,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    companion object {
        private val Permissions = arrayOf(PERMISSIONS_CAMERA, PERMISSIONS_RECORD_AUDIO)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val navController = rememberNavController()
    MlKitLabelerTheme {
        Scaffold { innerPadding -> OpenCamera(navController, innerPadding) }
    }
}