package com.caloriecounter.calorie.ui.main.view

import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.caloriecounter.calorie.R
import com.caloriecounter.calorie.WeatherApplication
import com.caloriecounter.calorie.base.BaseFragment
import com.caloriecounter.calorie.databinding.FragmentCategoryBinding
import com.caloriecounter.calorie.ui.main.LinearLayoutManagerWithSmoothScroller
import com.caloriecounter.calorie.ui.main.adapter.CategoryInPageAdapter
import com.caloriecounter.calorie.ui.main.model.category.Category
import com.caloriecounter.calorie.ui.main.viewmodel.WeatherViewModel
import com.caloriecounter.calorie.util.toGone
import com.caloriecounter.calorie.util.toVisible
import dagger.hilt.android.AndroidEntryPoint
import java.util.logging.Handler


@AndroidEntryPoint
class CategoryFragment : BaseFragment<FragmentCategoryBinding?>() {
    private var categoryInPageAdapter: CategoryInPageAdapter? = null
    private var listCategory = ArrayList<Category>()
    private val weatherViewModel: WeatherViewModel by viewModels()

    private var handle : Handler? = null
    override fun getLayoutRes(): Int {
        return R.layout.fragment_category
    }

    override fun initView() {}
    override fun initData() {
        weatherViewModel.getAllCat()
        binding?.swipeToRefresh?.setOnRefreshListener {
            weatherViewModel.getAllCat()
            binding?.progressBar?.visibility = View.VISIBLE
            WeatherApplication.trackingEvent("Refresh_Category")
        }

    }
    override fun setListener() {
        binding?.btnReload?.setOnClickListener {
            binding?.layoutReload?.toGone()
            binding?.progressBar?.visibility = View.VISIBLE
            weatherViewModel.getAllCat()
            WeatherApplication.trackingEvent("Refresh_Category")
        }
    }
    override fun setObserver() {
        weatherViewModel.categoryResponseLiveData.observe(this, Observer {
            binding?.swipeToRefresh?.isRefreshing = false
            binding?.progressBar?.visibility = View.GONE
            categoryInPageAdapter = CategoryInPageAdapter(mActivity, it.items, weatherViewModel, this)
            binding?.rclCategory?.adapter = categoryInPageAdapter
            binding?.rclCategory?.toVisible()

            binding?.rclCategory?.setHasFixedSize(true)
            binding?.rclCategory?.setItemViewCacheSize(40)

            doCircle()

        })

        weatherViewModel.requestFail.observe(this) {
            binding?.progressBar?.toGone()
            binding?.swipeToRefresh?.isRefreshing = false
            binding?.layoutReload?.toVisible()
            binding?.rclCategory?.toGone()
        }
    }
    override fun getFrame(): Int {
        return R.id.mainFrame
    }

    private fun doCircle(){
        android.os.Handler().postDelayed(Runnable {
            val childCount: Int = binding?.rclCategory?.childCount!!
            try {
                for (i in 0 until childCount) {
                    try {
                        val childView: View = binding?.rclCategory?.getChildAt(i)!!
                        val childRecyclerView =
                            childView.findViewById<RecyclerView>(R.id.rclImage)
                        val childLayoutManager = childRecyclerView.layoutManager as LinearLayoutManager?
                        if (childLayoutManager != null) {
                            if(i % 2 == 0) {
                                val firstVisibleItemPosition =
                                    childLayoutManager.findLastVisibleItemPosition()
                                childRecyclerView.smoothScrollToPosition(firstVisibleItemPosition + 1)
                            }else{
                                val firstVisibleItemPosition =
                                    childLayoutManager.findFirstVisibleItemPosition()
                                childRecyclerView.smoothScrollToPosition(firstVisibleItemPosition - 1)
                            }
                        }
                    } catch (e: Exception) {
                    }
                }

                doCircle()
            } catch (e: Exception) {
                Log.e("hihihiihihi", e.message.toString())
            }
        }, 1000)
    }
}