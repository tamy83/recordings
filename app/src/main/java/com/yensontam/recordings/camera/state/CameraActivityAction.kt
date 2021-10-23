package com.yensontam.recordings.camera.state

import com.yensontam.recordings.mvi.IAction

sealed class CameraActivityAction: IAction {
  data class SetFilePathAction(val filePath: String) : CameraActivityAction()
  data class HasTimeRemainingAction(val secondsRemaining: Int) : CameraActivityAction()
  data class OneTimeAction(val viewEffect: CameraActivityViewEffect) : CameraActivityAction()
}