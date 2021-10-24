package com.yensontam.recordings

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.yensontam.recordings.helper.CoroutinesTestRule
import com.yensontam.recordings.mvi.Data
import com.yensontam.recordings.recordings.RecordingsInteractor
import com.yensontam.recordings.recordings.model.RecordingViewItem
import com.yensontam.recordings.recordings.state.RecordingsFragmentIntent
import com.yensontam.recordings.recordings.state.RecordingsFragmentState
import com.yensontam.recordings.recordings.viewmodel.RecordingsViewModel
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestRule

class RecordingsViewModelUnitTest {

  @get:Rule
  var rule: TestRule = InstantTaskExecutorRule()

  @get:Rule
  var coroutinesTestRule = CoroutinesTestRule()

  @MockK
  lateinit var interactor: RecordingsInteractor

  @MockK
  lateinit var config: Config

  @MockK
  lateinit var application: Application

  lateinit var viewModel: RecordingsViewModel

  private val currentState: RecordingsFragmentState
    get() {
      return viewModel.stateLiveData.value!!
    }

  @Before
  fun setup() {
    MockKAnnotations.init(this)
    viewModel = RecordingsViewModel(application, config, interactor)

    every { config.recordingsDirectory } returns ""
  }

  @Test
  fun `test loaded intent`() {
    var recordingsList = createRecordingViewItems(0)
    coEvery { interactor.getRecordingViewItems(any()) } returns Data.Success(recordingsList)
    viewModel.onIntentReceived(RecordingsFragmentIntent.LoadedIntent)
    assertEquals(0, currentState.recordingViewItems.size)

    recordingsList = createRecordingViewItems(5)
    coEvery { interactor.getRecordingViewItems(any()) } returns Data.Success(recordingsList)
    viewModel.onIntentReceived(RecordingsFragmentIntent.LoadedIntent)
    assertEquals(5, currentState.recordingViewItems.size)

    recordingsList = createRecordingViewItems(8)
    coEvery { interactor.getRecordingViewItems(any()) } returns Data.Success(recordingsList)
    viewModel.onIntentReceived(RecordingsFragmentIntent.LoadedIntent)
    assertEquals(8, currentState.recordingViewItems.size)
  }

  private fun createRecordingViewItems(n: Int): List<RecordingViewItem> {
    val list = mutableListOf<RecordingViewItem>()
    repeat(n) {
      val recordingViewItem = RecordingViewItem(n.toString(), n.toString(), n.toString())
      list.add(recordingViewItem)
    }
    return list
  }
}