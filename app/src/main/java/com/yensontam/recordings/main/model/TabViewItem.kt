package com.yensontam.recordings.main.model

import androidx.fragment.app.Fragment
import com.yensontam.recordings.main.state.Tab

data class TabViewItem(
  val tab: Tab, // this is probably an enum
  val titleResId: Int,
  val position: Int
)