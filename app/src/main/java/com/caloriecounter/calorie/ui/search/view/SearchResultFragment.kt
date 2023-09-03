package com.caloriecounter.calorie.ui.search.view

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.PopupWindow
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.caloriecounter.calorie.Constant
import com.caloriecounter.calorie.R
import com.caloriecounter.calorie.base.BaseFragment
import com.caloriecounter.calorie.databinding.FragmentSearchResultBinding
import com.caloriecounter.calorie.ui.main.adapter.ImageAdapter
import com.caloriecounter.calorie.ui.main.model.image.Image
import com.caloriecounter.calorie.ui.main.viewmodel.WeatherViewModel
import com.caloriecounter.calorie.ui.search.adapter.RecentAdapter
import com.caloriecounter.calorie.util.KeyboardUtil
import com.caloriecounter.calorie.util.PreferenceUtil
import com.caloriecounter.calorie.util.toGone
import com.caloriecounter.calorie.util.toVisible
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jakewharton.rxbinding2.widget.RxTextView
import com.caloriecounter.calorie.WeatherApplication
import com.caloriecounter.calorie.ui.main.model.dish.Dish
import com.caloriecounter.calorie.ui.search.adapter.DishSearchAdapter
import com.caloriecounter.calorie.ui.search.model.SearchContent
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class SearchResultFragment : BaseFragment<FragmentSearchResultBinding?>() {
    private val weatherViewModel: WeatherViewModel by viewModels()
    private var recentAdapter: RecentAdapter? = null
    private var canLoadMore: Boolean = true
    private var offset: Int = 0;
    private var imageAdapter: DishSearchAdapter? = null
    private var listImage = ArrayList<SearchContent>()
    private var keyword: String? = null
    private var sort = Constant.SortBy.RATING
    private var isShowKeyboard = false;

    public fun setShowKeyboard(isShowKeyboard: Boolean) {
        this.isShowKeyboard = isShowKeyboard;
    }


    public fun setKeyword(keyword: String?) {
        this.keyword = keyword
    }

    @Inject
    lateinit var preferenceUtil: PreferenceUtil
    override fun getLayoutRes(): Int {
        return R.layout.fragment_search_result
    }

    override fun initView() {}
    override fun initData() {
        recentAdapter =
            RecentAdapter(getRecentSearch(), mActivity, object : RecentAdapter.ClickItem {
                override fun onClickItem(string: String?) {
                    binding?.edtSearch?.setText(string)
                }
            }, object : RecentAdapter.ClickDeleteItem{
                override fun onClickDeleteItem() {
                    recentAdapter?.setData(getRecentSearch())
                }

            })
        binding?.rcvHistory?.adapter = recentAdapter

        imageAdapter = DishSearchAdapter(mActivity, listImage)


        binding?.rcvResult?.adapter = imageAdapter
        handleSearch()

        if (isShowKeyboard) {
            binding?.edtSearch?.requestFocus()
            Handler().postDelayed(Runnable {
                KeyboardUtil.showKeyboard(
                    mActivity,
                    binding?.edtSearch
                )
            }, 200)
        }
        binding?.edtSearch?.setText(keyword)
    }

    override fun setListener() {
        binding?.btnBack?.setOnClickListener { mActivity.onBackPressed() }


        binding?.edtSearch?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                if (binding?.edtSearch?.text!!.isNotEmpty()) {
                    addRecentSearchAndReload()
                    KeyboardUtil.hideKeyboard(mActivity, binding?.edtSearch)
                }
                true
            } else {
                false
            }
        }

        binding?.rcvResult?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE && !recyclerView.canScrollVertically(
                        1
                    )
                ) {
                    getData()
                }
            }
        })

        binding?.rcvHistory?.setOnTouchListener { p0, p1 ->
            KeyboardUtil.hideKeyboard(mActivity, binding?.edtSearch)
            false
        }

        binding?.rcvResult?.setOnTouchListener { p0, p1 ->
            KeyboardUtil.hideKeyboard(mActivity, binding?.edtSearch)
            false
        }

        binding?.layoutSort?.setOnClickListener {
            createSortDialog()
            WeatherApplication.trackingEvent("Click_Sort_Search_Result")
        }

        binding?.btnClearText?.setOnClickListener {
            binding?.edtSearch?.setText("")
        }
    }

    private fun addRecentSearchAndReload() {
        addRecentSearch(binding?.edtSearch?.text.toString())
        recentAdapter?.setData(getRecentSearch())
    }


    private fun addRecentSearch(string: String) {
        try {
            var arrayListRecentSearchs: ArrayList<String>
            val json =
                PreferenceUtil.getInstance(mActivity).getValue(Constant.PrefKey.RECENT_SEARCH, "")
            if (json.isEmpty()) {
                arrayListRecentSearchs = ArrayList()
                arrayListRecentSearchs.add(string)
                PreferenceUtil.getInstance(mActivity)
                    .setValue(Constant.PrefKey.RECENT_SEARCH, Gson().toJson(arrayListRecentSearchs))
            } else {
                arrayListRecentSearchs = Gson().fromJson(
                    json,
                    object : TypeToken<java.util.ArrayList<String?>?>() {}.type
                )
                for (i in arrayListRecentSearchs.indices) {
                    if (string == arrayListRecentSearchs[i]) {
                        arrayListRecentSearchs.removeAt(i)
                        arrayListRecentSearchs.add(string)
                        PreferenceUtil.getInstance(mActivity).setValue(
                            Constant.PrefKey.RECENT_SEARCH,
                            Gson().toJson(arrayListRecentSearchs)
                        )
                        return
                    }
                }
                if (arrayListRecentSearchs.size >= 10) {
                    arrayListRecentSearchs.removeAt(0)
                }
                arrayListRecentSearchs.add(string)
                PreferenceUtil.getInstance(mActivity)
                    .setValue(Constant.PrefKey.RECENT_SEARCH, Gson().toJson(arrayListRecentSearchs))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun getRecentSearch(): ArrayList<String> {
        val arrayListRecentSearchs: ArrayList<String>
        val json = PreferenceUtil.getInstance(context).getValue(Constant.PrefKey.RECENT_SEARCH, "")
        arrayListRecentSearchs = if (json.isEmpty()) {
            ArrayList()
        } else {
            Gson().fromJson(
                json,
                object : TypeToken<java.util.ArrayList<String?>?>() {}.type
            )
        }

        arrayListRecentSearchs.reverse()

        return arrayListRecentSearchs;
    }


    @SuppressLint("CheckResult")
    private fun handleSearch() {
        try {
            RxTextView.afterTextChangeEvents(binding?.edtSearch!!)
                .skipInitialValue()
                .debounce(790, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    try {
                        if (binding?.edtSearch?.text!!.isNotEmpty()) {
                            addRecentSearchAndReload()
                            offset = 0
                            getData()
                            binding?.layoutResult?.toVisible()
                            binding?.layoutHistory?.toGone()
                            Log.e("debug", "1")
                        } else {
                            binding?.layoutResult?.toGone()
                            binding?.layoutHistory?.toVisible()
                            Log.e("debug", "2")
                        }
                    } catch (e: Exception) {
                        Log.e("debug", "3")
                    }
                }
        } catch (e: Exception) {
        }

    }

    private fun getData() {
        if (canLoadMore) {
            try {
                if(offset != 0) {
                    binding?.loadMoreProgressBar?.visibility = View.VISIBLE
                }
            } catch (e: Exception) {
            }
            weatherViewModel.searchDish(
                binding?.edtSearch?.text.toString()
            )
            canLoadMore = false
        }
    }

    override fun setObserver() {
        weatherViewModel.dataSearchResponseLiveData.observe(this, Observer {
            binding?.progressBar?.toGone()
            try {
                binding?.loadMoreProgressBar?.visibility = View.GONE
            } catch (e: Exception) {
            }
            canLoadMore = true
//            if (it.offset == 0) {
//                listImage.clear()
//            }
            listImage.addAll(it.items)
            imageAdapter?.notifyDataSetChanged()
            canLoadMore = true
            offset += it.items.size
        })

        weatherViewModel.requestFail.observe(this) {
            canLoadMore = true
            binding?.progressBar?.toGone()
            try {
                binding?.loadMoreProgressBar?.visibility = View.GONE
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
            WeatherApplication.trackingEvent("Click_Sort_Newest_Search_Result")
        }

        btnPopular.setOnClickListener {
            binding?.tvSortName?.text = "Sort by: MOST POPULAR"
            sort = Constant.SortBy.RATING
            popupWindow1?.dismiss()
            reload()
            WeatherApplication.trackingEvent("Click_Sort_popular_Search_Result")
        }

        btnDownload.setOnClickListener {
            binding?.tvSortName?.text = "Sort by: MOST DOWNLOADED"
            sort = Constant.SortBy.DOWNLOAD
            popupWindow1?.dismiss()
            reload()
            WeatherApplication.trackingEvent("Click_Sort_download_Search_Result")
        }

        btnFavorite.setOnClickListener {
            binding?.tvSortName?.text = "Sort by: MOST FAVORITE"
            sort = Constant.SortBy.FAVORITES
            popupWindow1?.dismiss()
            reload()
            WeatherApplication.trackingEvent("Click_Sort_favorite_Search_Result")
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
}