package com.yensontam.recordings.record

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.slider.Slider
import com.google.android.material.textfield.TextInputEditText
import com.yensontam.recordings.camera.view.CameraActivity
import com.yensontam.recordings.databinding.FragmentRecordBinding

class RecordFragment: Fragment() {

  private var _binding: FragmentRecordBinding? = null
  private lateinit var viewModel: RecordViewModel

  private val binding get() = _binding!!
  private lateinit var recordButton: Button
  private lateinit var fileNameEditText: TextInputEditText
  private lateinit var durationSlider: Slider

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    textWatcher = object: TextWatcher {
      override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { }

      override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        viewModel.setFileName(p0.toString())
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
    recordButton = binding.recordButton
    fileNameEditText = binding.nameInput
    durationSlider = binding.durationSlider

    return root
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    val recordViewModel: RecordViewModel by viewModels()
    viewModel = recordViewModel

    viewModel.recordButtonEnabledLiveData.observe(viewLifecycleOwner, {
      recordButton.isEnabled = it
    })

    recordButton.setOnClickListener {
      val fileName = viewModel.fileNameLiveData.value
      val duration = viewModel.durationLiveData.value
      if (fileName != null && duration != null) {
        requestPermissionOrStartCameraActivityIfApplicable()
      }
    }

    fileNameEditText.addTextChangedListener(textWatcher)

    durationSlider.addOnChangeListener(
      Slider.OnChangeListener { slider, value, fromUser ->
        viewModel.setDuration(value.toInt())
      }
    )
  }

  private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
    val act = activity
    if (act == null) {
      false
    } else {
      ContextCompat.checkSelfPermission(act, it) == PackageManager.PERMISSION_GRANTED
    }
  }

  val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
    if (allPermissionsGranted()) {
      startCameraActivity()
    }
  }

  private fun requestPermissionOrStartCameraActivityIfApplicable() {
    if (allPermissionsGranted()) {
      startCameraActivity()
    } else {
      permissionLauncher.launch(REQUIRED_PERMISSIONS)
    }
  }

  private fun startCameraActivity() {
    activity?.let {
      val fileName = viewModel.fileNameLiveData.value
      val duration = viewModel.durationLiveData.value
      if (fileName != null && duration != null) {
        it.startActivity(CameraActivity.getStartIntent(it, fileName = fileName, duration = duration))
      }
    }
  }

  companion object {
    private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
  }

  private lateinit var textWatcher: TextWatcher

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}