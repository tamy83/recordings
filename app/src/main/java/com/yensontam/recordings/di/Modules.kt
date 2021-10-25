package com.yensontam.recordings.di

import com.yensontam.recordings.Config
import com.yensontam.recordings.camera.CameraInteractorImpl
import com.yensontam.recordings.camera.viewmodel.CameraViewModel
import com.yensontam.recordings.helper.LetterOrDigitFileNameValidator
import com.yensontam.recordings.media.MediaMetadataRetrieverHelperImpl
import com.yensontam.recordings.record.RecordInteractorImpl
import com.yensontam.recordings.record.viewmodel.RecordViewModel
import com.yensontam.recordings.recordings.RecordingsInteractorImpl
import com.yensontam.recordings.recordings.viewmodel.RecordingsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val cameraViewModelModule = module {
  viewModel {
    CameraViewModel(
      androidApplication(),
      config = Config.instance,
      interactor = CameraInteractorImpl()
    )
  }
}

val recordViewModelModule = module {
  viewModel {
    RecordViewModel(
      androidApplication(),
      fileNameValidator = LetterOrDigitFileNameValidator(),
      interactor = RecordInteractorImpl()
    )
  }
}

val recordingsViewModelModule = module {
  viewModel {
    RecordingsViewModel(
      androidApplication(),
      config = Config.instance,
      interactor = RecordingsInteractorImpl(MediaMetadataRetrieverHelperImpl())
    )
  }
}