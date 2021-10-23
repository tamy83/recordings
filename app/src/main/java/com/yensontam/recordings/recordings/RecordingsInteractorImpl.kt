package com.yensontam.recordings.recordings

import com.yensontam.recordings.media.MediaMetadata
import com.yensontam.recordings.media.MediaMetadataRetrieverHelper
import com.yensontam.recordings.mvi.Data
import com.yensontam.recordings.recordings.model.RecordingViewItem
import java.io.File
import java.lang.IllegalArgumentException
import java.util.*

class RecordingsInteractorImpl(private var mediaMetadataRetrieverHelper: MediaMetadataRetrieverHelper): RecordingsInteractor {

  override suspend fun getRecordingViewItems(directory: String): Data<List<RecordingViewItem>> {
    val file = File(directory)
    if (!file.isDirectory) {
      return Data.Failure(IllegalArgumentException("Not a directory"))
    }
    val recordingViewItems = mutableListOf<RecordingViewItem>()

    file.walk().forEach {
      if (it.isFile) {
        val mediaMetadata = mediaMetadataRetrieverHelper.getMetadata(it)
        mediaMetadata?.let {
          recordingViewItems.add(mediaMetadataToRecordingViewItem(mediaMetadata))
        }
      }
    }
    return Data.Success(recordingViewItems)
  }

  private fun mediaMetadataToRecordingViewItem(mediaMetadata: MediaMetadata): RecordingViewItem {
    val durationSeconds = mediaMetadata.durationMs / 1000
    val timestamp = Date(mediaMetadata.timestamp).toString()

    return RecordingViewItem(name = mediaMetadata.fileName, duration = durationSeconds.toString(), timeStampString = timestamp)
  }

}