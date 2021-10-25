package com.yensontam.recordings.camera.state

import android.content.Intent
import com.yensontam.recordings.mvi.IIntent

sealed class CameraActivityIntent: IIntent {
  data class LoadedIntent(val intent: Intent): CameraActivityIntent()
  object CameraStreamingIntent: CameraActivityIntent()
  object CameraStopIntent: CameraActivityIntent()
  object ErrorIntent: CameraActivityIntent()
  object FileSavedIntent: CameraActivityIntent()
  data class OrientationChangedIntent(val orientation: Int): CameraActivityIntent()
}