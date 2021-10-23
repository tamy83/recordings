package com.yensontam.recordings.main.state

import com.yensontam.recordings.factory.FragmentProvider
import com.yensontam.recordings.main.model.TabViewItem
import com.yensontam.recordings.mvi.IAction
import com.yensontam.recordings.mvi.IState

data class MainActivityState(
  val fragmentProvider: FragmentProvider,
  var tabViewItems: List<TabViewItem>
) : IState {
  val numTabs: Int
    get() {
      return tabViewItems.size
    }

  override fun consumeAction(action: IAction) : MainActivityState {
    val newState = this.copy()
    when (action) {
      is MainAcitivityAction.HasTabsAction -> {
        newState.tabViewItems = action.tabViewItems
      }
    }
    return newState
  }
}

enum class Tab {
  RECORD,
  RECORDINGS
}