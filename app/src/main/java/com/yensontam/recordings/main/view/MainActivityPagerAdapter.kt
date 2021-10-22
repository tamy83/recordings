package com.yensontam.recordings.main.view

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.yensontam.recordings.main.state.MainActivityIntent
import com.yensontam.recordings.main.state.MainActivityState
import com.yensontam.recordings.main.viewmodel.MainViewModel

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class MainActivityPagerAdapter(private val context: Context,
                               fm: FragmentManager,
                               var mainActivityState: MainActivityState,
                               private val viewModel: MainViewModel) :
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