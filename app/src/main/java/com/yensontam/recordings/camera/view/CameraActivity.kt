package com.yensontam.recordings.camera.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaRecorder
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.Camera
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

import androidx.lifecycle.MutableLiveData




class CameraActivity: AppCompatActivity() {

  val TAG = "CAMERAACTIVITYUNIQUE"
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
    Log.d(TAG, "render state $state")
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
            Log.d(TAG, "SAVED VIDEO!!!")
            viewModel.onIntentReceived(CameraActivityIntent.FileSavedIntent)
          }

          override fun onError(videoCaptureError: Int, message: String, cause: Throwable?) {
            viewModel.onIntentReceived(CameraActivityIntent.ErrorIntent)
            Log.d(TAG, "ERROR $videoCaptureError message $message cause ${cause?.message}")
          }
        })
    }
  }

  @SuppressLint("RestrictedApi")
  private fun stopVideoCapture() {
    videoCapture.stopRecording()
  }

  /*

    /**
     * An unknown error occurred.
     *
     * <p>See message parameter in onError callback or log for more details.
     */
    public static final int ERROR_UNKNOWN = 0;
    /**
     * An error occurred with encoder state, either when trying to change state or when an
     * unexpected state change occurred.
     */
    public static final int ERROR_ENCODER = 1;
    /** An error with muxer state such as during creation or when stopping. */
    public static final int ERROR_MUXER = 2;
    /**
     * An error indicating start recording was called when video recording is still in progress.
     */
    public static final int ERROR_RECORDING_IN_PROGRESS = 3;
    /**
     * An error indicating the file saving operations.
     */
    public static final int ERROR_FILE_IO = 4;
    /**
     * An error indicating this VideoCapture is not bound to a camera.
     */
    public static final int ERROR_INVALID_CAMERA = 5;
    /**
     * An error indicating the video file is too short.
     * <p> The output file will be deleted if the OutputFileOptions is backed by File or uri.
     */
    public static final int ERROR_RECORDING_TOO_SHORT = 6;
   */

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
        Log.e(TAG, "Use case binding failed", exc)
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

  override fun onPause() {
    super.onPause()
    //viewModel.onIntentReceived(CameraActivityIntent.PauseIntent)
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