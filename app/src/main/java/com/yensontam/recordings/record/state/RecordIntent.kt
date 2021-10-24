package com.yensontam.recordings.record.state

import com.yensontam.recordings.mvi.IIntent

sealed class RecordIntent: IIntent {
  object LoadedIntent : RecordIntent()
  data class SetDurationIntent(val duration: Float) : RecordIntent()
  data class SetFileNameIntent(val fileName: String) : RecordIntent()
  object StartCameraIntent : RecordIntent()
}