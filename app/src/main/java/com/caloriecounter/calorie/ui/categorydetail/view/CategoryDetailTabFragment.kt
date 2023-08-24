package com.caloriecounter.calorie.ui.categorydetail.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.caloriecounter.calorie.Constant
import com.caloriecounter.calorie.R
import com.caloriecounter.calorie.WeatherApplication
import com.caloriecounter.calorie.ads.RewardedAds
import com.caloriecounter.calorie.base.BaseFragment
import com.caloriecounter.calorie.databinding.FragmentCategoryTabDetailBinding
import com.caloriecounter.calorie.iinterface.DialogState
import com.caloriecounter.calorie.ui.main.adapter.ImageAdapter
import com.caloriecounter.calorie.ui.main.event.CoinChange
import com.caloriecounter.calorie.ui.main.model.category.Category
import com.caloriecounter.calorie.ui.main.model.image.Image
import com.caloriecounter.calorie.ui.main.view.AskUserGoProDialog
import com.caloriecounter.calorie.ui.main.view.AskUserViewAdsDialog2
import com.caloriecounter.calorie.ui.main.viewmodel.WeatherViewModel
import com.caloriecounter.calorie.util.PreferenceUtil
import com.caloriecounter.calorie.util.toGone
import com.caloriecounter.calorie.util.toVisible
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import javax.inject.Inject

@AndroidEntryPoint
class CategoryDetailTabFragment : BaseFragment<FragmentCategoryTabDetailBinding?>() {

    private val mainViewModel : WeatherViewModel by viewModels()
    private var imageAdapter : ImageAdapter? = null
    private var listImage: ArrayList<Image> = ArrayList()

    private var catId : String? = null
    private var typeToGetImage: Array<String>? = null
    private var sortBy: String? = null
    private var presentImageType: String? = null

    private var position : Int? = null

    private var canLoadMore : Boolean = true
    private var offset : Int = 0;

    private var category: Category? = null

    @Inject
    lateinit var preferenceUtil: PreferenceUtil


    public fun setData(category : Category?, position : Int?, typeToGetImage: Array<String>?, sortBy: String?, presentImageType: String?){
        this.category = category
        this.position = position
        this.typeToGetImage = typeToGetImage
        this.sortBy = sortBy
        this.presentImageType = presentImageType
    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_category_tab_detail
    }

    override fun initView() {
        binding?.swipeToRefresh?.setOnRefreshListener {
            reload()
            WeatherApplication.trackingEvent("Refresh_Category_detail")
        }
        imageAdapter = ImageAdapter(mActivity, listImage)
        imageAdapter?.setDataPresentImageType(Constant.PresentImageType.CATEGORY, catId, sortBy, typeToGetImage)
        binding?.rclContent?.adapter = imageAdapter
    }
    override fun initData() {
        getData()
        binding?.rclContent?.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE && !recyclerView.canScrollVertically(1)) {
                    getData()
                }
            }
        })

        binding?.tvCateName?.text = category?.title
    }

    private fun getData(){
        binding?.tvCoin?.text = preferenceUtil.getValue(Constant.SharePrefKey.COIN, 0).toString()
        if (canLoadMore){
            try {
                binding?.loadMoreProgressBar?.visibility = View.VISIBLE
            } catch (e: Exception) {
            }
            canLoadMore = false
            mainViewModel?.getImagesByCatId(
                catId,
                sortBy,
                typeToGetImage,
                null,
                "30",
                offset)
            try {
                WeatherApplication.trackingEvent("Load_more_data_Category_detail", "Offset", offset.toString())
            } catch (e: Exception) {
            }
        }
    }


    override fun setListener() {
        binding?.btnBack?.setOnClickListener {
            mActivity.onBackPressed()
        }


        binding?.viewCoin?.setOnClickListener {
            if (RewardedAds.isCanShowAds()) {
                if (preferenceUtil.getValue(Constant.SharePrefKey.COUNT_REWARD, 0) >= 3) {
                    var askUserGoProDialog = AskUserGoProDialog();
                    askUserGoProDialog.setDialogState( object : DialogState {
                        override fun onDialogShow() {
                            binding?.blurFull?.toVisible()
                        }

                        override fun onDialogDismiss() {
                            binding?.blurFull?.toGone()
                        }

                    })
                    askUserGoProDialog.setOnClick {
                        RewardedAds.showAdsBreak(mActivity) {
                            preferenceUtil.setValue(Constant.SharePrefKey.COUNT_REWARD, 0)
                            preferenceUtil.setValue(
                                Constant.SharePrefKey.COIN,
                                preferenceUtil.getValue(Constant.SharePrefKey.COIN, 0) + 1
                            )
                            EventBus.getDefault().post(CoinChange())
                        }
                    }
                    askUserGoProDialog.show(mActivity.supportFragmentManager, null)
                } else {
                    var askUserViewAdsDialog2 = AskUserViewAdsDialog2();
                    askUserViewAdsDialog2.setDialogState( object : DialogState {
                        override fun onDialogShow() {
                            binding?.blurFull?.toVisible()
                        }

                        override fun onDialogDismiss() {
                            binding?.blurFull?.toGone()
                        }

                    })
                    askUserViewAdsDialog2.setOnClick {
                        RewardedAds.showAdsBreak(mActivity) {
                            preferenceUtil.setValue(
                                Constant.SharePrefKey.COUNT_REWARD,
                                preferenceUtil.getValue(
                                    Constant.SharePrefKey.COUNT_REWARD,
                                    0
                                ) + 1
                            )
                            preferenceUtil.setValue(
                                Constant.SharePrefKey.COIN,
                                preferenceUtil.getValue(Constant.SharePrefKey.COIN, 0) + 1
                            )
                            EventBus.getDefault().post(CoinChange())
                        }
                    }
                    askUserViewAdsDialog2.show(mActivity.supportFragmentManager, null)
                }
            } else {
                Toast.makeText(mActivity, "No ads now, please come back later", Toast.LENGTH_SHORT).show()
            }



        }
    }
    override fun setObserver() {
        mainViewModel?.dataResponseLiveData?.observe(this, Observer {

            binding?.progressBar?.toGone()

            try {
                binding?.loadMoreProgressBar?.visibility = View.GONE
            } catch (e: Exception) {
            }

            binding?.swipeToRefresh?.isRefreshing = false
            if(it.offset == 0) {
                listImage.clear()
            }
            listImage.addAll(it.items)
            imageAdapter?.notifyDataSetChanged()
            canLoadMore = true
            offset += it.items.size



//            if(position == 0){
//                EventBus.getDefault().post(OnHaveBackdrop(listImage[0]?.variations?.adapted?.url))
//            }
        })

        mainViewModel.requestFail.observe(this) {
            canLoadMore = true
            binding?.progressBar?.toGone()
            binding?.tvRefresh?.toGone()

            try {
                binding?.loadMoreProgressBar?.visibility = View.GONE
            } catch (e: Exception) {
            }
            binding?.swipeToRefresh?.isRefreshing = false
        }
    }

    override fun getFrame(): Int {
        return R.id.mainFrame
    }

    private fun reload(){
        binding?.progressBar?.toVisible()
        offset = 0
        listImage.clear()
        imageAdapter?.notifyDataSetChanged()
        getData()
    }


    @Subscribe
    public fun onCoinChange(onCoinChange: CoinChange) {
        binding?.tvCoin?.text = preferenceUtil.getValue(Constant.SharePrefKey.COIN, 0).toString()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }


    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }


}