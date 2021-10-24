package com.yensontam.recordings.recordings.state

import com.yensontam.recordings.mvi.IAction
import com.yensontam.recordings.mvi.IState
import com.yensontam.recordings.recordings.model.RecordingViewItem

data class RecordingsState(
  var recordingViewItems: List<RecordingViewItem> = listOf()) : IState {

  override fun consumeAction(action: IAction): RecordingsState {
    val newState = this.copy()

    when (action) {
      is RecordingsAction.HasRecordingsAction -> {
        newState.recordingViewItems = action.recordingViewItems
      }
    }
    return newState
  }
}