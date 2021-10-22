package com.yensontam.recordings.camera.state

interface CameraInteractor {
  var timeRemainingListener : TimeRemainingListener

  suspend fun startTimer()

  interface TimeRemainingListener {
    fun onTimeLeft(timeLeft: Int)
  }

  fun getDirectory(): String?
}