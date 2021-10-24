package com.yensontam.recordings.record.state

import com.yensontam.recordings.mvi.IAction
import com.yensontam.recordings.mvi.IState
import com.yensontam.recordings.record.model.SliderInfo

data class RecordState(
  var fileName: String?,
  var isStartCameraButtonEnabled: Boolean,
  var sliderInfo: SliderInfo
): IState {
  override fun consumeAction(action: IAction): RecordState {
    val newState = this.copy()

    when (action) {
      is RecordAction.SetInputValidityAction -> {
        newState.isStartCameraButtonEnabled = action.startCameraButtonEnabled
      }
      is RecordAction.SetDurationAction -> {
        newState.sliderInfo.value = action.duration
      }
    }

    return newState
  }
}
