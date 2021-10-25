package com.yensontam.recordings.record.state

sealed class RecordViewEffect {
  data class StartCameraActivityViewEffect(val fileName: String, val duration: Int): RecordViewEffect()
  data class RequestPermissionsViewEffect(val requiredPermissions: Array<String>): RecordViewEffect()
  data class ShowErrorMessageViewEffect(val message: String): RecordViewEffect()
}