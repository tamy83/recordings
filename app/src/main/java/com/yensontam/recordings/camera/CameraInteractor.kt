package com.yensontam.recordings.camera

interface CameraInteractor {

  var listener: CameraInteractorListener
  suspend fun startTimer(durationMs: Long, timerIntervalMs: Long)

  interface CameraInteractorListener {
    fun onTimerTick(timeRemainingMs: Long)
    fun onTimerFinish()
  }
}