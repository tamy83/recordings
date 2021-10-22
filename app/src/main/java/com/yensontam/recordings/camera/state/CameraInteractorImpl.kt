package com.yensontam.recordings.camera.state

import android.app.Application
import android.os.CountDownTimer

class CameraInteractorImpl(private val application: Application,
                           override var timeRemainingListener: CameraInteractor.TimeRemainingListener,
                           private var duration: Int = 0): CameraInteractor {

  private lateinit var countDownTimer: CountDownTimer

  override suspend fun startTimer() {
    val durationInMs = duration * 1000
    countDownTimer = object : CountDownTimer(durationInMs.toLong(), 1000) {
      override fun onTick(timeRemainingMs: Long) {
        timeRemainingListener.onTimeLeft((timeRemainingMs/1000).toInt())
      }

      override fun onFinish() {

      }
    }
  }

  override fun getDirectory(): String? {
    return application.getExternalFilesDir("recordings")?.path
  }
}