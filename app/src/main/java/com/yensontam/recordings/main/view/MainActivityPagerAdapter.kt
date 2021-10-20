package com.yensontam.recordings.main.view

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.yensontam.recordings.R
import com.yensontam.recordings.factory.FragmentProvider
import com.yensontam.recordings.main.state.MainActivityIntent
import com.yensontam.recordings.main.state.MainActivityState
import com.yensontam.recordings.main.state.Tab
import com.yensontam.recordings.main.viewmodel.MainActivityViewModel

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class MainActivityPagerAdapter(private val context: Context,
                               fm: FragmentManager,
                               var mainActivityState: MainActivityState,
                               private val viewModel: MainActivityViewModel) :
  FragmentPagerAdapter(fm) {

  override fun getItem(position: Int): Fragment {
    viewModel.onIntentReceived(MainActivityIntent.SelectedTabIntent(position))
    return mainActivityState.fragmentProvider.getFragment(mainActivityState.tabViewItems[position].tab)
  }

  override fun getPageTitle(position: Int): CharSequence? {
    return context.getString(mainActivityState.tabViewItems[position].titleResId)
  }

  override fun getCount(): Int {
    return mainActivityState.numTabs
  }
}