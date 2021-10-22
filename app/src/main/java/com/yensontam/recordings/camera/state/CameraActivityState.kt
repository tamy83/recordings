package com.yensontam.recordings.camera.state

import com.yensontam.recordings.state.IAction
import com.yensontam.recordings.state.IState

data class CameraActivityState(
  var secondsRemaining: Int,
  var recordingState: RECORDING_STATE = RECORDING_STATE.STOPPED): IState {

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
          CameraActivityViewEffect.Finish -> {
            newState.recordingState = RECORDING_STATE.STOPPED
          }
          CameraActivityViewEffect.Stop -> {
            newState.recordingState = RECORDING_STATE.STOPPED
          }
          CameraActivityViewEffect.Prepare -> {

          }
          is CameraActivityViewEffect.Record -> {
            newState.recordingState = RECORDING_STATE.RECORDING
          }
        }
      }
    }
    return newState
  }
}

enum class RECORDING_STATE {
  RECORDING,
  STOPPED
}