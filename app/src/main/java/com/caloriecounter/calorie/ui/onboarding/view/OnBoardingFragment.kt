package com.caloriecounter.calorie.ui.onboarding.view

import androidx.viewpager.widget.ViewPager
import com.caloriecounter.calorie.R
import com.caloriecounter.calorie.base.BaseFragment
import com.caloriecounter.calorie.databinding.FragmentOnboardBinding
import com.caloriecounter.calorie.ui.main.adapter.TabAdapter
import com.caloriecounter.calorie.ui.main.view.MainActivity
import com.caloriecounter.calorie.util.toGone
import com.caloriecounter.calorie.util.toVisible

class OnBoardingFragment : BaseFragment<FragmentOnboardBinding?>() {
    private var tabAdapter: TabAdapter? = null
    override fun getLayoutRes(): Int {
        return R.layout.fragment_onboard
    }

    override fun initView() {}
    override fun initData() {
        tabAdapter = TabAdapter(childFragmentManager, mActivity)
        var onBoardingTabFragment = OnBoardingVideoTabFragment()
        onBoardingTabFragment.position = 0
        onBoardingTabFragment.onBoardingFragment = this
        tabAdapter?.addFragment(onBoardingTabFragment, "")

        var onBoardingTabFragment1 = OnBoardingVideoTabFragment()
        onBoardingTabFragment1.position = 1
        onBoardingTabFragment.onBoardingFragment = this
        tabAdapter?.addFragment(onBoardingTabFragment1, "")

        var onBoardingTabFragment2 = OnBoardingVideoTabFragment()
        onBoardingTabFragment2.position = 2
        onBoardingTabFragment.onBoardingFragment = this
        tabAdapter?.addFragment(onBoardingTabFragment2, "")


        var onBoardingTabFragment3 = OnBoardingTab2Fragment()
        tabAdapter?.addFragment(onBoardingTabFragment3, "")

        binding?.vpMain?.adapter = tabAdapter

        binding?.vpMain?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                if(position == 3){
                    binding?.btnSkip?.toGone()
                    binding?.btnClose?.toVisible()
                }else{
                    binding?.btnSkip?.toVisible()
                    binding?.btnClose?.toGone()
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

        })
    }
    override fun setListener() {
        binding?.btnNext?.setOnClickListener {
            next()
        }

        binding?.btnSkip?.setOnClickListener {
            MainActivity.startScreen(mActivity, null)
        }

        binding?.btnClose?.setOnClickListener {
            MainActivity.startScreen(mActivity, null)
        }
    }
    override fun setObserver() {}
    override fun getFrame(): Int {
        return R.id.mainFrame
    }

    public fun next(){
        if(binding?.vpMain?.currentItem == 3){
            MainActivity.startScreen(mActivity, null)
            mActivity.finish()
        }else {
            binding?.vpMain?.currentItem = binding?.vpMain?.currentItem!! + 1
        }
    }
}