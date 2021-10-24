package com.yensontam.recordings.record.state

import com.yensontam.recordings.mvi.IAction

sealed class RecordAction: IAction {
  data class SetInputValidityAction(val startCameraButtonEnabled: Boolean) : RecordAction()
  data class SetDurationAction(val duration: Float): RecordAction()
  data class OneTimeAction(val viewEffect: RecordViewEffect) : RecordAction()
}