package com.yensontam.recordings.main.view

import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import com.yensontam.recordings.databinding.ActivityMainBinding
import com.yensontam.recordings.factory.FragmentFactory
import com.yensontam.recordings.main.state.MainActivityIntent
import com.yensontam.recordings.main.state.MainActivityState
import com.yensontam.recordings.main.state.MainActivityViewEffect
import com.yensontam.recordings.main.viewmodel.MainViewModel

class MainActivity: AppCompatActivity() {

  private lateinit var binding: ActivityMainBinding
  private lateinit var viewModel: MainViewModel
  private lateinit var pagerAdapter: MainActivityPagerAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)

    val mainViewModel: MainViewModel by viewModels()
    viewModel = mainViewModel
    viewModel.onIntentReceived(MainActivityIntent.LoadedIntent)

    val initialState = viewModel.stateLiveData.value ?: MainActivityState(FragmentFactory(), listOf())
    pagerAdapter = MainActivityPagerAdapter(this, supportFragmentManager, initialState, viewModel = viewModel)
    val viewPager: ViewPager = binding.viewPager
    viewPager.adapter = pagerAdapter
    val tabs: TabLayout = binding.tabs
    tabs.setupWithViewPager(viewPager)

    viewModel.stateLiveData.observe(this, {
      renderState(it)
    })

    viewModel.effectSingleLiveEvent.observe(this, {
      showViewEffect(it)
    })
  }

  private fun renderState(state: MainActivityState) {
    pagerAdapter.mainActivityState = state
  }

  private fun showViewEffect(viewEffect: MainActivityViewEffect) {

  }

}