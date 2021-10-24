package com.yensontam.recordings.main.view

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.yensontam.recordings.main.state.MainActivityState

class MainActivityPagerAdapter(private val context: Context,
                               fm: FragmentManager,
                               var mainActivityState: MainActivityState) :
  FragmentPagerAdapter(fm) {

  override fun getItem(position: Int): Fragment {
    return mainActivityState.fragmentProvider.getFragment(mainActivityState.tabViewItems[position].tab)
  }

  override fun getPageTitle(position: Int): CharSequence? {
    return context.getString(mainActivityState.tabViewItems[position].titleResId)
  }

  override fun getCount(): Int {
    return mainActivityState.numTabs
  }
}