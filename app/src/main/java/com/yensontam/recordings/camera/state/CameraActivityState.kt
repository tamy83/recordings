package com.yensontam.recordings.camera.state

import com.yensontam.recordings.mvi.IAction
import com.yensontam.recordings.mvi.IState

data class CameraActivityState(
  var secondsRemaining: Int,
  var orientation: Orientation = Orientation.PORTRAIT): IState {

  override fun consumeAction(action: IAction): CameraActivityState {
    val newState = this.copy()
    when (action) {
      is CameraActivityAction.SetFilePathAction -> {
      }
      is CameraActivityAction.HasTimeRemainingAction -> {
        newState.secondsRemaining = action.secondsRemaining
      }
      is CameraActivityAction.OneTimeAction -> {
        when (action.viewEffect) {
          CameraActivityViewEffect.Finish -> { }
          CameraActivityViewEffect.Stop -> { }
          CameraActivityViewEffect.Prepare -> { }
          is CameraActivityViewEffect.Record -> { }
        }
      }
      is CameraActivityAction.OrientationChangeAction -> {
        newState.orientation = action.orientation
      }
    }
    return newState
  }
}

enum class Orientation(val degreeRotation: Int) {
  PORTRAIT(0),
  LANDSCAPE(90),
  REVERSE_PORTRAIT(180),
  REVERSE_LANDSCAPE(270)
}
