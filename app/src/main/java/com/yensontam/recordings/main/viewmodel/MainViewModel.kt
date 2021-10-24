package com.yensontam.recordings.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yensontam.recordings.R
import com.yensontam.recordings.factory.FragmentFactory
import com.yensontam.recordings.main.model.TabViewItem
import com.yensontam.recordings.main.state.*
import com.yensontam.recordings.SingleLiveEvent

class MainViewModel : ViewModel() {

  private val initialState = MainActivityState(FragmentFactory(), listOf())
  val stateLiveData = MutableLiveData(initialState)

  private val currentState: MainActivityState
    get() {
      return stateLiveData.value ?: initialState
    }

  fun onIntentReceived(intent: MainActivityIntent) {
    when(intent) {
      is MainActivityIntent.LoadedIntent -> {
        handleLoadedIntent(intent)
      }
    }
  }

  private fun handleLoadedIntent(intent: MainActivityIntent.LoadedIntent) {
    stateLiveData.value = currentState.consumeAction(MainAcitivityAction.HasTabsAction(tabViewItems = getTabViewItems()))
  }


  private fun getTabViewItems(): List<TabViewItem> {
    val list = mutableListOf<TabViewItem>()
    Tab.values().forEachIndexed { i, it ->
      val tabViewItem = TabViewItem(it, getTitleResId(it), i)
      list.add(tabViewItem)
    }
    return list
  }

  private fun getTitleResId(tab: Tab): Int {
    return when (tab) {
      Tab.RECORD ->
        R.string.tab_title_record
      Tab.RECORDINGS ->
        R.string.tab_title_recordings
    }
  }
}