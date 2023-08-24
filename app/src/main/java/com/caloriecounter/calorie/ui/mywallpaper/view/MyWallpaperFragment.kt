package com.caloriecounter.calorie.ui.mywallpaper.view

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.caloriecounter.calorie.Constant
import com.caloriecounter.calorie.R
import com.caloriecounter.calorie.ads.RewardedAds
import com.caloriecounter.calorie.base.BaseFragment
import com.caloriecounter.calorie.databinding.FragmentMyWallpaperBinding
import com.caloriecounter.calorie.iinterface.DialogState
import com.caloriecounter.calorie.ui.main.adapter.ImageAdapter
import com.caloriecounter.calorie.ui.main.event.CoinChange
import com.caloriecounter.calorie.ui.main.model.image.Image
import com.caloriecounter.calorie.ui.main.view.AskUserGoProDialog
import com.caloriecounter.calorie.ui.main.view.AskUserViewAdsDialog2
import com.caloriecounter.calorie.ui.mywallpaper.viewmodel.MyWallpaperViewModel
import com.caloriecounter.calorie.util.PreferenceUtil
import com.caloriecounter.calorie.util.toGone
import com.caloriecounter.calorie.util.toVisible
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import javax.inject.Inject

@AndroidEntryPoint
class MyWallpaperFragment : BaseFragment<FragmentMyWallpaperBinding?>() {
    private var myWallpaperType = Constant.MyWallpaperType.FAVORITE
    private var imageAdapter: ImageAdapter? = null
    private var listImage: ArrayList<Image> = ArrayList()
    private var myWallpaperViewModel: MyWallpaperViewModel? = null
    @Inject
    lateinit var preferenceUtil: PreferenceUtil

    public fun setMyWallPaperType(myWallpaperType: String) {
        this.myWallpaperType = myWallpaperType
    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_my_wallpaper
    }

    override fun initView() {
        imageAdapter = ImageAdapter(mActivity, listImage, true, true, false)
        binding?.rclContent?.adapter = imageAdapter
    }

    override fun initData() {
        myWallpaperViewModel = MyWallpaperViewModel()
        if(myWallpaperType == Constant.MyWallpaperType.FAVORITE) {
            binding?.tvCateName?.text = "Favorite wallpapers"
            Glide.with(mActivity).load(R.drawable.ic_baseline_favorite_24_2).into(binding?.imgAvatar!!)
            binding?.tvTitle?.text = mActivity.getString(R.string.you_have_not_favorited_any_photos_yet)
            myWallpaperViewModel?.getAllFavoriteImage()
        }else{
            binding?.tvCateName?.text = "Install wallpapers"
            Glide.with(mActivity).load(R.drawable.ic_baseline_access_time_24_2).into(binding?.imgAvatar!!)
            binding?.tvTitle?.text = mActivity.getString(R.string.you_have_not_used_any_photos_as_wallpaper_yet)
            myWallpaperViewModel?.getAllRecentImage()
        }


        binding?.tvCoin?.text = PreferenceUtil.getInstance(mActivity).getValue(Constant.SharePrefKey.COIN, 0).toString()

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
        myWallpaperViewModel?.repos?.observe(this, Observer {
            listImage.addAll(it)
            if(listImage.size > 0){
                binding?.layoutTip?.toGone()
            }else{
                binding?.layoutTip?.toVisible()
            }
            imageAdapter?.notifyDataSetChanged()
        })

        myWallpaperViewModel?.reposRecent?.observe(this, Observer {
            listImage.addAll(it)
            if(listImage.size > 0){
                binding?.layoutTip?.toGone()
            }else{
                binding?.layoutTip?.toVisible()
            }
            imageAdapter?.notifyDataSetChanged()
        })
    }

    override fun getFrame(): Int {
        return R.id.mainFrame
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