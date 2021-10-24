package com.yensontam.recordings.recordings.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yensontam.recordings.databinding.FragmentRecordingsBinding
import com.yensontam.recordings.recordings.state.RecordingsIntent
import com.yensontam.recordings.recordings.state.RecordingsState
import com.yensontam.recordings.recordings.viewmodel.RecordingsViewModel
import org.koin.android.ext.android.inject

class RecordingsFragment: Fragment() {

  private var _binding: FragmentRecordingsBinding? = null
  private val viewModel: RecordingsViewModel by inject()

  private val binding get() = _binding!!
  private lateinit var recordingsRecyclerView: RecyclerView
  private lateinit var recordingsListAdapter: RecordingsListAdapter

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    _binding = FragmentRecordingsBinding.inflate(inflater, container, false)
    val root = binding.root
    recordingsRecyclerView = binding.recordingsRecyclerView

    return root
  }

  override fun onActivityCreated(savedInstanceState: Bundle?) {
    super.onActivityCreated(savedInstanceState)
    viewModel.stateLiveData.observe(viewLifecycleOwner, {
      renderState(it)
    })

    recordingsRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
    recordingsListAdapter = RecordingsListAdapter(viewModel)
    recordingsRecyclerView.adapter = recordingsListAdapter
  }

  override fun onResume() {
    super.onResume()
    viewModel.onIntentReceived(RecordingsIntent.LoadedIntent)
  }

  private fun renderState(state: RecordingsState) {
    recordingsListAdapter.setData(state.recordingViewItems)
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}