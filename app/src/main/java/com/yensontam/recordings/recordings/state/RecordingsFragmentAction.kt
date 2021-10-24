package com.yensontam.recordings.recordings.state

import com.yensontam.recordings.mvi.IAction
import com.yensontam.recordings.recordings.model.RecordingViewItem

sealed class RecordingsFragmentAction: IAction {
  data class HasRecordingsFragmentAction(val recordingViewItems: List<RecordingViewItem>) : RecordingsFragmentAction()
}