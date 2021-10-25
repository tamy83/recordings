package com.yensontam.recordings.camera.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yensontam.recordings.Config
import com.yensontam.recordings.camera.state.*
import com.yensontam.recordings.camera.view.CameraActivity
import com.yensontam.recordings.SingleLiveEvent
import com.yensontam.recordings.camera.CameraInteractor
import kotlinx.coroutines.launch
import java.io.File

class CameraViewModel(application: Application, private val config: Config, private val interactor: CameraInteractor): AndroidViewModel(application), CameraInteractor.CameraInteractorListener {

  val stateLiveData = MutableLiveData(CameraActivityState(0))
  val effectSingleLiveEvent =
    SingleLiveEvent<CameraActivityViewEffect>()

  val currentState: CameraActivityState
    get() {
      return stateLiveData.value ?: CameraActivityState(0)
    }

  init {
    interactor.listener = this
  }

  private lateinit var recordingsDirectory: String
  private var durationMs = 0L
  private lateinit var fileName: String
  private val filePath: String
    get() = recordingsDirectory + File.separator + fileName

  fun onIntentReceived(intent: CameraActivityIntent) {
    when (intent) {
      is CameraActivityIntent.LoadedIntent -> {
        handledLoadedIntent(intent)
      }
      is CameraActivityIntent.CameraStreamingIntent -> {
        handledCameraStreamingIntent(intent)
      }
      is CameraActivityIntent.CameraStopIntent -> {
        handleCameraStopIntent(intent)
      }
      is CameraActivityIntent.ErrorIntent -> {
        handleCameraErrorIntent(intent)
      }
      is CameraActivityIntent.FileSavedIntent -> {
        handleFileSavedIntent(intent)
      }
    }
  }

  private fun handledLoadedIntent(loadedIntent: CameraActivityIntent.LoadedIntent) {
    val intent = loadedIntent.intent
    val fileName = intent.getStringExtra(CameraActivity.EXTRA_FILE_NAME)
    val duration = intent.getIntExtra(CameraActivity.EXTRA_DURATION_SECONDS, 0)

    if (fileName != null && duration > 0) {
      this.fileName = fileName
      durationMs = (duration * 1000).toLong()
      recordingsDirectory = config.recordingsDirectory
      stateLiveData.postValue(currentState.consumeAction(CameraActivityAction.HasTimeRemainingAction(duration)))
      effectSingleLiveEvent.postValue(CameraActivityViewEffect.Prepare)
    } else {
      effectSingleLiveEvent.postValue(CameraActivityViewEffect.Finish)
    }
  }

  private fun handledCameraStreamingIntent(intent: CameraActivityIntent.CameraStreamingIntent) {
    startTimer()
    effectSingleLiveEvent.postValue(CameraActivityViewEffect.Record(filePath = filePath))
  }

  private fun handleCameraStopIntent(intent: CameraActivityIntent.CameraStopIntent) {
    effectSingleLiveEvent.postValue(CameraActivityViewEffect.Finish)
  }

  private fun handleCameraErrorIntent(intent: CameraActivityIntent.ErrorIntent) {
    effectSingleLiveEvent.postValue(CameraActivityViewEffect.Stop)
  }

  private fun handleFileSavedIntent(intent: CameraActivityIntent.FileSavedIntent) {
    effectSingleLiveEvent.postValue(CameraActivityViewEffect.Finish)
  }

  private fun startTimer() {
    viewModelScope.launch {
      interactor.startTimer(durationMs, 1000)
    }
  }

  override fun onTimerTick(timeRemainingMs: Long) {
    val timeRemainingInSeconds = (timeRemainingMs / 1000).toInt()
    stateLiveData.postValue(currentState.consumeAction(CameraActivityAction.HasTimeRemainingAction(timeRemainingInSeconds)))
  }

  override fun onTimerFinish() {
    effectSingleLiveEvent.postValue(CameraActivityViewEffect.Stop)
  }
}