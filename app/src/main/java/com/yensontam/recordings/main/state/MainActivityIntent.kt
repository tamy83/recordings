package com.yensontam.recordings.main.state

import com.yensontam.recordings.mvi.IIntent

sealed class MainActivityIntent: IIntent{
  object LoadedIntent : MainActivityIntent()
  data class SelectedTabIntent(val position: Int) : MainActivityIntent()
}