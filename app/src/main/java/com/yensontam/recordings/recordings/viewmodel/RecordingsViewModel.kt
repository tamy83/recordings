package com.yensontam.recordings.recordings.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yensontam.recordings.main.state.*
import com.yensontam.recordings.media.MediaMetadataRetrieverHelperImpl
import com.yensontam.recordings.mvi.Data
import com.yensontam.recordings.recordings.RecordingsInteractorImpl
import com.yensontam.recordings.recordings.state.RecordingsAction
import com.yensontam.recordings.recordings.state.RecordingsIntent
import com.yensontam.recordings.recordings.state.RecordingsState
import kotlinx.coroutines.launch

class RecordingsViewModel(application: Application) : AndroidViewModel(application) {

  val stateLiveData = MutableLiveData(RecordingsState())

  private val currentState: RecordingsState
    get() {
      return stateLiveData.value ?: RecordingsState()
    }

  private val interactor = RecordingsInteractorImpl(MediaMetadataRetrieverHelperImpl())

  fun onIntentReceived(intent: RecordingsIntent) {
    when(intent) {
      is RecordingsIntent.LoadedIntent -> {
        handleLoadedIntent(intent)
      }
    }
  }

  private fun handleLoadedIntent(intent: RecordingsIntent.LoadedIntent) {
    viewModelScope.launch {
      val directory = getApplication<Application>().getExternalFilesDir("recordings")?.path
      if (directory == null) {
        return@launch
      }
      val result = interactor.getRecordingViewItems(directory)
      when (result) {
        is Data.Success -> {
          val recordingViewItems = result.value
          stateLiveData.postValue(currentState.consumeAction(RecordingsAction.HasRecordingsAction(recordingViewItems)))
        }
        is Data.Failure -> {
          // handle error
        }
      }
    }
  }
}