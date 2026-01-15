package com.example.mlkitlabeler.ui.theme.viewModel

import android.content.ContentValues
import android.content.Context
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.OptIn
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.lifecycle.awaitInstance
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import coil3.Uri
import coil3.toCoilUri
import com.example.mlkitlabeler.DetectedLabel
import com.google.mlkit.common.model.LocalModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.custom.CustomImageLabelerOptions
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CameraViewModel() : ViewModel() {

    private val _surfaceRequest = MutableStateFlow<SurfaceRequest?>(null)
    val surfaceRequest: StateFlow<SurfaceRequest?> = _surfaceRequest

    private val _selectorCamera = MutableStateFlow<Boolean>(false)
    var selectorCamera: StateFlow<Boolean> = _selectorCamera

    private val _imageList = MutableStateFlow<List<Uri>>(emptyList())
    var imageList: StateFlow<List<Uri>> = _imageList

    private val _labelsList = MutableStateFlow<List<DetectedLabel>>(emptyList())
    var labelsList: StateFlow<List<DetectedLabel>> = _labelsList

    private val cameraPreviewUseCase = Preview.Builder().build().apply {
        setSurfaceProvider { newSurfaceRequest ->
            _surfaceRequest.value = newSurfaceRequest
        }
    }

    val customModel = LocalModel.Builder().setAssetFilePath("model.tflite").build()

    val customOptions = CustomImageLabelerOptions.Builder(customModel).setConfidenceThreshold(0.5f)
        .setConfidenceThreshold(0.2f).build()
    private val labeler =
        ImageLabeling.getClient(customOptions)

//    private val labeler =
//        ImageLabeling.getClient(ImageLabelerOptions.Builder().setConfidenceThreshold(0.7f).build())

    private val imageAnalyzer = ImageAnalysis.Builder()
        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
        .build()

    private val imageCapture =
        ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY).build()

    fun toggleCamera() {
        _selectorCamera.value = !_selectorCamera.value
    }

    fun getCameraSelector(): CameraSelector {
        return if (_selectorCamera.value) {
            CameraSelector.DEFAULT_FRONT_CAMERA
        } else {
            CameraSelector.DEFAULT_BACK_CAMERA
        }
    }

    suspend fun bindToCamera(appContext: Context, lifecycleOwner: LifecycleOwner) {
        val processCameraProvider = ProcessCameraProvider.awaitInstance(appContext)

        imageAnalyzer.setAnalyzer(ContextCompat.getMainExecutor(appContext)) { imageProxy ->
            processRealTime(imageProxy, appContext)
        }

        processCameraProvider.unbindAll()
        processCameraProvider.bindToLifecycle(
            lifecycleOwner,
            getCameraSelector(),
            imageCapture,
            cameraPreviewUseCase,
            imageAnalyzer
        )
    }

    @OptIn(ExperimentalGetImage::class)
    private fun processRealTime(imageProxy: ImageProxy, context: Context) {
        if (imageProxy.image != null) {
            val image = InputImage.fromMediaImage(
                imageProxy.image, imageProxy.imageInfo.rotationDegrees
            )

            val newLabels: List<String> = context.resources.assets
                .open("labels.txt")
                .bufferedReader()
                .readLines()

            labeler.process(image)
                .addOnSuccessListener { labels ->

                    val detectedLabels = labels.map { label ->
                        DetectedLabel(
                            text = "${newLabels[label.index]}",
                            confidence = label.confidence,
                            displayConfidence = "${(label.confidence * 100).toInt()}%"
                        )
                    }
                    _labelsList.value = detectedLabels
                    Log.e("LABELS", labels.toString())
                }
                .addOnFailureListener {
                    imageProxy.close()
                    Log.e("LABELS", "Deu ruim")
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
        } else {
            imageProxy.close()
        }
    }

    fun onCaptureImage(context: Context) {
        capturePhoto(context)
    }

    private fun capturePhoto(context: Context) {
        val capture = imageCapture ?: return

        val name = "IMG_${System.currentTimeMillis()}.jpg"
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, name)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }

        val outputOptions = ImageCapture.OutputFileOptions.Builder(
            context.contentResolver,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        ).build()

        capture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(context),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val uri = outputFileResults.savedUri
                    _imageList.value += uri.toCoilUri()

                    analyzeSaveImage(context, uri)
                }

                override fun onError(exception: ImageCaptureException) {
                    // Erro: output.savedUri
                }

            })
    }

    private fun analyzeSaveImage(context: Context, uri: android.net.Uri) {
        val image = InputImage.fromFilePath(context, uri)

        labeler.process(image)
            .addOnSuccessListener { labels ->
                val teste = labels
            }
            .addOnFailureListener { e ->
                Log.e("DEU RUIM", e.message.toString())
            }
    }

    companion object {
        const val FORMAT_CONFIDENCE = "%.1f%%"
    }
}