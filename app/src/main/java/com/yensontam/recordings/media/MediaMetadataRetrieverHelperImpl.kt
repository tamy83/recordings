package com.yensontam.recordings.media

import android.media.MediaMetadataRetriever
import java.io.File
import java.lang.Exception

class MediaMetadataRetrieverHelperImpl: MediaMetadataRetrieverHelper {

  private val retriever = MediaMetadataRetriever()

  override fun getMetadata(file: File): MediaMetadata? {
    val durationLong = try {
      retriever.setDataSource(file.absolutePath)
      val durationString = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION) ?: return null
      durationString.toLong()
    } catch (e: Exception) {
      -1L
    }
    return MediaMetadata(fileName = file.name, durationMs = durationLong, timestamp = file.lastModified())
  }

  protected fun finalize() {
    retriever.release()
  }

}