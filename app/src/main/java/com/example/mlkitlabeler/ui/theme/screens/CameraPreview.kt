package com.example.mlkitlabeler.ui.theme.screens

import androidx.camera.compose.CameraXViewfinder
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.mlkitlabeler.R
import com.example.mlkitlabeler.ui.theme.labelSmall
import com.example.mlkitlabeler.ui.theme.viewModel.CameraViewModel
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraPreview(
    viewModel: CameraViewModel = koinViewModel(),
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {
    val context = LocalContext.current
    val applicationContext = context.applicationContext
    val surfaceRequest by viewModel.surfaceRequest.collectAsState()
    val selectorCamera by viewModel.selectorCamera.collectAsState()
    val labels by viewModel.labelsList.collectAsState()
    val scaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(lifecycleOwner, selectorCamera) {
        viewModel.bindToCamera(applicationContext, lifecycleOwner)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        surfaceRequest?.let { request ->
            CameraXViewfinder(
                surfaceRequest = request,
                modifier = Modifier.fillMaxSize()
            )
        }
        LazyColumn(modifier = Modifier.align(Alignment.TopStart).padding(top = 24.dp, start = 16.dp)) {
            items(labels) {
                Row() {
                    Text(text = it.text, modifier = Modifier.padding(end = 16.dp), style = labelSmall)
                    Text(text = it.displayConfidence, style = labelSmall)
                }
            }
        }

        FloatingActionButton(
            onClick = { viewModel.toggleCamera() },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(painter = painterResource(R.drawable.cameraswitch), contentDescription = "")
        }

        FloatingActionButton(
            onClick = {
                viewModel.onCaptureImage(context)
            },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Icon(painter = painterResource(R.drawable.camera), contentDescription = "")
        }

        FloatingActionButton(
            onClick = {
                scope.launch { scaffoldState.bottomSheetState.expand() }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.folder),
                contentDescription = ""
            )
        }

        BottonContentApp(scaffoldState)
    }
}