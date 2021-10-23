package com.yensontam.recordings.camera.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.core.VideoCapture
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.yensontam.recordings.camera.state.CameraActivityIntent
import com.yensontam.recordings.camera.state.CameraActivityState
import com.yensontam.recordings.camera.state.CameraActivityViewEffect
import com.yensontam.recordings.camera.viewmodel.CameraViewModel
import com.yensontam.recordings.databinding.ActivityCameraBinding
import java.io.File
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class CameraActivity: AppCompatActivity() {

  private lateinit var viewModel: CameraViewModel
  private lateinit var binding: ActivityCameraBinding
  private lateinit var timeLeftTextView: TextView
  private lateinit var cameraExecutor: Executor
  private lateinit var previewView: PreviewView

  private lateinit var videoCapture: VideoCapture
  private lateinit var outputOptions: VideoCapture.OutputFileOptions

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivityCameraBinding.inflate(layoutInflater)
    setContentView(binding.root)

    val cameraViewModel: CameraViewModel by viewModels()

    viewModel = cameraViewModel
    viewModel.onIntentReceived(CameraActivityIntent.LoadedIntent(intent))

    viewModel.stateLiveData.observe(this, {
      renderState(it)
    })
    viewModel.effectSingleLiveEvent.observe(this, {
      showViewEffect(it)
    })

    timeLeftTextView = binding.timeLeftTextView
    cameraExecutor = Executors.newSingleThreadExecutor()
    previewView = binding.cameraPreviewView
  }


  private fun renderState(state: CameraActivityState) {
    timeLeftTextView.text = state.secondsRemaining.toString()
  }

  private fun showViewEffect(viewEffect: CameraActivityViewEffect) {
    when (viewEffect) {
      is CameraActivityViewEffect.Finish -> finish()
      is CameraActivityViewEffect.Prepare -> {
        setupCamera()
      }
      is CameraActivityViewEffect.Record -> {
        startVideoCapture(viewEffect.filePath)
      }
      is CameraActivityViewEffect.Stop -> {
        stopVideoCapture()
      }
    }
  }

  @SuppressLint("RestrictedApi")
  private fun startVideoCapture(filePath: String) {
    val videoFile = File(filePath)
    outputOptions = VideoCapture.OutputFileOptions.Builder(videoFile).build()

    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
      videoCapture.startRecording(
        outputOptions,
        ContextCompat.getMainExecutor(this),
        object : VideoCapture.OnVideoSavedCallback {
          override fun onVideoSaved(outputFileResults: VideoCapture.OutputFileResults) {
            viewModel.onIntentReceived(CameraActivityIntent.FileSavedIntent)
          }

          override fun onError(videoCaptureError: Int, message: String, cause: Throwable?) {
            viewModel.onIntentReceived(CameraActivityIntent.ErrorIntent)
          }
        })
    }
  }

  @SuppressLint("RestrictedApi")
  private fun stopVideoCapture() {
    videoCapture.stopRecording()
  }

  @SuppressLint("RestrictedApi")
  private fun setupCamera() {
    val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

    cameraProviderFuture.addListener(Runnable {
      val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

      val preview = Preview.Builder().build().also {
        it.setSurfaceProvider(previewView.surfaceProvider)
      }

      val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

      videoCapture = VideoCapture.Builder().apply {
        setTargetRotation(previewView.display.rotation)
        setCameraSelector(cameraSelector)
      }.build()

      try {
        cameraProvider.unbindAll()

        cameraProvider.bindToLifecycle(this, cameraSelector, preview, videoCapture)
      } catch (exc: Exception) {
        viewModel.onIntentReceived(CameraActivityIntent.CameraStopIntent)
      }

    }, ContextCompat.getMainExecutor(this))

    previewView.previewStreamState.observe(this, {
      onPreviewStateChanged(it)
    })
  }

  private fun onPreviewStateChanged(state: PreviewView.StreamState) {
    when (state) {
      PreviewView.StreamState.IDLE -> {
      }
      PreviewView.StreamState.STREAMING -> {
        viewModel.onIntentReceived(CameraActivityIntent.CameraStreamingIntent)
      }
    }
  }

  companion object {
    const val EXTRA_FILE_NAME = "extra_file_name"
    const val EXTRA_DURATION_SECONDS = "extra_duration"

    @JvmStatic
    fun getStartIntent(context: Context, fileName: String, duration: Int): Intent {
      return Intent(context, CameraActivity::class.java).apply {
        putExtra(EXTRA_FILE_NAME, fileName)
        putExtra(EXTRA_DURATION_SECONDS, duration)
      }
    }
  }
}