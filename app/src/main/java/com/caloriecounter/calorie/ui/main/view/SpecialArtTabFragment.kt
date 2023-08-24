package com.caloriecounter.calorie.ui.main.view

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.caloriecounter.calorie.Constant
import com.caloriecounter.calorie.base.BaseFragment
import com.caloriecounter.calorie.R
import com.caloriecounter.calorie.WeatherApplication
import com.caloriecounter.calorie.databinding.FragmentSpecialBinding
import com.caloriecounter.calorie.ui.main.adapter.ImageAdapter
import com.caloriecounter.calorie.ui.main.model.image.Image
import com.caloriecounter.calorie.ui.main.viewmodel.WeatherViewModel
import com.caloriecounter.calorie.util.toGone
import com.caloriecounter.calorie.util.toVisible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SpecialArtTabFragment : BaseFragment<FragmentSpecialBinding?>() {
    private val weatherViewModel : WeatherViewModel by viewModels()
    private var imageAdapter : ImageAdapter? = null

    private var listImage: ArrayList<Image> = ArrayList()
    private var type : Array<String>? = null
    val uploader: Array<String> = arrayOf("wlc_ai_art")

    private var canLoadMore : Boolean = true
    private var offset : Int = 0;

    private var sort = Constant.SortBy.RATING


    public fun setData(type : Array<String>?){
        this.type = type
    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_special
    }

    override fun initView() {
        binding?.swipeToRefresh?.setOnRefreshListener {
            reload()
            WeatherApplication.trackingEvent("Refresh_Special_Art")
        }
    }
    override fun initData() {
        imageAdapter = ImageAdapter(mActivity, listImage,true, false, true)
        imageAdapter?.setDataPresentImageType(Constant.PresentImageType.HOME, null, Constant.SortBy.RATING, type)

        var layoutManager = GridLayoutManager(mActivity, 3)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                var type = imageAdapter?.getItemViewType(position)
                if (type == 2 || type == 1) {
                    return 3
                } else {
                    return 1
                }
            }

        }
        binding?.rclContent?.apply {
            this.layoutManager = layoutManager
            adapter = imageAdapter
            setHasFixedSize(true)
            setItemViewCacheSize(20)
            isDrawingCacheEnabled = true
            drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
            addOnScrollListener(object : RecyclerView.OnScrollListener(){
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE && !recyclerView.canScrollVertically(1)) {
                        getData()
                    }
                }
            })
        }


    }

    private fun getData() {
        if (canLoadMore) {
            try {
                binding?.loadMoreProgressBar?.visibility = View.VISIBLE
            } catch (e: Exception) {
            }
            canLoadMore = false
            weatherViewModel.getImagesByCatId(null, sort, type, uploader,"60", offset)
            try {
                WeatherApplication.trackingEvent("Load_more_data_Special_Art", "Offset", offset.toString())
            } catch (e: Exception) {
            }
        }
    }


    override fun setListener() {
        binding?.layoutSort?.setOnClickListener {
            createSortDialog()
            WeatherApplication.trackingEvent("Click_Sort_Special_Art")
        }

        binding?.btnReload?.setOnClickListener {
            binding?.layoutReload?.toGone()
            reload()
            WeatherApplication.trackingEvent("Refresh_Home")
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    override fun setObserver() {
        weatherViewModel.dataResponseLiveData.observe(this, Observer {
            binding?.progressBar?.toGone()
            binding?.tvRefresh?.toGone()
            binding?.layoutReload?.toGone()
            try {
                binding?.loadMoreProgressBar?.visibility = View.GONE
            } catch (e: Exception) {
            }
            binding?.swipeToRefresh?.isRefreshing = false
            if (it.offset == 0) {
                listImage.clear()
            }
            var items = it.items.shuffled() as ArrayList
            offset += it.items.size
            if(items.size >= 30){
                var adsItem = Image()
                adsItem.itemType = 2
                items.add(30, adsItem)
            }else if(items.size > 0){
                var adsItem = Image()
                adsItem.itemType = 2
                items.add(adsItem)
            }
            listImage.addAll(items)
            imageAdapter?.notifyDataSetChanged()
            canLoadMore = true
        })

        weatherViewModel.requestFail.observe(this) {
            canLoadMore = true
            binding?.progressBar?.toGone()
            binding?.tvRefresh?.toGone()

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

        getData()
    }
    override fun getFrame(): Int {
        return R.id.mainFrame
    }


    private var popupWindow1: PopupWindow? = null
    private fun createSortDialog() {
        val layoutInflater =
            mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val layout: View = layoutInflater.inflate(
            R.layout.layout_filter_gift_type,
            binding?.root as ViewGroup,
            false
        )

        var btnNewest = layout.findViewById<TextView>(R.id.btnNewest);
        var btnPopular = layout.findViewById<TextView>(R.id.btnPopular);
        var btnDownload = layout.findViewById<TextView>(R.id.btnDownload);
        var btnFavorite = layout.findViewById<TextView>(R.id.btnFavorite);


        btnNewest.setOnClickListener {
            binding?.tvSortName?.text = "Sort by: NEWEST"
            sort = Constant.SortBy.NEWEST
            popupWindow1?.dismiss()
            reload()
            WeatherApplication.trackingEvent("Click_Sort_Newest_Special_art")
        }

        btnPopular.setOnClickListener {
            binding?.tvSortName?.text = "Sort by: MOST POPULAR"
            sort = Constant.SortBy.RATING
            popupWindow1?.dismiss()
            reload()
            WeatherApplication.trackingEvent("Click_Sort_popular_Special_art")
        }

        btnDownload.setOnClickListener {
            binding?.tvSortName?.text = "Sort by: MOST DOWNLOADED"
            sort = Constant.SortBy.DOWNLOAD
            popupWindow1?.dismiss()
            reload()
            WeatherApplication.trackingEvent("Click_Sort_download_Special_art")
        }

        btnFavorite.setOnClickListener {
            binding?.tvSortName?.text = "Sort by: MOST FAVORITE"
            sort = Constant.SortBy.FAVORITES
            popupWindow1?.dismiss()
            reload()
            WeatherApplication.trackingEvent("Click_Sort_Favorite_Special_art")
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
        imageAdapter?.notifyDataSetChanged()
        getData()
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyAllNativeAds()
    }

    private fun destroyAllNativeAds(){
        try {
            for (i in 0 until listImage.size){
                try {
                    if(listImage[i].itemType == 2 && listImage[i].nativeAd != null){
                        listImage[i].nativeAd.destroy()
                    }
                } catch (e: Exception) {
                }
            }
        } catch (e: Exception) {
        }
    }
}