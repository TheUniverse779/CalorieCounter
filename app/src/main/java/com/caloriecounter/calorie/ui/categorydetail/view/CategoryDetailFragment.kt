package com.caloriecounter.calorie.ui.categorydetail.view

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.caloriecounter.calorie.Constant
import com.caloriecounter.calorie.R
import com.caloriecounter.calorie.WeatherApplication
import com.caloriecounter.calorie.base.BaseFragment
import com.caloriecounter.calorie.databinding.FragmentCategoryDetailBinding
import com.caloriecounter.calorie.ui.main.adapter.TabAdapter
import com.caloriecounter.calorie.ui.main.model.category.Category
import com.caloriecounter.calorie.ui.search.view.SearchActivity
import com.caloriecounter.calorie.util.toVisible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryDetailFragment : BaseFragment<FragmentCategoryDetailBinding?>() {
    private var tabAdapter: TabAdapter? = null
    private var category: Category? = null

    public fun setData(category: Category?) {
        this.category = category
    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_category_detail
    }

    override fun initView() {
        binding?.tvCateName?.text = category?.title
    }

    override fun initData() {
        try {
            Glide.with(mActivity).load("https://eztech-wallpapers.s3.ap-southeast-2.amazonaws.com/wallpaper/avatar/avatar_"+category?.title?.toLowerCase()+".jpg").placeholder(R.drawable.test_image_banner).into(binding?.imgBackdrop!!)
        } catch (e: Exception) {
        }
        tabAdapter = TabAdapter(childFragmentManager, mActivity)
//        for (i in 0..2) {
//            var categoryDetailTabFragment = CategoryDetailTabFragment()
//            categoryDetailTabFragment.setData(
//                category?.id,
//                i,
//                null,
//                if (i == 0) Constant.SortBy.DOWNLOAD else if (i == 1) Constant.SortBy.RATING else null,
//                Constant.PresentImageType.CATEGORY
//            )
//            tabAdapter?.addFragment(categoryDetailTabFragment, "")
//        }
        binding?.vpContent?.adapter = tabAdapter
        binding?.tabLayout?.setupWithViewPager(binding?.vpContent)

        var index = 0;
        for (i in 0..2) {
            val tabHome: View = LayoutInflater.from(mActivity).inflate(R.layout.item_tab, null)
            val tvHome = tabHome.findViewById<TextView>(R.id.tvTitle)
            val indicator = tabHome.findViewById<View>(R.id.indicator)
            tvHome.text = if (i == 0) "Most download" else if (i == 1) "Most popular" else "Newest"
            if (i == 0) {
                tvHome.setTextColor(Color.parseColor("#FFFFFF"))
                indicator.toVisible()
                val view = tabHome.findViewById<View>(R.id.rootView)
//                view.background = ContextCompat.getDrawable(
//                    mActivity,
//                    R.drawable.f2_bg_tab_selected_round_suggestion_location
//                )
            }
            binding?.tabLayout?.getTabAt(index)?.customView = tabHome
            index += 1
        }

        binding?.tabLayout?.addOnTabSelectedListener(onTabSelectedListener);
        binding?.vpContent?.offscreenPageLimit = 10
    }


    override fun setListener() {
        binding?.btnBack?.setOnClickListener {
            mActivity.onBackPressed()
        }

        binding?.btnSearch?.setOnClickListener {
            SearchActivity.startScreen(mActivity, null)
            WeatherApplication.trackingEvent("Click_Search_Category_Detail")
        }
    }

    override fun setObserver() {}

    private val onTabSelectedListener: TabLayout.OnTabSelectedListener = object :
        TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab) {
            val c = tab.position
            tabAdapter?.setOnSelectView(binding?.tabLayout, c)
        }

        override fun onTabUnselected(tab: TabLayout.Tab) {
            val c = tab.position
            tabAdapter?.setUnSelectView(binding?.tabLayout, c)
        }

        override fun onTabReselected(tab: TabLayout.Tab) {}
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        EventBus.getDefault().register(this)
//    }

    override fun getFrame(): Int {
        return R.id.mainFrame
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        EventBus.getDefault().unregister(this)
//    }
//
//    @Subscribe
//    public fun onHaveBackdrop(onHaveBackdrop: OnHaveBackdrop){
//        Glide.with(mActivity).load(onHaveBackdrop.link).placeholder(R.drawable.test_image_banner).into(binding?.imgBackdrop)
//    }
//
//    override fun getFrame(): Int {
//        return R.id.mainFrame
//    }
}