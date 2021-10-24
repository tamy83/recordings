package com.yensontam.recordings.factory

import androidx.fragment.app.Fragment
import com.yensontam.recordings.main.state.Tab
import com.yensontam.recordings.record.view.RecordFragment
import com.yensontam.recordings.recordings.view.RecordingsFragment

class FragmentFactory : FragmentProvider {

  override fun getFragment(tab: Tab): Fragment {
    return when (tab) {
      Tab.RECORD ->
        RecordFragment()
      Tab.RECORDINGS ->
        RecordingsFragment()
    }
  }
}