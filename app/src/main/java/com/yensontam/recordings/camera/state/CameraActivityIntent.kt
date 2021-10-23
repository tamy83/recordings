package com.yensontam.recordings.camera.state

import android.content.Intent
import com.yensontam.recordings.mvi.IIntent

sealed class CameraActivityIntent: IIntent {
  data class LoadedIntent(val intent: Intent): CameraActivityIntent()
  object CameraIdleIntent: CameraActivityIntent()
  object CameraStreamingIntent: CameraActivityIntent()
  object CameraStopIntent: CameraActivityIntent()
  object PauseIntent: CameraActivityIntent()
  object ResumeIntent: CameraActivityIntent()
  object ErrorIntent: CameraActivityIntent()
  object FileSavedIntent: CameraActivityIntent()
}