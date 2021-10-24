package com.yensontam.recordings.recordings.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yensontam.recordings.Config
import com.yensontam.recordings.mvi.Data
import com.yensontam.recordings.recordings.RecordingsInteractor
import com.yensontam.recordings.recordings.state.RecordingsFragmentAction
import com.yensontam.recordings.recordings.state.RecordingsFragmentIntent
import com.yensontam.recordings.recordings.state.RecordingsFragmentState
import kotlinx.coroutines.launch

class RecordingsViewModel(application: Application,
                          private val config: Config,
                          private val interactor: RecordingsInteractor) : AndroidViewModel(application) {

  val stateLiveData = MutableLiveData(RecordingsFragmentState())

  private val currentState: RecordingsFragmentState
    get() {
      return stateLiveData.value ?: RecordingsFragmentState()
    }

  fun onIntentReceived(intent: RecordingsFragmentIntent) {
    when(intent) {
      is RecordingsFragmentIntent.LoadedIntent -> {
        handleLoadedIntent(intent)
      }
    }
  }

  private fun handleLoadedIntent(intent: RecordingsFragmentIntent.LoadedIntent) {
    viewModelScope.launch {
      val directory = config.recordingsDirectory
      val result = interactor.getRecordingViewItems(directory)
      when (result) {
        is Data.Success -> {
          val recordingViewItems = result.value
          stateLiveData.postValue(currentState.consumeAction(RecordingsFragmentAction.HasRecordingsFragmentAction(recordingViewItems)))
        }
        is Data.Failure -> {
          // handle error
        }
      }
    }
  }
}