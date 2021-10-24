package com.yensontam.recordings.record.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.yensontam.recordings.SingleLiveEvent
import com.yensontam.recordings.camera.view.CameraActivity
import com.yensontam.recordings.helper.FileNameValidator
import com.yensontam.recordings.record.RecordInteractor
import com.yensontam.recordings.record.model.SliderInfo
import com.yensontam.recordings.record.state.RecordAction
import com.yensontam.recordings.record.state.RecordIntent
import com.yensontam.recordings.record.state.RecordState
import com.yensontam.recordings.record.state.RecordViewEffect

class RecordViewModel(application: Application, private val fileNameValidator: FileNameValidator, private val interactor: RecordInteractor) : AndroidViewModel(application) {

  internal var sliderInfo = SliderInfo(value = 3F, valueFrom = 3F, valueTo = 180F, stepSize = 1F)
  private val duration: Float
    get() = sliderInfo.value

  private var fileName: String? = null

  private val isStartCameraButtonEnabled: Boolean
    get() = fileNameValidator.isValidFileName(fileName)

  private val initialState = RecordState(fileName = null, sliderInfo = sliderInfo, isStartCameraButtonEnabled = isStartCameraButtonEnabled)

  val stateLiveData = MutableLiveData(initialState)
  val effectSingleLiveEvent = SingleLiveEvent<RecordViewEffect>()

  private val currentState: RecordState
    get() {
      return stateLiveData.value ?: initialState
    }

  private val permissionsGranted: Boolean
    get() {
      return interactor.hasAllPermissions(CameraActivity.REQUIRED_PERMISSIONS, getApplication())
    }

  fun onIntentReceived(intent: RecordIntent) {
    when(intent) {
      is RecordIntent.LoadedIntent -> {
        handleLoadedIntent(intent)
      }
      is RecordIntent.StartCameraIntent -> {
        handleStartCameraIntent(intent)
      }
      is RecordIntent.SetFileNameIntent -> {
        handleSetFileNameIntent(intent)
      }
      is RecordIntent.SetDurationIntent -> {
        handleSetDurationIntent(intent)
      }
    }
  }

  private fun handleLoadedIntent(intent: RecordIntent.LoadedIntent) {
    stateLiveData.postValue(initialState)
  }

  private fun handleStartCameraIntent(intent: RecordIntent.StartCameraIntent) {
    if (!permissionsGranted) {
      effectSingleLiveEvent.postValue(RecordViewEffect.RequestPermissionsViewEffect(CameraActivity.REQUIRED_PERMISSIONS))
    } else {
      val file = fileName
      file?.let {
        effectSingleLiveEvent.postValue(RecordViewEffect.StartCameraActivityViewEffect(file, duration.toInt()))
      }
    }
  }

  private fun handleSetFileNameIntent(intent: RecordIntent.SetFileNameIntent) {
    fileName = intent.fileName
    stateLiveData.postValue(currentState.consumeAction(RecordAction.SetInputValidityAction(isStartCameraButtonEnabled)))
  }

  private fun handleSetDurationIntent(intent: RecordIntent.SetDurationIntent) {
    sliderInfo.value = intent.duration
    stateLiveData.postValue(currentState.consumeAction(RecordAction.SetDurationAction(duration)))
  }

}