package com.yensontam.recordings.media

import android.media.MediaMetadataRetriever
import java.io.File
import java.lang.Exception

class MediaMetadataRetrieverHelperImpl: MediaMetadataRetrieverHelper {

  private val retriever = MediaMetadataRetriever()

  override fun getMetadata(file: File): MediaMetadata? {
    retriever.setDataSource(file.absolutePath)
    val durationString = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION) ?: return null
    return try {
      val durationLong = durationString.toLong()
      MediaMetadata(fileName = file.name, durationMs = durationLong, timestamp = file.lastModified())
    } catch (e: Exception) {
      null
    }
  }

  protected fun finalize() {
    retriever.release()
  }

}