package com.yensontam.recordings.record

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RecordViewModel : ViewModel() {

  val fileNameLiveData = MutableLiveData<String?>()
  val durationLiveData = MutableLiveData(3)
  val recordButtonEnabledLiveData = MutableLiveData(false)

  fun setFileName(fileName: String?) {
    fileNameLiveData.value = fileName
    recordButtonEnabledLiveData.value = fileName?.isNotEmpty() ?: false
  }

  fun setDuration(duration: Int) {
    durationLiveData.value = duration
  }

}