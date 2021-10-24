package com.yensontam.recordings

import android.app.Application
import com.yensontam.recordings.di.cameraViewModelModule
import com.yensontam.recordings.di.recordViewModelModule
import com.yensontam.recordings.di.recordingsViewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class RecordingsApp : Application() {

  override fun onCreate() {
    super.onCreate()

    val directory = getExternalFilesDir("recordings")?.path
    if (directory == null) {
      // handle error
    } else {
      Config.instance.recordingsDirectory = directory
    }

    setUpKoin()
  }

  private fun setUpKoin() {
    startKoin {
      androidContext(this@RecordingsApp)
      modules(
        listOf(
          cameraViewModelModule,
          recordViewModelModule,
          recordingsViewModelModule
        )
      )
    }
  }
}