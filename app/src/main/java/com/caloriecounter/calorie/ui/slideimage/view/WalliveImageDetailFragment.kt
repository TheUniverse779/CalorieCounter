package com.caloriecounter.calorie.ui.slideimage.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import com.caloriecounter.calorie.Constant
import com.caloriecounter.calorie.R
import com.caloriecounter.calorie.WeatherApplication
import com.caloriecounter.calorie.ads.InterAds
import com.caloriecounter.calorie.ads.Prefs
import com.caloriecounter.calorie.ads.RewardedAds
import com.caloriecounter.calorie.base.BaseFragment
import com.caloriecounter.calorie.databinding.FragmentDetailImageBinding
import com.caloriecounter.calorie.databinding.FragmentDetailImageWalliveBinding
import com.caloriecounter.calorie.iinterface.DialogState
import com.caloriecounter.calorie.iinterface.OnClick
import com.caloriecounter.calorie.model.Favorite
import com.caloriecounter.calorie.model.Recent
import com.caloriecounter.calorie.ui.livewallpaper.view.LiveDownloadingDialog
import com.caloriecounter.calorie.ui.main.event.CoinChange
import com.caloriecounter.calorie.ui.main.model.image.Image
import com.caloriecounter.calorie.ui.main.view.AskUserGoProDialog
import com.caloriecounter.calorie.ui.main.view.AskUserViewAdsDialog2
import com.caloriecounter.calorie.ui.main.viewmodel.SearchViewModel
import com.caloriecounter.calorie.ui.main.viewmodel.WeatherViewModel
import com.caloriecounter.calorie.ui.newlivewallpaper.GLWallpaperService
import com.caloriecounter.calorie.ui.newlivewallpaper.LWApplication
import com.caloriecounter.calorie.ui.newlivewallpaper.WallpaperCard
import com.caloriecounter.calorie.ui.premium.PremiumActivity
import com.caloriecounter.calorie.ui.preview.PreviewActivity
import com.caloriecounter.calorie.ui.preview.PreviewVideoActivity
import com.caloriecounter.calorie.ui.slideimage.adapter.SimilarImageAdapter
import com.caloriecounter.calorie.ui.slideimage.adapter.SlideImageAdapter
import com.caloriecounter.calorie.ui.slideimage.adapter.SlideImageAdapter2
import com.caloriecounter.calorie.ui.slideimage.event.OnDialogDismiss
import com.caloriecounter.calorie.util.AppUtil
import com.caloriecounter.calorie.util.GlideBlurTransformation
import com.caloriecounter.calorie.util.PreferenceUtil
import com.caloriecounter.calorie.util.toGone
import com.caloriecounter.calorie.util.toVisible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.dialog_exit_app.btn_exit
import kotlinx.android.synthetic.main.fragment_detail_image_wallive.*
import kotlinx.android.synthetic.main.layout_bottom_sheet_similar.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.io.File
import javax.inject.Inject


@AndroidEntryPoint
class WalliveImageDetailFragment : BaseFragment<FragmentDetailImageWalliveBinding?>(), OnClick {
    private val mainViewModel: WeatherViewModel by viewModels()
    private val searchViewModel: SearchViewModel by viewModels()
    private var imageAdapter: SlideImageAdapter2? = null
    private var listImage: ArrayList<Image>? = null

    private var catId: String? = null
    private var typeToGetImage: Array<String>? = null
    private var sortBy: String? = null
    private var presentImageType: String? = null
    private var keyword: String? = null
    private var isFromNotification: Boolean = false
    private var ids: Array<String>? = null

    private var currentPosition: Int = 0
    private var loadMoreAble: Boolean = true

    private var canLoadMore: Boolean = true
    private var offset: Int = 0;


    private var wallpaperManager: WallpaperManager? = null

    private var progressDialog: ProgressDialog? = null


    private val HOME_SCREEN_TYPE: Int = 0
    private val LOCK_SCREEN_TYPE: Int = 1
    private val BOTH_TYPE: Int = 2

    private var offsetSimilar: Int = 0;
    private var listImageSimilar = ArrayList<Image>()
    private var canLoadMoreSimilar: Boolean = true
    private var similarImageAdapter: SimilarImageAdapter? = null


    private var sheetBehavior: BottomSheetBehavior<*>? = null

    @Inject
    lateinit var preferenceUtil: PreferenceUtil

    private var canGetSimilar = false

    private var imageClicked : Image? = null

    private var positionOriginal = 0;

    public fun setData(listImage: ArrayList<Image>?, canGetSimilar : Boolean, imageClicked : Image?) {
        this.listImage = listImage
        this.canGetSimilar = canGetSimilar
        this.imageClicked = imageClicked

        try {
            if (!canGetSimilar){
                for (i in 0 until listImage!!.size){
                    if(imageClicked!!.id == listImage!![i].id){
                        positionOriginal = i
                        break
                    }
                }
            }
        } catch (e: Exception) {
        }
    }


    override fun getLayoutRes(): Int {
        return R.layout.fragment_detail_image_wallive
    }

    override fun initView() {
        imageAdapter = SlideImageAdapter2(mActivity, listImage!!)
        vpContent.adapter = imageAdapter
        vpContent.currentItem = positionOriginal
        binding?.vpContent?.setPreviewBothSide(R.dimen._20dp,R.dimen._20dp)
        progressDialog = ProgressDialog(mActivity, ProgressDialog.THEME_HOLO_DARK)
    }

    private var isRewardLoaded = false;
    private var clickDismissDialog = false;
    private fun showProgress(message: String) {
        clickDismissDialog = false
        progressDialog?.setMessage(message)
        progressDialog?.show()
        progressDialog?.setOnDismissListener {
            clickDismissDialog = true
            EventBus.getDefault().post(OnDialogDismiss())
        }
    }

    override fun initData() {
        wallpaperManager = WallpaperManager.getInstance(mActivity)
        getData()

        binding?.tvCoin?.text = PreferenceUtil.getInstance(mActivity).getValue(Constant.SharePrefKey.COIN, 0).toString()


    }


    override fun setListener() {

        binding?.viewCoin?.setOnClickListener {
            if (RewardedAds.isCanShowAds()) {
                if (preferenceUtil.getValue(Constant.SharePrefKey.COUNT_REWARD, 0) >= 3) {
                    var askUserGoProDialog = AskUserGoProDialog();
                    askUserGoProDialog.setDialogState( object : DialogState{
                        override fun onDialogShow() {
                            binding?.blurFull2?.toVisible()
                        }

                        override fun onDialogDismiss() {
                            binding?.blurFull2?.toGone()
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
                    askUserViewAdsDialog2.setDialogState( object : DialogState{
                        override fun onDialogShow() {
                            binding?.blurFull2?.toVisible()
                        }

                        override fun onDialogDismiss() {
                            binding?.blurFull2?.toGone()
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


        binding?.btnView?.setOnClickListener {
            if (listImage!![binding?.vpContent?.currentItem!!].videoVariations != null) {
                PreviewVideoActivity.startScreen(
                    mActivity,
                    listImage?.get(binding?.vpContent?.currentItem!!)
                )
            } else {
                PreviewActivity.startScreen(
                    mActivity,
                    listImage?.get(vpContent.currentItem)?.variations?.adapted?.url!!
                )
            }
        }

        binding?.blurFull2?.setOnClickListener {
            binding?.blurFull2?.toGone()
        }



        btnBack.setOnClickListener {
            mActivity.onBackPressed()
        }

        btnDownload.setOnClickListener {
            try {
                WeatherApplication.trackingEvent(
                    "Click_Download_Detail_Image_" + listImage?.get(
                        vpContent.currentItem
                    )?.content_type
                )
            } catch (e: Exception) {
            }

            try {
                mainViewModel.postAction(Constant.Action.DOWNLOAD, listImage?.get(vpContent.currentItem)?.id, null)
            } catch (e: Exception) {
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
                if(listImage!![binding?.vpContent?.currentItem!!].videoVariations != null){
                    requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 456)
                    requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 456)
                }else {
                    requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 234)
                    requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 234)
                }
            } else {
                try {
                    if(listImage!![binding?.vpContent?.currentItem!!].videoVariations != null){
                        clickDownloadVideo()
                    }else{
                        clickDownloadImage()
                    }

                } catch (e: Exception) {
                }

//                }


            }

        }

        binding?.vpContent?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                InterAds.showAdsBreak(mActivity) {
                    checkImageIsFavorite(position)
//                if (position == (listImage?.size?.minus(1))) {
//                    getData()
//                }


                    imageAdapter?.setCenterPosition(position)

                    Handler().postDelayed(Runnable {

                        imageAdapter?.notifyItemChanged(position)

                        if (listImage!![position].videoVariations != null) {
                            Glide.with(mActivity)
                                .load(listImage!![position].imageVariations.adapted.url)
                                .transform(GlideBlurTransformation(mActivity))
                                .into(binding?.imgBackdrop!!)
                        } else {
                            Glide.with(mActivity)
                                .load(listImage!![position].variations.adapted.url)
                                .transform(GlideBlurTransformation(mActivity))
                                .into(binding?.imgBackdrop!!)
                        }

                    }, 300)
                }
            }

        })


        btnFavorite.setOnClickListener {
            var image = listImage!![vpContent.currentItem]
            var favorite = Gson().fromJson(Gson().toJson(image), Favorite::class.java)
            WeatherApplication.get().database.foodDao().insertFavorite(favorite)
            favoriteImage()
            try {
                WeatherApplication.trackingEvent(
                    "Click_Favorite_Detail_Image_" + listImage?.get(
                        vpContent.currentItem
                    )?.content_type
                )
            } catch (e: Exception) {
            }


            try {
                mainViewModel.postAction(Constant.Action.FAVORITE, listImage?.get(vpContent.currentItem)?.id, null)
            } catch (e: Exception) {
            }


        }

        btnUnFavorite.setOnClickListener {
            var image = listImage!![vpContent.currentItem]
            var favorite = Gson().fromJson(Gson().toJson(image), Favorite::class.java)
            WeatherApplication.get().database.foodDao().deleteFavorite(favorite)
            unFavoriteImage()
            try {
                WeatherApplication.trackingEvent(
                    "Click_UnFavorite_Detail_Image_" + listImage?.get(
                        vpContent.currentItem
                    )?.content_type
                )
            } catch (e: Exception) {
            }
        }

    }


    private fun clickDownloadImage(){
        val isPro = Prefs(WeatherApplication.get()).premium

        if(isPro == 1){
            showDownloadDialog()
        }else {
            var image = listImage!![vpContent.currentItem]
            var showImageDialog =
                ShowImageDialog.newInstance(object : ShowImageDialog.ClickButton {
                    override fun onClickButton() {
                        showDownloadDialog()
                    }

                    override fun onClickViewAd() {
                        RewardedAds.showAdsBreak(mActivity) {
                            showDownloadDialog()
                        }
                    }

                })
            showImageDialog.setImage(image)
            showImageDialog.setDialogState(object : DialogState {
                override fun onDialogShow() {
                    binding?.blurFull2?.toVisible()
                }

                override fun onDialogDismiss() {
                    binding?.blurFull2?.toGone()
                }
            })
            showImageDialog.show(mActivity.supportFragmentManager, null)
        }
    }

    private fun clickDownloadVideo(){
        val isPro = Prefs(WeatherApplication.get()).premium
        if(isPro == 1) {
            downloadLiveWallpaper()
        }else{
            var image = listImage!![vpContent.currentItem]
            var showImageDialog =
                ShowImageDialog.newInstance(object : ShowImageDialog.ClickButton {
                    override fun onClickButton() {
                        downloadLiveWallpaper()
                    }

                    override fun onClickViewAd() {
                        RewardedAds.showAdsBreak(mActivity) {
                            downloadLiveWallpaper()
                        }
                    }

                })
            showImageDialog.setImage(image)
            showImageDialog.setDialogState(object : DialogState {
                override fun onDialogShow() {
                    binding?.blurFull2?.toVisible()
                }

                override fun onDialogDismiss() {
                    binding?.blurFull2?.toGone()
                }
            })
            showImageDialog.show(mActivity.supportFragmentManager, null)
        }
    }


    private fun checkImageIsFavorite(position: Int) {
        try {
            var favorite = WeatherApplication.get().database.foodDao()
                .getFavoriteFromId(listImage!![position].id)
            if (favorite != null) {
                btnFavorite.visibility = View.GONE
                btnUnFavorite.visibility = View.VISIBLE
            } else {
                btnFavorite.visibility = View.VISIBLE
                btnUnFavorite.visibility = View.GONE
            }
        } catch (e: Exception) {
        }
    }

    private fun showDownloadDialog() {
        var optionDialog = OptionDownloadDialog();
        optionDialog.setCallback {
            download(
                it.url,
                listImage?.get(vpContent.currentItem)?.id + " - (" + it.resolution.width + " x " + it.resolution.height + ").jpg"
            )
        }
        optionDialog.setVariation(listImage?.get(vpContent.currentItem)?.variations)
        optionDialog.show(childFragmentManager, "");
    }

    private fun favoriteImage() {
        btnFavorite.visibility = View.GONE
        btnUnFavorite.visibility = View.VISIBLE
    }

    private fun unFavoriteImage() {
        btnFavorite.visibility = View.VISIBLE
        btnUnFavorite.visibility = View.GONE
    }

    inner class ShareImage() : AsyncTask<String?, Int?, String?>() {
        override fun doInBackground(vararg string: String?): String? {
            getImageUri(listImage!![vpContent.currentItem].variations.adapted.url)
            return null
        }

        override fun onPreExecute() {
            super.onPreExecute()

        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

        }
    }

    public fun shareImageUri(uri: Uri) {
        try {
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.type = "image/png"
            startActivity(intent)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun getImageUri(linkImage: String?) {
        try {
            Glide.with(mActivity)
                .asBitmap()
                .load(linkImage)
                .into(object : CustomTarget<Bitmap>() {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        progressDialog?.dismiss()
                        var ts = System.currentTimeMillis().toString()
                        val path = MediaStore.Images.Media.insertImage(
                            mActivity.contentResolver,
                            resource,
                            ts + "Title",
                            ts
                        )
                        shareImageUri(Uri.parse(path))
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {

                    }

                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        try {
                            progressDialog?.dismiss()
                            val notifiDialog = NotifiDialog.newInstance(
                                mActivity.getString(R.string.share_fail),
                                mActivity.getString(R.string.ok),
                                NotifiDialog.ClickButton { })
                            notifiDialog.show(childFragmentManager, "")
                        } catch (e: Exception) {
                        }

                    }

                });

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun getData() {
//        if (presentImageType == Constant.PresentImageType.SEARCH) {
//            if (canLoadMore && loadMoreAble) {
//                searchViewModel?.search(
//                    catId,
//                    sortBy,
//                    typeToGetImage,
//                    "30",
//                    offset,
//                    keyword
//                )
//            }
//        } else {
//            if (isFromNotification) {
////                mainViewModel?.getImagesByIds(
////                    Constant.ScreenSize.WIDTH,
////                    Constant.ScreenSize.HEIGHT,
////                    ids
////                )
//            } else {
//                if (canLoadMore && loadMoreAble) {
//                    mainViewModel?.getImagesByCatId(
//                        catId,
//                        sortBy,
//                        typeToGetImage,
//                        null,
//                        "30",
//                        offset
//                    )
//                }
//            }
//        }

        try {
            if (canGetSimilar) {
                keywordForGetSimilar = listImage!![0].tags[0]
                mainViewModel?.getSimilar(
                    null,
                    Constant.SortBy.DOWNLOAD,
                    null,
                    "60",
                    offsetSimilar,
                    keywordForGetSimilar
                )
            }
        } catch (e: Exception) {
        }
    }

    private var imageIdForGetSimilar: String? = null
    private var keywordForGetSimilar: String? = null
    private fun getDataSimilar() {
        if (canLoadMoreSimilar) {
            mainViewModel?.getSimilar( null, Constant.SortBy.DOWNLOAD, null, "30", offsetSimilar, keywordForGetSimilar)
        }
    }

    override fun setObserver() {
        mainViewModel?.similarLiveData?.observe(this, Observer {
//            if (it.offset == 0) {
//                listImageSimilar.clear()
//            }
//            listImageSimilar.addAll(it.items)
//            similarImageAdapter?.notifyDataSetChanged()
//            canLoadMoreSimilar = true
//            offsetSimilar += it.items.size

            try {
                if(it.items.size >0){
                    for (i in 0 until it.items.size){
                        if(listImage!![0].id == it.items!![i].id){
                            it.items.removeAt(i)
                            break
                        }
                    }
                }

                listImage?.addAll(it.items)
                imageAdapter?.notifyDataSetChanged()
            } catch (e: Exception) {
            }

        })



    }

    @SuppressLint("StaticFieldLeak")
    private fun setLockScreen(bitmap: Bitmap) {
        object : AsyncTask<Void?, Void?, Void?>() {


            override fun onPostExecute(aVoid: Void?) {
                super.onPostExecute(aVoid)
                try {
//                    if(clickDismissDialog) {
//                        progressDialog?.dismiss()
//                        try {
//                            var setImageSuccess = SetImageSuccess.newInstance(mActivity.getString(R.string.set_image_as_lock_screen_successfully), "OK", SetImageSuccess.ClickButton {
//
//                            } )
//                            setImageSuccess.show(mActivity.supportFragmentManager, null)
//                        } catch (e: Exception) {
//                        }
//                    }else{
//                        InstallSuccessActivity.startScreen(mActivity, 1)
//                        progressDialog?.dismiss()
//                    }
                    val isPro = Prefs(WeatherApplication.get()).premium
                    if(isPro == 0) {
                        progressDialog?.dismiss()
                        Toast.makeText(
                            mActivity,
                            mActivity.getString(R.string.set_image_as_lock_screen_successfully),
                            Toast.LENGTH_SHORT
                        ).show()
                        var intent = Intent(mActivity, PremiumActivity::class.java)
                        mActivity.startActivity(intent)
                    }else{
                        progressDialog?.dismiss()
                        Toast.makeText(
                            mActivity,
                            mActivity.getString(R.string.set_image_as_lock_screen_successfully),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: Exception) {
                }
            }

            override fun doInBackground(vararg p0: Void?): Void? {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        wallpaperManager!!.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK)
                    } else {
                        wallpaperManager!!.setBitmap(bitmap)
                    }
                    insertToRecent();
                } catch (e: java.lang.Exception) {
                }
                return null
            }

        }.execute()
    }

    @SuppressLint("StaticFieldLeak")
    private fun setBothScreen(bitmap: Bitmap) {
        object : AsyncTask<Void?, Void?, Void?>() {


            override fun onPostExecute(aVoid: Void?) {
                super.onPostExecute(aVoid)
                try {
//                    if (clickDismissDialog) {
//                        progressDialog?.dismiss()
//                        try {
//                            var setImageSuccess = SetImageSuccess.newInstance(mActivity.getString(R.string.set_as_both_success), "OK", SetImageSuccess.ClickButton {
//
//                            } )
//                            setImageSuccess.show(mActivity.supportFragmentManager, null)
//                        } catch (e: Exception) {
//                        }
//                    } else {
//                        InstallSuccessActivity.startScreen(mActivity, 2)
//                        progressDialog?.dismiss()
//                    }
                    val isPro = Prefs(WeatherApplication.get()).premium
                    if(isPro == 0) {
                        progressDialog?.dismiss()
                        Toast.makeText(
                            mActivity,
                            mActivity.getString(R.string.set_as_both_success),
                            Toast.LENGTH_SHORT
                        ).show()
                        var intent = Intent(mActivity, PremiumActivity::class.java)
                        mActivity.startActivity(intent)
                    }else{
                        progressDialog?.dismiss()
                        Toast.makeText(
                            mActivity,
                            mActivity.getString(R.string.set_as_both_success),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } catch (e: Exception) {
                }
            }

            override fun doInBackground(vararg p0: Void?): Void? {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        wallpaperManager!!.setBitmap(
                            bitmap,
                            null,
                            true,
                            WallpaperManager.FLAG_SYSTEM
                        )
                    } else {
                        wallpaperManager!!.setBitmap(bitmap)
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        wallpaperManager!!.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK)
                    } else {
                        wallpaperManager!!.setBitmap(bitmap)
                    }
                    insertToRecent();
                } catch (e: java.lang.Exception) {
                    Log.e("", "")
                }
                return null
            }

        }.execute()
    }

    @SuppressLint("StaticFieldLeak")
    private fun setHomeScreen(bitmap: Bitmap) {
        object : AsyncTask<Void?, Void?, Void?>() {


            override fun onPostExecute(aVoid: Void?) {
                super.onPostExecute(aVoid)
                try {
//                    if (clickDismissDialog) {
//                        progressDialog?.dismiss()
//                        try {
//                            var setImageSuccess = SetImageSuccess.newInstance(mActivity.getString(R.string.set_image_as_main_screen_success), "OK", SetImageSuccess.ClickButton {
//
//                            } )
//                            setImageSuccess.show(mActivity.supportFragmentManager, null)
//                        } catch (e: Exception) {
//                        }
//                    } else {
//                        InstallSuccessActivity.startScreen(mActivity, 0)
//                        progressDialog?.dismiss()
//                    }
                    val isPro = Prefs(WeatherApplication.get()).premium
                    if(isPro == 0) {
                        Toast.makeText(
                            mActivity,
                            mActivity.getString(R.string.set_image_as_main_screen_success),
                            Toast.LENGTH_SHORT
                        ).show()
                        progressDialog?.dismiss()
                        var intent = Intent(mActivity, PremiumActivity::class.java)
                        mActivity.startActivity(intent)
                    }else{
                        Toast.makeText(
                            mActivity,
                            mActivity.getString(R.string.set_image_as_main_screen_success),
                            Toast.LENGTH_SHORT
                        ).show()
                        progressDialog?.dismiss()
                    }

                } catch (e: Exception) {
                }
            }

            override fun doInBackground(vararg p0: Void?): Void? {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        wallpaperManager!!.setBitmap(
                            bitmap,
                            null,
                            true,
                            WallpaperManager.FLAG_SYSTEM
                        )
                    } else {
                        wallpaperManager!!.setBitmap(bitmap)
                    }
                    insertToRecent();
                } catch (e: Exception) {
                    Log.e("", "");
                }
                return null
            }

        }.execute()
    }


    override fun onClickToLockScreen() {
        getBitmapFromImageLink(
            listImage!![vpContent.currentItem].variations.adapted.url,
            LOCK_SCREEN_TYPE
        )
    }


    override fun onClickToBoth() {
        getBitmapFromImageLink(listImage!![vpContent.currentItem].variations.adapted.url, BOTH_TYPE)
    }


    override fun onClickToMainScreen() {
        getBitmapFromImageLink(
            listImage!![vpContent.currentItem].variations.adapted.url,
            HOME_SCREEN_TYPE
        )
    }


    private fun getBitmapFromImageLink(link: String, type: Int) {
        showProgress(mActivity.getString(R.string.setting_wallpaper))
        Glide.with(mActivity)
            .asBitmap()
            .load(link)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    when (type) {
                        LOCK_SCREEN_TYPE -> {
                            setLockScreen(resource)
                        }
                        HOME_SCREEN_TYPE -> {
                            setHomeScreen(resource)
                        }
                        else -> {
                            setBothScreen(resource)
                        }
                    }
                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }

                override fun onLoadFailed(errorDrawable: Drawable?) {
                    progressDialog?.dismiss()
                    val notifiDialog = NotifiDialog.newInstance(
                        mActivity.getString(R.string.set_wallpaper_error),
                        mActivity.getString(R.string.ok),
                        NotifiDialog.ClickButton {
                            btnDownload.performClick()
                        })
                    notifiDialog.show(childFragmentManager, "")


                }

            });
    }


    private fun insertToRecent() {
        var image = listImage!![vpContent.currentItem]
        var recent = Gson().fromJson(Gson().toJson(image), Recent::class.java)
        WeatherApplication.get().database.foodDao().insertRecent(recent)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 123) {
            if (grantResults.isNotEmpty()) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showProgress(mActivity.getString(R.string.preparing_photo_to_share))
                    ShareImage().execute()
                } else {
                }
            } else {
            }
        } else if (requestCode == 234) {
            if (grantResults.isNotEmpty()) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    showDownloadDialog()
                    clickDownloadImage()
                } else {
                }
            } else {
            }
        }else if(requestCode == 456){
            if (grantResults.isNotEmpty()) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    downloadLiveWallpaper()
                    clickDownloadVideo()
                } else {
                }
            } else {
            }
        }
    }

    private fun download(url: String, name: String) {
        var downloadingDialog = DownloadingDialog()
        downloadingDialog.setUrl(url, name)
        downloadingDialog.setDownloadListener(object : DownloadingDialog.DownloadListener {
            override fun onDownloadComplete() {

                var optionAfterDownloadDialog = OptionAfterDownloadDialog()
                optionAfterDownloadDialog.setOnClick(this@WalliveImageDetailFragment, true)
                optionAfterDownloadDialog.show(mActivity.supportFragmentManager, null)
            }

            override fun onDownloadError() {
                val notifiDialog = NotifiDialog.newInstance(
                    mActivity.getString(R.string.download_fail),
                    mActivity.getString(R.string.ok),
                    NotifiDialog.ClickButton { })
                notifiDialog.show(childFragmentManager, "")
            }

        })
        downloadingDialog.isCancelable = false
        downloadingDialog.show(childFragmentManager, "")
    }


    override fun getFrame(): Int {
        return R.id.mainFrame
    }

    override fun onDestroy() {
        super.onDestroy()
        imageAdapter?.simpleExoplayer?.release()
        EventBus.getDefault().unregister(this)
    }

    // live

    private val PREVIEW_REQUEST_CODE = 7

    private fun goToSetWallpaperSystemScreen(fUri: Uri) {
        if (fUri == null) {
            return
        }
        val downloadDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file_path = downloadDir.absolutePath + "/EZT4KWallpaper"

        var wallpaperCard = WallpaperCard(
            listImage!![binding?.vpContent?.currentItem!!].id + ".mp4",
            file_path + "/" + listImage!![binding?.vpContent?.currentItem!!].id + ".mp4",
            fUri,
            WallpaperCard.Type.EXTERNAL,
            Bitmap.createBitmap(1080, 2340, Bitmap.Config.ARGB_8888)
        )

        LWApplication.setPreviewWallpaperCard(wallpaperCard)

        val intent = Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER)
        intent.putExtra(
            WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
            ComponentName(
                mActivity,
                GLWallpaperService::class.java
            )
        )
        startActivityForResult(intent, PREVIEW_REQUEST_CODE)

//        simpleExoplayer?.stop()
        imageAdapter?.simpleExoplayer?.stop()

    }

    private fun getURI() : Uri {
        val downloadDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val filePathOrigin =
            downloadDir.absolutePath + "/EZT4KWallpaper" + "/" + listImage!![binding?.vpContent?.currentItem!!].id + ".mp4"
        var fileOrigin = File(filePathOrigin)

        var uri = Uri.fromFile(fileOrigin)
        return uri
    }
    private fun downloadLiveWallpaper() {
        var liveDownloadingDialog = LiveDownloadingDialog()
        liveDownloadingDialog.setDownloadListener(object : LiveDownloadingDialog.DownloadListener {
            override fun onDownloadComplete() {
//                setupPlayerWithFullSharp()
                var optionAfterDownloadDialog = OptionAfterVideoDownloadDialog()
                optionAfterDownloadDialog.setOnClick(object : OnClick {
                    override fun onClickToMainScreen() {
                    }

                    override fun onClickToLockScreen() {
                    }

                    override fun onClickToBoth() {
                        goToSetWallpaperSystemScreen(getURI())
                    }

                }, true)
                optionAfterDownloadDialog.show(mActivity.supportFragmentManager, null)
            }

            override fun onDownloadError() {
                try {
                    val notifiDialog = NotifiDialog.newInstance(mActivity.getString(R.string.download_fail), mActivity.getString(R.string.ok), NotifiDialog.ClickButton {  })
                    notifiDialog.show(childFragmentManager, "")
                } catch (e: Exception) {
                }
            }

        })
        liveDownloadingDialog.setUrl(listImage!![binding?.vpContent?.currentItem!!].videoVariations.adapted.url, listImage!![binding?.vpContent?.currentItem!!].id + ".mp4")
        liveDownloadingDialog.isCancelable = false
        liveDownloadingDialog.show(childFragmentManager, "")

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PREVIEW_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                LWApplication.setCurrentWallpaperCard(
                    mActivity, LWApplication.getPreviewWallpaperCard()
                )

                LWApplication.setPreviewWallpaperCard(null)
                Toast.makeText(
                    mActivity,
                    mActivity.getString(R.string.set_live_wallpaper_success),
                    Toast.LENGTH_SHORT
                ).show()
                mActivity.onBackPressed()
            }

        }
//        else if (requestCode == 779) {
//            if (resultCode == Activity.RESULT_OK) {
//                val downloadDir =
//                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//                val file_path = downloadDir.absolutePath + "/EZT4KWallpaper"
//                var uri = Uri.fromFile(File(file_path + "/" + image!!.id + ".mp4"))
//                preparePlayer2(uri, "default")
//            }
//        }
    }


    @Subscribe
    public fun onCoinChange(onCoinChange: CoinChange) {
        binding?.tvCoin?.text = preferenceUtil.getValue(Constant.SharePrefKey.COIN, 0).toString()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

}