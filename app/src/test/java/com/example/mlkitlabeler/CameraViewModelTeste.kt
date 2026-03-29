package com.example.mlkitlabeler

import android.content.Context
import android.net.Uri
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.test.core.app.ApplicationProvider
import com.example.mlkitlabeler.data.ImageDataBase
import com.example.mlkitlabeler.ui.theme.viewModel.CameraViewModel
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.ImageLabeler
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.mockkConstructor
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CameraViewModelTeste {

    private lateinit var viewModel: CameraViewModel
    private val dataBase = mockk<ImageDataBase>(relaxed = true)
    private val mockImageCapture = mockk<ImageCapture>(relaxed = true)
    private val mockImageLabeler = mockk<ImageLabeler>(relaxed = true)
    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Before
    fun setup() {
        mockkStatic(ImageLabeling::class)
        every { ImageLabeling.getClient(any()) } returns mockImageLabeler

        mockkConstructor(ImageCapture.Builder::class)
        every { anyConstructed<ImageCapture.Builder>().build() } returns mockImageCapture

        viewModel = CameraViewModel(dataBase)
    }

    @After
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun toggleCamera_whenSelectedCamera_shouldChangeState() {
        val initialSelector = viewModel.selectorCamera.value
        assertEquals(initialSelector, false)

        viewModel.toggleCamera()

        val finalSelector = viewModel.selectorCamera.value
        assertEquals(finalSelector, true)
        assertNotEquals(initialSelector, finalSelector)
    }

    @Test
    fun getCameraSelector_WhenChangeCamera_ShouldReturnFrontCamera() {
        viewModel.toggleCamera()
        val result = viewModel.getCameraSelector()
        assertEquals(result, CameraSelector.DEFAULT_FRONT_CAMERA)
    }

    @Test
    fun getCameraSelector_WhenInitApp_ShouldReturnBackCamera() {
        val result = viewModel.getCameraSelector()
        assertEquals(result, CameraSelector.DEFAULT_BACK_CAMERA)
    }

    @Test
    fun onCaptureImage_WhenCapturePhoto_ShouldReturnUriList() {
        val uri = Uri.parse("content://media/external/images/media/1")
        val mockResults = mockk<ImageCapture.OutputFileResults>()
        every { mockResults.savedUri } returns uri

        mockkConstructor(ImageCapture.OutputFileOptions.Builder::class)
        every { anyConstructed<ImageCapture.OutputFileOptions.Builder>().build() } returns mockk(relaxed = true)

        val callbackSlot = slot<ImageCapture.OnImageSavedCallback>()
        every {
            mockImageCapture.takePicture(
                any<ImageCapture.OutputFileOptions>(),
                any(),
                capture(callbackSlot)
            )
        } just Runs

        viewModel.onCaptureImage(context)
        callbackSlot.captured.onImageSaved(mockResults)

        val result = viewModel.imageList.value
        assertEquals(listOf(uri), result)
    }

    @Test
    fun onCaptureImage_WhenCapturePhotoWhitError_ShouldNotUpdateImageList() {
        val exception = ImageCaptureException(
            ImageCapture.ERROR_UNKNOWN,
            "Erro simulado",
            null
        )

        mockkConstructor(ImageCapture.OutputFileOptions.Builder::class)
        every { anyConstructed<ImageCapture.OutputFileOptions.Builder>().build() } returns mockk(relaxed = true)

        val callbackSlot = slot<ImageCapture.OnImageSavedCallback>()
        every {
            mockImageCapture.takePicture(
                any<ImageCapture.OutputFileOptions>(),
                any(),
                capture(callbackSlot)
            )
        } just Runs

        viewModel.onCaptureImage(context)
        callbackSlot.captured.onError(exception)

        val result = viewModel.imageList.value
        assertEquals(emptyList<Uri>(), result)
    }
}