package com.yensontam.recordings.recordings.state

import com.yensontam.recordings.mvi.IIntent

sealed class RecordingsFragmentIntent : IIntent {
  object LoadedIntent: RecordingsFragmentIntent()
}