package com.caloriecounter.calorie.ui.main.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.caloriecounter.calorie.Constant
import com.caloriecounter.calorie.R
import com.caloriecounter.calorie.WeatherApplication
import com.caloriecounter.calorie.base.BaseFragment
import com.caloriecounter.calorie.databinding.FragmentDoubleBinding
import com.caloriecounter.calorie.iinterface.DataChange
import com.caloriecounter.calorie.ui.main.adapter.DoubleImageAdapter
import com.caloriecounter.calorie.ui.main.model.doubleimage.DoubleImage
import com.caloriecounter.calorie.ui.main.viewmodel.WeatherViewModel
import com.caloriecounter.calorie.util.toGone
import com.caloriecounter.calorie.util.toVisible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_double.*

@AndroidEntryPoint
class DoubleFragment : BaseFragment<FragmentDoubleBinding?>() {
    private val weatherViewModel : WeatherViewModel by viewModels()
    private var doubleImageAdapter: DoubleImageAdapter? = null

    private var listImage: ArrayList<DoubleImage> = ArrayList()
    private var type : Array<String>? = null

    private var canLoadMore : Boolean = true
    private var offset : Int = 0;

    private var sort = Constant.SortByDouble.NEWEST

    public fun setData(type : Array<String>?){
        this.type = type
    }



    override fun getLayoutRes(): Int {
        return R.layout.fragment_double
    }

    override fun initView() {

    }
    override fun initData() {
        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding?.rclContent)
        doubleImageAdapter = DoubleImageAdapter(mActivity, listImage,  DataChange {
//            if(it){
//                progressBar.visibility = View.VISIBLE
//                tvRefresh.visibility = View.VISIBLE
//            }else{
//                progressBar.visibility = View.GONE
//                tvRefresh.visibility = View.GONE
//            }
        })
        rclContent.adapter = doubleImageAdapter


        getData()

        rclContent.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE && !recyclerView.canScrollVertically(1)) {
                    getData()
                }
            }
        })
    }
    override fun setListener() {
        swipeToRefresh.setOnRefreshListener {
            reload()
            WeatherApplication.trackingEvent("Refresh_Double")

        }
        rclContent.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                var layoutManager = recyclerView.layoutManager as LinearLayoutManager
                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    var firstVisiblePosition: Int = layoutManager.findFirstVisibleItemPosition()
                    var lastVisiblePosition: Int = layoutManager.findLastVisibleItemPosition()

                    var centerItem : Int = (firstVisiblePosition + lastVisiblePosition)/2
                    doubleImageAdapter?.setCenterPosition(centerItem)
                    doubleImageAdapter?.notifyItemChanged(centerItem)

                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                doubleImageAdapter?.setCenterPosition(-1)
            }
        })


        binding?.layoutSort?.setOnClickListener {
            createSortDialog()
            WeatherApplication.trackingEvent("Click_Sort_Double")
        }
    }
    override fun setObserver() {
        weatherViewModel.doubleImageLiveData.observe(this, Observer {
            binding?.progressBar?.toGone()
            swipeToRefresh.isRefreshing = false
            if (it.offset == 0) {
                listImage.clear()
            }
            listImage.addAll(it.items.shuffled())
            doubleImageAdapter?.notifyDataSetChanged()
            canLoadMore = true
            offset += it.items.size
        })

        weatherViewModel.requestFail.observe(this) {
            canLoadMore = true
            binding?.progressBar?.toGone()
            binding?.tvRefresh?.toGone()

            binding?.swipeToRefresh?.isRefreshing = false
        }
    }

    private fun getData() {
        if (canLoadMore) {
            canLoadMore = false
            weatherViewModel.getDoubleImages( sort, type, "30", offset)
            try {
                WeatherApplication.trackingEvent("Load_more_data_double", "Offset", offset.toString())
            } catch (e: Exception) {
            }
        }
    }
    override fun getFrame(): Int {
        return R.id.mainFrame
    }

    private var popupWindow1: PopupWindow? = null
    private fun createSortDialog() {
        val layoutInflater =
            mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout: View = layoutInflater.inflate(
            R.layout.layout_sort_double,
            binding?.root as ViewGroup,
            false
        )

        var btnNewest = layout.findViewById<TextView>(R.id.btnNewest);
        var btnPopular = layout.findViewById<TextView>(R.id.btnPopular);


        btnNewest.setOnClickListener {
            binding?.tvSortName?.text = "Sort by: NEWEST"
            sort = Constant.SortByDouble.NEWEST
            popupWindow1?.dismiss()
            reload()
            WeatherApplication.trackingEvent("Click_Sort_Newest_Double")
        }

        btnPopular.setOnClickListener {
            binding?.tvSortName?.text = "Sort by: MOST POPULAR"
            sort = Constant.SortByDouble.RATING
            popupWindow1?.dismiss()
            reload()
            WeatherApplication.trackingEvent("Click_Sort_popular_Double")

        }


        popupWindow1 = PopupWindow(
            layout, ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT, true
        )
        popupWindow1?.contentView = layout
        popupWindow1?.showAsDropDown(binding?.layoutSort, 0, 0)
    }


    private fun reload(){
        binding?.progressBar?.toVisible()
        offset = 0
        listImage.clear()
        doubleImageAdapter?.notifyDataSetChanged()
        getData()
    }
}