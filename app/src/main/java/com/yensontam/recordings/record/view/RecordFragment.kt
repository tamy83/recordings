package com.yensontam.recordings.record.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.yensontam.recordings.databinding.FragmentRecordBinding
import com.yensontam.recordings.ui.main.PageViewModel
import com.yensontam.recordings.ui.main.PlaceholderFragment

class RecordFragment: Fragment() {

  private lateinit var recordViewModel: RecordViewModel
  private var _binding: FragmentRecordBinding? = null

  private val binding get() = _binding!!

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

  }

  override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {

    _binding = FragmentRecordBinding.inflate(inflater, container, false)
    val root = binding.root

    return root
  }

  companion object {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private const val ARG_SECTION_NUMBER = "section_number"

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    @JvmStatic
    fun newInstance(sectionNumber: Int): PlaceholderFragment {
      return PlaceholderFragment().apply {
        arguments = Bundle().apply {
          putInt(ARG_SECTION_NUMBER, sectionNumber)
        }
      }
    }
  }

  override fun onDestroyView() {
    super.onDestroyView()
    _binding = null
  }
}