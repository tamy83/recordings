package com.yensontam.recordings

import android.app.Application
import android.content.Intent
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.yensontam.recordings.camera.CameraInteractor
import com.yensontam.recordings.camera.CameraInteractorImpl
import com.yensontam.recordings.camera.state.CameraActivityIntent
import com.yensontam.recordings.camera.state.CameraActivityState
import com.yensontam.recordings.camera.state.CameraActivityViewEffect
import com.yensontam.recordings.camera.view.CameraActivity
import com.yensontam.recordings.camera.viewmodel.CameraViewModel
import com.yensontam.recordings.helper.CoroutinesTestRule
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import java.io.File

class CameraViewModelTest {

  @get:Rule
  var rule: TestRule = InstantTaskExecutorRule()

  @get:Rule
  var coroutinesTestRule = CoroutinesTestRule()

  @MockK
  lateinit var config: Config

  @MockK
  lateinit var application: Application

  @SpyK
  private var interactor: CameraInteractor = CameraInteractorImpl()

  lateinit var viewModel: CameraViewModel

  private val currentState: CameraActivityState
    get() {
      return viewModel.stateLiveData.value!!
    }

  private val viewEffect: CameraActivityViewEffect
    get() {
      return viewModel.effectSingleLiveEvent.value!!
    }

  private val directory = "directory"

  @Before
  fun setup() {
    MockKAnnotations.init(this)

    every { config.recordingsDirectory } returns directory
    coEvery { interactor.startTimer(any(), any()) } returns Unit

    viewModel = CameraViewModel(application, config, interactor)
  }

  @Test
  fun `test loaded intent`() {
    var intent = getIntent("file", 5)
    viewModel.onIntentReceived(CameraActivityIntent.LoadedIntent(intent))
    assertEquals(5, currentState.secondsRemaining)
    assertTrue(viewEffect is CameraActivityViewEffect.Prepare)

    intent = getIntent(null, 5)
    viewModel.onIntentReceived(CameraActivityIntent.LoadedIntent(intent))
    assertTrue(viewEffect is CameraActivityViewEffect.Finish)

    intent = getIntent("file", 0)
    viewModel.onIntentReceived(CameraActivityIntent.LoadedIntent(intent))
    assertEquals(5, currentState.secondsRemaining)
    assertTrue(viewEffect is CameraActivityViewEffect.Finish)
  }

  @Test
  fun `test camera streaming intent`() {
    val fileName = "filename"
    val intent = getIntent(fileName, 5)
    viewModel.onIntentReceived(CameraActivityIntent.LoadedIntent(intent))

    viewModel.onIntentReceived(CameraActivityIntent.CameraStreamingIntent)
    assertTrue(viewEffect is CameraActivityViewEffect.Record)
    assertEquals(directory + File.separator + fileName,
      (viewEffect as CameraActivityViewEffect.Record).filePath)
    coVerify { interactor.startTimer(withArg {
      assertEquals(5000, it)
    }, withArg {
      assertEquals(1000, it)
    })}
  }

  @Test
  fun `test camera error intent`() {
    viewModel.onIntentReceived(CameraActivityIntent.ErrorIntent)
    assertTrue(viewEffect is CameraActivityViewEffect.Stop)
  }

  @Test
  fun `test camera stop intent`() {
    viewModel.onIntentReceived(CameraActivityIntent.CameraStopIntent)
    assertTrue(viewEffect is CameraActivityViewEffect.Finish)
  }

  @Test
  fun `test file saved intent`() {
    viewModel.onIntentReceived(CameraActivityIntent.FileSavedIntent)
    assertTrue(viewEffect is CameraActivityViewEffect.Finish)
  }

  @Test
  fun `test camera state on interactor timer tick`() {
    interactor.listener.onTimerTick(4000)
    assertEquals(4, currentState.secondsRemaining)
  }

  @Test
  fun `test camera vieweffect on interactor timer finish`() {
    interactor.listener.onTimerFinish()
    assertTrue(viewEffect is CameraActivityViewEffect.Stop)
  }

  private fun getIntent(fileName: String?, duration: Int) : Intent {
    val intent = mockkClass(Intent::class)
    every { intent.getStringExtra(CameraActivity.EXTRA_FILE_NAME) } returns fileName
    every { intent.getIntExtra(CameraActivity.EXTRA_DURATION_SECONDS, any()) } returns duration
    return intent
  }

}