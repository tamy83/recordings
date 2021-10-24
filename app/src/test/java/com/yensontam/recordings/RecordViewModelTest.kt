package com.yensontam.recordings

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.yensontam.recordings.helper.CoroutinesTestRule
import com.yensontam.recordings.helper.FileNameValidator
import com.yensontam.recordings.mvi.Data
import com.yensontam.recordings.record.RecordInteractor
import com.yensontam.recordings.record.model.SliderInfo
import com.yensontam.recordings.record.state.RecordIntent
import com.yensontam.recordings.record.state.RecordState
import com.yensontam.recordings.record.state.RecordViewEffect
import com.yensontam.recordings.record.viewmodel.RecordViewModel
import com.yensontam.recordings.recordings.model.RecordingViewItem
import com.yensontam.recordings.recordings.state.RecordingsIntent
import com.yensontam.recordings.recordings.viewmodel.RecordingsViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

class RecordViewModelTest {

  @get:Rule
  var rule: TestRule = InstantTaskExecutorRule()

  @get:Rule
  var coroutinesTestRule = CoroutinesTestRule()

  @MockK
  lateinit var application: Application

  lateinit var viewModel: RecordViewModel

  @MockK
  lateinit var fileNameValidator: FileNameValidator

  @MockK
  lateinit var interactor: RecordInteractor

  private val currentState: RecordState
    get() {
      return viewModel.stateLiveData.value!!
    }

  private val currentViewEffect: RecordViewEffect
    get() {
      return viewModel.effectSingleLiveEvent.value!!
    }

  @Before
  fun setup() {
    MockKAnnotations.init(this)
    every { fileNameValidator.isValidFileName(any()) } returns false
    viewModel = RecordViewModel(application, fileNameValidator, interactor)
  }

  @Test
  fun `test loaded intent`() {
    viewModel.onIntentReceived(RecordIntent.LoadedIntent)
    assertNull(currentState.fileName)
    assertFalse(currentState.isStartCameraButtonEnabled)
    assertEquals(viewModel.sliderInfo, currentState.sliderInfo)
    assertNull(currentState.fileName)
  }

  @Test
  fun `test start camera intent`() {
    `test set filename intent`()
    every { interactor.hasAllPermissions(any(), any()) } returns false
    viewModel.onIntentReceived(RecordIntent.StartCameraIntent)
    assertTrue(currentViewEffect is RecordViewEffect.RequestPermissionsViewEffect)

    every { interactor.hasAllPermissions(any(), any()) } returns true
    viewModel.onIntentReceived(RecordIntent.StartCameraIntent)
    assertTrue(currentViewEffect is RecordViewEffect.StartCameraActivityViewEffect)

  }

  @Test
  fun `test set filename intent`() {
    val fileName = "name"
    every { fileNameValidator.isValidFileName(any()) } returns false
    viewModel.onIntentReceived(RecordIntent.SetFileNameIntent(fileName))
    assertFalse(currentState.isStartCameraButtonEnabled)

    every { fileNameValidator.isValidFileName(any()) } returns true
    viewModel.onIntentReceived(RecordIntent.SetFileNameIntent(fileName))
    assertTrue(currentState.isStartCameraButtonEnabled)
  }

  @Test
  fun `test set duration intent`() {
    var duration = 3F
    viewModel.onIntentReceived(RecordIntent.SetDurationIntent(duration))
    assertEquals(duration, currentState.sliderInfo.value)

    duration = 5F
    viewModel.onIntentReceived(RecordIntent.SetDurationIntent(duration))
    assertEquals(duration, currentState.sliderInfo.value)

    duration = 180F
    viewModel.onIntentReceived(RecordIntent.SetDurationIntent(duration))
    assertEquals(duration, currentState.sliderInfo.value)

    duration = 70F
    viewModel.onIntentReceived(RecordIntent.SetDurationIntent(duration))
    assertEquals(duration, currentState.sliderInfo.value)
  }
}