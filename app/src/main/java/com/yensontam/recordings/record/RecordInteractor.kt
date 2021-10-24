package com.yensontam.recordings.record

import android.content.Context

interface RecordInteractor {
  fun hasAllPermissions(permissions: Array<String>, applicationContext: Context) : Boolean
}