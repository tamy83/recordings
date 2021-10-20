package com.yensontam.recordings.main.state

sealed class MainActivityIntent {
  object LoadedIntent : MainActivityIntent()
  data class SelectedTabIntent(val position: Int) : MainActivityIntent()
}