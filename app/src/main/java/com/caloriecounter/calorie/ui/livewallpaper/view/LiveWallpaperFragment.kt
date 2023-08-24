package com.caloriecounter.calorie.ui.livewallpaper.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.caloriecounter.calorie.Constant
import com.caloriecounter.calorie.R
import com.caloriecounter.calorie.WeatherApplication
import com.caloriecounter.calorie.base.BaseFragment
import com.caloriecounter.calorie.databinding.FragmentLiveWallpaperBinding
import com.caloriecounter.calorie.iinterface.DataChange
import com.caloriecounter.calorie.ui.livewallpaper.adapter.LiveWallpaperAdapter
import com.caloriecounter.calorie.ui.main.model.image.Image
import com.caloriecounter.calorie.ui.main.viewmodel.WeatherViewModel
import com.caloriecounter.calorie.util.toGone
import com.caloriecounter.calorie.util.toVisible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LiveWallpaperFragment : BaseFragment<FragmentLiveWallpaperBinding?>() {

    private val mainViewModel : WeatherViewModel by viewModels()
    private var liveWallpaperAdapter: LiveWallpaperAdapter? = null

    private var listImage: ArrayList<Image> = ArrayList()
    private var type : Array<String>? = null

    private var canLoadMore : Boolean = true
    private var offset : Int = 0;

    private var isShowToolbar = false
    private var sort = Constant.SortBy.DOWNLOAD

    public fun setShowToolbar(isShowToolbar : Boolean){
        this.isShowToolbar = isShowToolbar
    }
    override fun getLayoutRes(): Int {
        return R.layout.fragment_live_wallpaper
    }

    override fun initView() {
        binding?.swipeToRefresh?.setOnRefreshListener {
            WeatherApplication.trackingEvent("Refresh_Live")
            reload()
        }

        binding?.layoutSort?.setOnClickListener {
            WeatherApplication.trackingEvent("Click_Sort_Live")
            createSortDialog()
        }
    }
    override fun initData() {
        liveWallpaperAdapter = LiveWallpaperAdapter(mActivity, listImage,  DataChange {
//            if(it){
//                binding?.progressBar?.visibility = View.VISIBLE
//            }else{
//                binding?.progressBar?.visibility = View.GONE
//            }
        })
        binding?.rclContent?.adapter = liveWallpaperAdapter


        getData()

        binding?.rclContent?.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE && !recyclerView.canScrollVertically(1)) {
                    getData()
                }
            }
        })

        if(isShowToolbar){
            binding?.layoutToolbar?.toVisible()
        }else{
            binding?.layoutToolbar?.toGone()
        }
    }


    override fun setObserver() {
        mainViewModel?.liveRepos?.observe(this, Observer {
            try {
                binding?.loadMoreProgressBar?.visibility = View.GONE
                binding?.layoutReload?.toGone()
            } catch (e: Exception) {
            }
            binding?.progressBar?.toGone()
            binding?.swipeToRefresh?.isRefreshing = false
            if(it.offset == 0) {
                listImage.clear()
            }
            listImage.addAll(it.items)
            liveWallpaperAdapter?.notifyDataSetChanged()
            canLoadMore = true
            offset += it.items.size
        })


        mainViewModel.requestFail.observe(this) {
            canLoadMore = true
            binding?.progressBar?.toGone()

            try {
                binding?.loadMoreProgressBar?.visibility = View.GONE
            } catch (e: Exception) {
            }

            binding?.swipeToRefresh?.isRefreshing = false

            try {
                if(listImage.size == 0){
                    binding?.layoutReload?.toVisible()
                }else{
                    binding?.layoutReload?.toGone()
                }
            } catch (e: Exception) {
            }


        }

//        mainViewModel?.failRepos?.observe(this, Observer {
//            canLoadMore = true
//        })
    }

    private fun getData() {
        if (canLoadMore) {
            try {
                binding?.loadMoreProgressBar?.visibility = View.VISIBLE
            } catch (e: Exception) {
            }
            canLoadMore = false
            mainViewModel.getLiveWallpaper( sort, "android_video", "30", offset)
            try {
                WeatherApplication.trackingEvent("Load_more_data_Live", "Offset", offset.toString())
            } catch (e: Exception) {
            }
        }
    }

    override fun setListener() {
        binding?.btnBack?.setOnClickListener {
            mActivity.onBackPressed()
            WeatherApplication.trackingEvent("Click_Check_Auto_change")
        }

        binding?.btnReload?.setOnClickListener {
            binding?.layoutReload?.toGone()
            reload()
            WeatherApplication.trackingEvent("Refresh_Home")
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
            binding?.tvSortName?.text = "NEWEST"
            sort = Constant.SortBy.NEWEST
            popupWindow1?.dismiss()
            offset = 0
            getData()
            WeatherApplication.trackingEvent("Click_Sort_Newest_Live")
        }

        btnPopular.setOnClickListener {
            binding?.tvSortName?.text = "MOST DOWNLOADED"
            sort = Constant.SortBy.DOWNLOAD
            popupWindow1?.dismiss()
            offset = 0
            getData()
            WeatherApplication.trackingEvent("Click_Sort_DOWNLOAD_Live")
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
        liveWallpaperAdapter?.notifyDataSetChanged()
        getData()
    }
}