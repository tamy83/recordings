package com.yensontam.recordings.camera

import android.os.CountDownTimer

class CameraInteractorImpl() : CameraInteractor {

  override lateinit var listener: CameraInteractor.CameraInteractorListener

  private lateinit var countDownTimer: CountDownTimer

  override suspend fun startTimer(durationMs: Long, timerIntervalMs: Long) {
    countDownTimer = object : CountDownTimer(durationMs, timerIntervalMs) {
      override fun onTick(timeRemainingMs: Long) {
        listener.onTimerTick(timeRemainingMs)
      }

      override fun onFinish() {
        listener.onTimerFinish()
      }
    }
    countDownTimer.start()
  }
}