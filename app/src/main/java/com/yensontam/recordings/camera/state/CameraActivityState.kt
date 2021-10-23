package com.yensontam.recordings.camera.state

import com.yensontam.recordings.mvi.IAction
import com.yensontam.recordings.mvi.IState

data class CameraActivityState(
  var secondsRemaining: Int): IState {

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
    }
    return newState
  }
}
