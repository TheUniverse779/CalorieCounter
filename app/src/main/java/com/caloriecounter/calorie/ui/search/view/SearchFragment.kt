package com.caloriecounter.calorie.ui.search.view

import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.caloriecounter.calorie.R
import com.caloriecounter.calorie.WeatherApplication
import com.caloriecounter.calorie.base.BaseFragment
import com.caloriecounter.calorie.databinding.FragmentSearchBinding
import com.caloriecounter.calorie.ui.main.viewmodel.WeatherViewModel
import com.caloriecounter.calorie.ui.search.adapter.TagTrendingAdapter
import com.caloriecounter.calorie.ui.search.model.PopularTag
import com.caloriecounter.calorie.util.PreferenceUtil
import com.caloriecounter.calorie.util.toGone
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding?>() {
    private val weatherViewModel : WeatherViewModel by viewModels()
    private var tagTrendingAdapter : TagTrendingAdapter? = null
    private val listPopularTag : ArrayList<PopularTag> = ArrayList()

    @Inject lateinit var preferenceUtil: PreferenceUtil
    override fun getLayoutRes(): Int {
        return R.layout.fragment_search
    }

    override fun initView() {}
    override fun initData() {
        weatherViewModel.getPopularTag()
        tagTrendingAdapter = TagTrendingAdapter(mActivity, listPopularTag, this)
        binding?.rcvTagTrending?.adapter = tagTrendingAdapter

    }
    override fun setListener() {
        binding?.btnBack?.setOnClickListener { mActivity.onBackPressed() }
        binding?.edtSearch?.setOnClickListener {
            WeatherApplication.trackingEvent("Click_EdtSearch")
            var searchResultFragment = SearchResultFragment();
            searchResultFragment.setShowKeyboard(true)
            addFragment(searchResultFragment)
        }
    }


    override fun setObserver() {
        weatherViewModel.popularTagResponse.observe(this, Observer {
            binding?.progressBar?.toGone()
            listPopularTag.addAll(it.items)
            tagTrendingAdapter?.notifyDataSetChanged()
        })
    }
    override fun getFrame(): Int {
        return R.id.mainFrame
    }
}