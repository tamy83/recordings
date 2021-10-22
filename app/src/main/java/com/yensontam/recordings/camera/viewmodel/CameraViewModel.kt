package com.yensontam.recordings.camera.viewmodel

import android.app.Application
import android.os.CountDownTimer
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yensontam.recordings.camera.state.*
import com.yensontam.recordings.camera.view.CameraActivity
import com.yensontam.recordings.main.viewmodel.SingleLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

class CameraViewModel(application: Application): AndroidViewModel(application), CameraInteractor.TimeRemainingListener {

  val TAG = "CAMERAACTIVITYUNIQUE"
  val stateLiveData = MutableLiveData(CameraActivityState(0))
  val effectSingleLiveEvent = SingleLiveEvent<CameraActivityViewEffect>()

  val currentState: CameraActivityState
    get() {
      return stateLiveData.value ?: CameraActivityState(0)
    }

  private var durationMs = 0L
  private lateinit var fileName: String
  val filePath: String
    get() {
      val directory = interactor.getDirectory() ?: ""
      return directory + File.separator + fileName
    }

  override fun onTimeLeft(timeLeft: Int) {
    currentState.consumeAction(CameraActivityAction.HasTimeRemainingAction(timeLeft))
    //effectSingleLiveEvent.postValue()
  }

  private lateinit var interactor: CameraInteractor
  private lateinit var countDownTimer: CountDownTimer

  fun onIntentReceived(intent: CameraActivityIntent) {
    when (intent) {
      is CameraActivityIntent.LoadedIntent -> {
        handledLoadedIntent(intent)
      }
      is CameraActivityIntent.PauseIntent -> {
        handlePauseIntent(intent)
      }
      is CameraActivityIntent.ResumeIntent -> {

      }
      is CameraActivityIntent.CameraIdleIntent -> {
        handledCameraIdleIntent(intent)
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
      interactor = CameraInteractorImpl(application = getApplication(), this, duration)
      val directory = interactor.getDirectory()
      if (directory == null) {
        effectSingleLiveEvent.postValue(CameraActivityViewEffect.Finish)
        return
      }
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

  private fun handledCameraIdleIntent(intent: CameraActivityIntent.CameraIdleIntent) {
    //stateLiveData.postValue(currentState.consumeAction(CameraActivityAction.OneTimeAction(CameraActivityViewEffect.Pause)))
  }

  private fun handlePauseIntent(intent: CameraActivityIntent.PauseIntent) {
    effectSingleLiveEvent.postValue(CameraActivityViewEffect.Stop)
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

  fun getDirectory(): String? {
    return getApplication<Application>().getExternalFilesDir("recordings")?.path
  }

  private fun startTimer() {
    Log.d(TAG, "start timer")
    viewModelScope.launch {
      countDownTimer = object : CountDownTimer(durationMs, 1000) {
        override fun onTick(timeRemainingMs: Long) {
          val timeRemainingInSeconds = (timeRemainingMs / 1000).toInt()
          Log.d(TAG, "on Tick $timeRemainingMs $timeRemainingInSeconds")
          stateLiveData.postValue(currentState.consumeAction(CameraActivityAction.HasTimeRemainingAction(timeRemainingInSeconds)))
        }

        override fun onFinish() {
          effectSingleLiveEvent.postValue(CameraActivityViewEffect.Stop)
        }
      }
      countDownTimer.start()
    }
  }

}