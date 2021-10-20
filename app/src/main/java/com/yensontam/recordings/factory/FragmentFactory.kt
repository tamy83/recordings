package com.yensontam.recordings.factory

import androidx.fragment.app.Fragment
import com.yensontam.recordings.main.state.Tab

class FragmentFactory : FragmentProvider {

  override fun getFragment(tab: Tab): Fragment {
    return when (tab) {
      Tab.RECORD ->
        Fragment()
      Tab.RECORDINGS ->
        Fragment()
    }
  }
}