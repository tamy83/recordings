package com.yensontam.recordings.media

import java.io.File

interface MediaMetadataRetrieverHelper {
  fun getMetadata(file: File): MediaMetadata?
}