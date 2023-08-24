package com.caloriecounter.calorie.ui.onboarding.view

import com.caloriecounter.calorie.R
import com.caloriecounter.calorie.base.BaseFragment
import com.caloriecounter.calorie.databinding.FragmentOnboard1Binding

class OnBoardingTabFragment : BaseFragment<FragmentOnboard1Binding?>() {
    public var position : Int = 0
    public var onBoardingFragment : OnBoardingFragment? = null
    override fun getLayoutRes(): Int {
        return R.layout.fragment_onboard_1
    }

    override fun initView() {}
    override fun initData() {
        when(position){
            0 -> {
                binding?.tv1?.text = "100.000+"
                binding?.tv2?.text = "4K Wallpapers"
                binding?.imgBanner?.setImageResource(R.drawable.intro_1)
            }
            1 -> {
                binding?.tv1?.text = "Various"
                binding?.tv2?.text = "Wallpapers"
                binding?.imgBanner?.setImageResource(R.drawable.intro_2)
            }
            2 -> {
                binding?.tv1?.text = "Unique"
                binding?.tv2?.text = "Video Wallpapers"
                binding?.imgBanner?.setImageResource(R.drawable.background_video)
            }
        }
    }
    override fun setListener() {

    }
    override fun setObserver() {}
    override fun getFrame(): Int {
        return R.id.mainFrame
    }
}