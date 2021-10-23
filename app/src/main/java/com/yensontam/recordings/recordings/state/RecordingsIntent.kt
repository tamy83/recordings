package com.yensontam.recordings.recordings.state

import com.yensontam.recordings.mvi.IIntent

sealed class RecordingsIntent : IIntent {
  object LoadedIntent: RecordingsIntent()
}