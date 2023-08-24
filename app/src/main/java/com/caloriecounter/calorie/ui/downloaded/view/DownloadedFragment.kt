package com.caloriecounter.calorie.ui.downloaded.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.caloriecounter.calorie.Constant

import com.caloriecounter.calorie.ui.downloaded.adapter.ListImageDownloadedAdapter
import com.caloriecounter.calorie.R
import com.caloriecounter.calorie.ads.RewardedAds
import com.caloriecounter.calorie.base.BaseFragment
import com.caloriecounter.calorie.databinding.FragmentDownloadedBinding
import com.caloriecounter.calorie.iinterface.DataChange
import com.caloriecounter.calorie.iinterface.DialogState
import com.caloriecounter.calorie.ui.main.event.CoinChange
import com.caloriecounter.calorie.ui.main.view.AskUserGoProDialog
import com.caloriecounter.calorie.ui.main.view.AskUserViewAdsDialog2
import com.caloriecounter.calorie.ui.newlivewallpaper.DownloadedContent
import com.caloriecounter.calorie.ui.newlivewallpaper.task.LoadAllContentFromFolder
import com.caloriecounter.calorie.util.PreferenceUtil
import com.caloriecounter.calorie.util.toGone
import com.caloriecounter.calorie.util.toVisible
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import javax.inject.Inject

@AndroidEntryPoint
class DownloadedFragment : BaseFragment<FragmentDownloadedBinding?>() {
    private var imageAdapter : ListImageDownloadedAdapter? = null;
    private var listImage = ArrayList<DownloadedContent>()

    @Inject
    lateinit var preferenceUtil: PreferenceUtil
    override fun getLayoutRes(): Int {
        return R.layout.fragment_downloaded
    }

    override fun initView() {}
    override fun initData() {
        imageAdapter = ListImageDownloadedAdapter(mActivity, listImage, DataChange {
            if(it){
                binding?.layoutTip?.visibility = View.VISIBLE
            }else{
                binding?.layoutTip?.visibility = View.GONE
            }
        }, object : ListImageDownloadedAdapter.ClickItem{
            override fun onClickItem(position:Int) {
                var downloadedImageDetailFragment = DownloadedImageDetailFragment()
                downloadedImageDetailFragment.setData(listImage, position)
                addFragment(downloadedImageDetailFragment)
            }

        })
        binding?.rclContent?.run {
            adapter = imageAdapter
            setHasFixedSize(true)
            setItemViewCacheSize(20)
            isDrawingCacheEnabled = true
            drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
        }

        if (ContextCompat.checkSelfPermission(
                mActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                mActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 789)
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 789)
        } else {
            getImage()
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
    override fun setObserver() {}

    public fun getImage() {
        val sdCard = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        val savedPath = sdCard.absolutePath + "/EZT4KWallpaper/"
        val loadAllImageFromFolder = LoadAllContentFromFolder(mActivity.contentResolver)
        loadAllImageFromFolder.setFolderPath(savedPath)
        loadAllImageFromFolder.onLoadDoneListener = object :
            LoadAllContentFromFolder.OnLoadDoneListener {
            override fun onLoadDone(images: List<DownloadedContent>) {
                listImage.clear()
                listImage.addAll(images)

                mActivity.runOnUiThread {
                    imageAdapter!!.notifyDataSetChanged()
                }

            }

            override fun onLoadError() {}
        }
        loadAllImageFromFolder.execute()
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 789) {
            if (grantResults.isNotEmpty()) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getImage()
                } else {
                }
            } else {
            }
        }
    }

    override fun getFrame(): Int {
        return R.id.mainFrame
    }
}