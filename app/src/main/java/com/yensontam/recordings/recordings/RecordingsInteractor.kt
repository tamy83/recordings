package com.yensontam.recordings.recordings

import com.yensontam.recordings.mvi.Data
import com.yensontam.recordings.recordings.model.RecordingViewItem

interface RecordingsInteractor {
  suspend fun getRecordingViewItems(directory: String): Data<List<RecordingViewItem>>
}