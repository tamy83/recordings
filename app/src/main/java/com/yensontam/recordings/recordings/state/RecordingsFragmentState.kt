package com.yensontam.recordings.recordings.state

import com.yensontam.recordings.mvi.IAction
import com.yensontam.recordings.mvi.IState
import com.yensontam.recordings.recordings.model.RecordingViewItem

data class RecordingsFragmentState(
  var recordingViewItems: List<RecordingViewItem> = listOf()) : IState {

  override fun consumeAction(action: IAction): RecordingsFragmentState {
    val newState = this.copy()

    when (action) {
      is RecordingsFragmentAction.HasRecordingsFragmentAction -> {
        newState.recordingViewItems = action.recordingViewItems
      }
    }
    return newState
  }
}