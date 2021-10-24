package com.yensontam.recordings.record

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

class RecordInteractorImpl : RecordInteractor {
  override fun hasAllPermissions(permissions: Array<String>, applicationContext: Context): Boolean {
    return permissions.all {
      ContextCompat.checkSelfPermission(applicationContext, it) == PackageManager.PERMISSION_GRANTED
    }
  }
}