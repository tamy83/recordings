package com.yensontam.recordings.factory

import androidx.fragment.app.Fragment
import com.yensontam.recordings.main.state.Tab

interface FragmentProvider {
  fun getFragment(tab: Tab): Fragment
}