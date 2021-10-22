package com.yensontam.recordings.camera.state

sealed class CameraActivityViewEffect {
  object Finish: CameraActivityViewEffect()
  object Stop: CameraActivityViewEffect()
  data class Record(val filePath: String): CameraActivityViewEffect()
  object Prepare: CameraActivityViewEffect()
}