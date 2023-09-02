package com.caloriecounter.calorie.ui.main.view

import android.annotation.SuppressLint
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.caloriecounter.calorie.R
import com.caloriecounter.calorie.base.BaseFragment
import com.caloriecounter.calorie.databinding.FragmentAllDishBinding
import com.caloriecounter.calorie.databinding.FragmentDiscoveryBinding
import com.caloriecounter.calorie.model.Schedule
import com.caloriecounter.calorie.ui.main.adapter.DiscoverAdapter
import com.caloriecounter.calorie.ui.main.adapter.DishAdapter
import com.caloriecounter.calorie.ui.main.model.dish.Dish
import com.caloriecounter.calorie.ui.main.model.dish.DishData
import com.caloriecounter.calorie.ui.main.viewmodel.WeatherViewModel
import com.caloriecounter.calorie.util.Util
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.thin.downloadmanager.util.Log
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AllDishFragment : BaseFragment<FragmentAllDishBinding?>() {

    private val weatherViewModel: WeatherViewModel by viewModels()
    private var discoverAdapter: DishAdapter? = null

    private var listImage: ArrayList<Dish> = ArrayList()



    override fun getLayoutRes(): Int {
        return R.layout.fragment_all_dish
    }

    override fun initView() {

    }

    override fun initData() {
        val schedules = Gson().fromJson<ArrayList<Dish>>(
            Util.loadJSONFromAsset(mActivity, "allDish"),
            object : TypeToken<List<Dish?>?>() {}.type
        )

        discoverAdapter = DishAdapter(mActivity, schedules)

        binding?.rclContent?.apply {
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