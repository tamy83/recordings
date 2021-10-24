package com.yensontam.recordings.main.state

import com.yensontam.recordings.main.model.TabViewItem
import com.yensontam.recordings.mvi.IAction

sealed class MainAcitivityAction: IAction {
  data class HasTabsAction(val tabViewItems: List<TabViewItem>) : MainAcitivityAction()
}