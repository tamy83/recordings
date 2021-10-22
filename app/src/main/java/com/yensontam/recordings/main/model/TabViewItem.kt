package com.yensontam.recordings.main.model

import com.yensontam.recordings.main.state.Tab

data class TabViewItem(
  val tab: Tab,
  val titleResId: Int,
  val position: Int
)