package com.yensontam.recordings.record.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.material.slider.Slider
import com.google.android.material.textfield.TextInputEditText
import com.yensontam.recordings.camera.view.CameraActivity
import com.yensontam.recordings.databinding.FragmentRecordBinding
import com.yensontam.recordings.record.state.RecordIntent
import com.yensontam.recordings.record.state.RecordState
import com.yensontam.recordings.record.state.RecordViewEffect
import com.yensontam.recordings.record.viewmodel.RecordViewModel
import org.koin.android.ext.android.inject

class RecordFragment: Fragment() {

  private var _binding: FragmentRecordBinding? = null
  private val viewModel: RecordViewModel by inject()

  private val binding get() = _binding!!
  private lateinit var startCameraButton: Button
  private lateinit var fileNameEditText: TextInputEditText
  private lateinit var durationSlider: Slider
  private lateinit var textWatcher: TextWatcher

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    textWatcher = object: TextWatcher {
      override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

      override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        viewModel.onIntentReceived(RecordIntent.SetFileNameIntent(p0.toString()))
      }

      override fun afterTextChanged(p0: Editable?) { }
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    _binding = FragmentRecordBinding.inflate(inflater, container, false)
    val root = binding.root
    startCameraButton = binding.startCameraButton
    fileNameEditText = binding.nameInput
    durationSlider = binding.durationSlider

    return root
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)

    viewModel.stateLiveData.observe(viewLifecycleOwner, {
      renderState(it)
    })

    viewModel.effectSingleLiveEvent.observe(viewLifecycleOwner, {
      showViewEffect(it)
    })

    startCameraButton.setOnClickListener {
      viewModel.onIntentReceived(RecordIntent.StartCameraIntent)
    }

    fileNameEditText.addTextChangedListener(textWatcher)

    durationSlider.addOnChangeListener(
      Slider.OnChangeListener { slider, value, fromUser ->
        if (fromUser) {
          viewModel.onIntentReceived(RecordIntent.SetDurationIntent(value))
        }
      }
    )
    viewModel.onIntentReceived(RecordIntent.LoadedIntent)
  }

  private fun renderState(state: RecordState) {
    durationSlider.value = state.sliderInfo.value
    durationSlider.valueFrom = state.sliderInfo.valueFrom
    durationSlider.valueTo = state.sliderInfo.valueTo
    durationSlider.stepSize = state.sliderInfo.stepSize
    startCameraButton.isEnabled = state.isStartCameraButtonEnabled
  }

  private fun showViewEffect(viewEffect: RecordViewEffect) {
    when (viewEffect) {
      is RecordViewEffect.RequestPermissionsViewEffect -> {
        permissionLauncher.launch(viewEffect.requiredPermissions)
      }
      is RecordViewEffect.StartCameraActivityViewEffect -> {
        startCameraActivity(viewEffect.fileName, viewEffect.duration)

      }
    }
  }

  val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
    viewModel.onIntentReceived(RecordIntent.StartCameraIntent)
  }

  private fun startCameraActivity(fileName: String, duration: Int) {
    activity?.let {
      it.startActivity(CameraActivity.getStartIntent(it, fileName = fileName, duration = duration))
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}