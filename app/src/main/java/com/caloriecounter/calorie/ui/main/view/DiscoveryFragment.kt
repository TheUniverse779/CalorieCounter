package com.caloriecounter.calorie.ui.main.view

import android.annotation.SuppressLint
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.caloriecounter.calorie.R
import com.caloriecounter.calorie.WeatherApplication
import com.caloriecounter.calorie.base.BaseFragment
import com.caloriecounter.calorie.databinding.FragmentDiscoveryBinding
import com.caloriecounter.calorie.databinding.FragmentHomeTabBinding
import com.caloriecounter.calorie.ui.main.adapter.DiscoverAdapter
import com.caloriecounter.calorie.ui.main.model.dish.Category
import com.caloriecounter.calorie.ui.main.viewmodel.WeatherViewModel
import com.caloriecounter.calorie.util.toGone
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DiscoveryFragment : BaseFragment<FragmentDiscoveryBinding?>() {

    private val weatherViewModel: WeatherViewModel by viewModels()
    private var discoverAdapter: DiscoverAdapter? = null

    private var listImage: ArrayList<Category> = ArrayList()



    override fun getLayoutRes(): Int {
        return R.layout.fragment_discovery
    }

    override fun initView() {

    }

    override fun initData() {
        discoverAdapter = DiscoverAdapter(mActivity, listImage)
        var layoutManager = LinearLayoutManager(mActivity)

        binding?.rclContent?.apply {
            this.layoutManager = layoutManager
            adapter = discoverAdapter
            setHasFixedSize(true)
            setItemViewCacheSize(20)
            isDrawingCacheEnabled = true
            drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
        }

    }

    private fun getData() {

    }


    override fun setListener() {

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun setObserver() {
        weatherViewModel.dataResponseLiveData.observe(this, Observer {


        })

        weatherViewModel.requestFail.observe(this) {

        }

        getData()
    }

    override fun getFrame(): Int {
        return R.id.mainFrame
    }

    private fun reload(){

    }

    override fun onDestroy() {
        super.onDestroy()
    }


}