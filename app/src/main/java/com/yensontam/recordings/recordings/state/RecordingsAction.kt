package com.yensontam.recordings.recordings.state

import com.yensontam.recordings.mvi.IAction
import com.yensontam.recordings.recordings.model.RecordingViewItem

sealed class RecordingsAction: IAction {
  data class HasRecordingsAction(val recordingViewItems: List<RecordingViewItem>) : RecordingsAction()
}