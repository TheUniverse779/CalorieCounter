package com.caloriecounter.calorie.ui.similar

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.app.WallpaperManager
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import com.caloriecounter.calorie.Constant
import com.caloriecounter.calorie.R
import com.caloriecounter.calorie.WeatherApplication
import com.caloriecounter.calorie.ads.Callback
import com.caloriecounter.calorie.ads.RewardedAds
import com.caloriecounter.calorie.base.BaseFragment
import com.caloriecounter.calorie.databinding.FragmentDetailImageBinding
import com.caloriecounter.calorie.iinterface.OnClick
import com.caloriecounter.calorie.model.Favorite
import com.caloriecounter.calorie.model.Recent
import com.caloriecounter.calorie.ui.main.model.image.Image
import com.caloriecounter.calorie.ui.main.viewmodel.SearchViewModel
import com.caloriecounter.calorie.ui.main.viewmodel.WeatherViewModel
import com.caloriecounter.calorie.ui.slideimage.adapter.SimilarImageAdapter
import com.caloriecounter.calorie.ui.slideimage.adapter.SlideImageAdapter
import com.caloriecounter.calorie.ui.slideimage.view.*
import com.caloriecounter.calorie.util.PreferenceUtil
import com.caloriecounter.calorie.util.toGone
import com.caloriecounter.calorie.util.toVisible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_detail_image.*
import kotlinx.android.synthetic.main.layout_bottom_sheet_similar.*
import javax.inject.Inject

@AndroidEntryPoint
class ImageSimilarDetailFragment : BaseFragment<FragmentDetailImageBinding?>(), OnClick {
    private val mainViewModel : WeatherViewModel by viewModels()
    private val searchViewModel: SearchViewModel by viewModels()
    private var imageAdapter: SlideImageAdapter? = null
    private var listImage: ArrayList<Image>? = null

    private var catId: String? = null
    private var typeToGetImage: Array<String>? = null
    private var sortBy: String? = null
    private var presentImageType: String? = null
    private var keyword: String? = null
    private var isFromNotification: Boolean = false
    private var ids : Array<String>? = null

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
    private var listImageSimilar =  ArrayList<Image>()
    private var canLoadMoreSimilar: Boolean = true
    private var similarImageAdapter : SimilarImageAdapter? = null


    private var sheetBehavior: BottomSheetBehavior<*>? = null

    @Inject
    lateinit var preferenceUtil : PreferenceUtil

    public fun setData( listImage: ArrayList<Image>?, currentPosition: Int, imageId : String?) {
        this.listImage = listImage
        this.currentPosition = currentPosition
        this.offset = listImage?.size ?: 0
        this.imageIdForGetSimilar = imageId
    }



    override fun getLayoutRes(): Int {
        return R.layout.fragment_detail_image
    }

    override fun initView() {
//        imageAdapter = SlideImageAdapter(mActivity, listImage, null) {
//            if(binding?.layoutButton?.visibility == View.GONE){
//                binding?.layoutButton?.toVisible()
//                binding?.layoutTime?.toVisible()
//                binding?.layoutToolbar?.toVisible()
//            }else{
//                binding?.layoutButton?.toGone()
//                binding?.layoutTime?.toGone()
//                binding?.layoutToolbar?.toGone()
//            }
//        }
        vpContent.adapter = imageAdapter
        vpContent.currentItem = currentPosition
        checkImageIsFavorite(currentPosition)

        progressDialog = ProgressDialog(mActivity, ProgressDialog.THEME_HOLO_DARK)


        similarImageAdapter = SimilarImageAdapter(mActivity, listImageSimilar)
        rclSimilar.adapter = similarImageAdapter


        rclSimilar.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE && !recyclerView.canScrollVertically(1)) {
                    getDataSimilar()

                }
            }
        })

        sheetBehavior = BottomSheetBehavior.from(bottomSheet)


        if (preferenceUtil.getValue(Constant.SharePrefKey.SHOW_REWARD, "no").equals("yes")) {
//            if (listImage?.get(vpContent.currentItem)?.content_type.equals("private")) {
            RewardedAds.initRewardAds(mActivity, Callback {
                isRewardLoaded = true
            })
//            }
        }
    }

    private var isRewardLoaded = false;

    private fun showProgress(message: String) {
        progressDialog?.setMessage(message)
        progressDialog?.show()
    }

    override fun initData() {
        wallpaperManager = WallpaperManager.getInstance(mActivity)
        getData()

        try {
            imageIdForGetSimilar = listImage!![currentPosition].id
            keywordForGetSimilar = listImage!![currentPosition].tags[0]
            getDataSimilar()
            try {
                Log.e("iddddddddddddddd", imageIdForGetSimilar!!)
            } catch (e: Exception) {
            }
        } catch (e: Exception) {
        }

    }


    override fun setListener() {
//        btnSimilar.setOnClickListener {
//            sheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
//        }

        btnPullUp.setOnClickListener {
            sheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
        }

//        imgCloseSheet.setOnClickListener {
//            layoutCloseSimilar.visibility = View.GONE
//            sheetBehavior?.state = BottomSheetBehavior.STATE_COLLAPSED
//        }


        btnBack.setOnClickListener {
            mActivity.onBackPressed()
        }

//        btnSetImage.setOnClickListener {
//            try {
//                try {
//                    WeatherApplication.trackingEvent("Click_Set_Image_Detail_Image_"+listImage?.get(vpContent.currentItem)?.content_type)
//                } catch (e: Exception) {
//                }
//
//
//                if (preferenceUtil.getValue(Constant.SharePrefKey.SHOW_REWARD, "no").equals("yes")) {
//                    if (listImage?.get(vpContent.currentItem)?.content_type.equals("private")) {
//                        if (isRewardLoaded) {
//                            var askUserViewAdsDialog = AskUserViewAdsDialog()
//                            askUserViewAdsDialog.setOnClick {
//                                RewardedAds.showAdsBreak(mActivity, Callback {
//                                    var optionDialog = InstallDialog();
//                                    optionDialog.setOnClick(this)
//                                    optionDialog.show(childFragmentManager, "");
//                                })
//                            }
//                            askUserViewAdsDialog.show(childFragmentManager, "")
//                        } else {
//                            var optionDialog = InstallDialog();
//                            optionDialog.setOnClick(this)
//                            optionDialog.show(childFragmentManager, "");
//                        }
//                    } else {
//                        var optionDialog = InstallDialog();
//                        optionDialog.setOnClick(this)
//                        optionDialog.show(childFragmentManager, "");
//                    }
//                }else{
//                    var optionDialog = InstallDialog();
//                    optionDialog.setOnClick(this)
//                    optionDialog.show(childFragmentManager, "");
//                }
//
//
//            } catch (e: Exception) {
//            }
//        }

        btnDownload.setOnClickListener {
            try {
                WeatherApplication.trackingEvent("Click_Download_Detail_Image_"+listImage?.get(vpContent.currentItem)?.content_type)
            } catch (e: Exception) {
            }
            if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 234)
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 234)
            } else {
                showDownloadDialog()
            }

        }

        vpContent.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
                checkImageIsFavorite(position)
                if (position == (listImage?.size?.minus(1))) {
                    getData()
                }



                imageIdForGetSimilar = listImage!![position].id
                keywordForGetSimilar = listImage!![position].tags[0]
                offsetSimilar = 0
                getDataSimilar()
                try {
                    WeatherApplication.trackingEvent("Change_Slide_Detail_Image_"+listImage?.get(vpContent.currentItem)?.content_type)
                } catch (e: Exception) {
                }


            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })


        btnFavorite.setOnClickListener {
            var image = listImage!![vpContent.currentItem]
            var favorite = Gson().fromJson(Gson().toJson(image), Favorite::class.java)
            WeatherApplication.get().database.foodDao().insertFavorite(favorite)
            favoriteImage()
            try {
                WeatherApplication.trackingEvent("Click_Favorite_Detail_Image_"+listImage?.get(vpContent.currentItem)?.content_type)
            } catch (e: Exception) {
            }


        }

        btnUnFavorite.setOnClickListener {
            var image = listImage!![vpContent.currentItem]
            var favorite = Gson().fromJson(Gson().toJson(image), Favorite::class.java)
            WeatherApplication.get().database.foodDao().deleteFavorite(favorite)
            unFavoriteImage()
            try {
                WeatherApplication.trackingEvent("Click_UnFavorite_Detail_Image_"+listImage?.get(vpContent.currentItem)?.content_type)
            } catch (e: Exception) {
            }
        }

//        btnShare.setOnClickListener {
//            WeatherApplication.trackingEvent("Click_Share_Detail_Image")
//            if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
//                ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 123)
//                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 123)
//            } else {
//                showProgress(mActivity.getString(R.string.preparing_photo_to_share))
//                ShareImage().execute()
//            }
//        }
//
//        btnInfo.setOnClickListener {
//            var informationDialog = InformationDialog()
//            informationDialog.setImage(listImage?.get(vpContent.currentItem))
//            informationDialog.show(childFragmentManager, "")
//            WeatherApplication.trackingEvent("Click_Info")
//        }



//        sheetBehavior?.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){
//            override fun onStateChanged(bottomSheet: View, newState: Int) {
//                if(newState == BottomSheetBehavior.STATE_EXPANDED){
//                    layoutCloseSimilar.visibility = View.VISIBLE
//                }else{
//                    layoutCloseSimilar.visibility = View.INVISIBLE
//                }
//            }
//
//            override fun onSlide(bottomSheet: View, slideOffset: Float) {
//            }
//
//        })

    }

    private fun checkImageIsFavorite(position: Int) {
        try {
            var favorite = WeatherApplication.get().database.foodDao().getFavoriteFromId(listImage!![position].id)
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
            download(it.url, listImage?.get(vpContent.currentItem)?.id + " - (" + it.resolution.width + " x " + it.resolution.height + ").jpg")
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
                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        progressDialog?.dismiss()
                        var ts = System.currentTimeMillis().toString()
                        val path = MediaStore.Images.Media.insertImage(mActivity.contentResolver, resource, ts+"Title", ts)
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

//            val url = URL(linkImage)
//            val connection = url.openConnection() as HttpURLConnection
//            connection.doInput = true
//            connection.connect()
//            val input = connection.inputStream
//            val myBitmap = BitmapFactory.decodeStream(input)
//            val bytes = ByteArrayOutputStream()
//            myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
//            val path = MediaStore.Images.Media.insertImage(mActivity.contentResolver, myBitmap, "Title", null)
//            Uri.parse(path)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun getData() {
        if (presentImageType == Constant.PresentImageType.SEARCH) {
            if (canLoadMore && loadMoreAble) {
                searchViewModel?.search(
                    catId,
                    sortBy,
                    typeToGetImage,
                    "30",
                    offset,
                    keyword)
            }
        } else {
            if(isFromNotification){
//                mainViewModel?.getImagesByIds(
//                    Constant.ScreenSize.WIDTH,
//                    Constant.ScreenSize.HEIGHT,
//                    ids
//                )
            }else {
                if (canLoadMore && loadMoreAble) {
                    mainViewModel?.getImagesByCatId(
                        catId,
                        sortBy,
                        typeToGetImage,
                        null,
                        "30",
                        offset
                    )
                }
            }
        }
    }

    private var imageIdForGetSimilar : String? = null
    private var keywordForGetSimilar : String? = null
    private fun getDataSimilar() {
        if (canLoadMoreSimilar) {
//            mainViewModel?.getSimilar( null, Constant.SortBy.DOWNLOAD, null, "30", offsetSimilar, keywordForGetSimilar)
        }
    }

    override fun setObserver() {
        mainViewModel?.dataResponseLiveData?.observe(this, Observer {
            if (it.offset == 0) {
                listImage?.clear()
            }
            listImage?.addAll(it.items)
            imageAdapter?.notifyDataSetChanged()
            canLoadMore = true
            offset += it.items.size
        })


//        mainViewModel?.failRepos?.observe(this, Observer {
//            canLoadMore = true
//        })


        searchViewModel?.dataResponseLiveData?.observe(this, Observer {
            if (it.offset == 0) {
                listImage?.clear()
            }
            listImage?.addAll(it.items)
            imageAdapter?.notifyDataSetChanged()
            canLoadMore = true
            offset += it.items.size
        })


//        searchViewModel?.failRepos?.observe(this, Observer {
//            canLoadMore = true
//        })
//
        mainViewModel?.similarLiveData?.observe(this, Observer {
            if (it.offset == 0) {
                listImageSimilar.clear()
            }
            listImageSimilar.addAll(it.items)
            similarImageAdapter?.notifyDataSetChanged()
            canLoadMoreSimilar = true
            offsetSimilar += it.items.size
        })
//
//        mainViewModel?.specificRepos?.observe(this, Observer {
//            if (it.offset == 0) {
//                listImage?.clear()
//            }
//            listImage?.addAll(it.items)
//            imageAdapter?.notifyDataSetChanged()
//            canLoadMore = true
//            offset += it.items.size
//        })


    }

    @SuppressLint("StaticFieldLeak")
    private fun setLockScreen(bitmap: Bitmap){
        object : AsyncTask<Void?, Void?, Void?>() {


            override fun onPostExecute(aVoid: Void?) {
                super.onPostExecute(aVoid)
                try {
                    progressDialog?.dismiss()
                    Toast.makeText(mActivity, mActivity.getString(R.string.set_image_as_lock_screen_successfully), Toast.LENGTH_LONG).show()
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
    private fun setBothScreen(bitmap: Bitmap){
        object : AsyncTask<Void?, Void?, Void?>() {


            override fun onPostExecute(aVoid: Void?) {
                super.onPostExecute(aVoid)
                try {
                    progressDialog?.dismiss()
                    Toast.makeText(mActivity, mActivity.getString(R.string.set_as_both_success), Toast.LENGTH_LONG).show()
                } catch (e: Exception) {
                }
            }

            override fun doInBackground(vararg p0: Void?): Void? {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        wallpaperManager!!.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM)
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
    private fun setHomeScreen(bitmap: Bitmap){
        object : AsyncTask<Void?, Void?, Void?>() {


            override fun onPostExecute(aVoid: Void?) {
                super.onPostExecute(aVoid)
                try {
                    progressDialog?.dismiss()
                    Toast.makeText(mActivity, mActivity.getString(R.string.set_image_as_main_screen_success), Toast.LENGTH_LONG).show()
                } catch (e: Exception) {
                }
            }

            override fun doInBackground(vararg p0: Void?): Void? {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        wallpaperManager!!.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM)
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
        getBitmapFromImageLink(listImage!![vpContent.currentItem].variations.adapted.url, LOCK_SCREEN_TYPE)
    }


    override fun onClickToBoth() {
        getBitmapFromImageLink(listImage!![vpContent.currentItem].variations.adapted.url, BOTH_TYPE)
    }


    override fun onClickToMainScreen() {
        getBitmapFromImageLink(listImage!![vpContent.currentItem].variations.adapted.url, HOME_SCREEN_TYPE)
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


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
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
                    showDownloadDialog()
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
                val notifiDialog = NotifiDialog.newInstance(
                    mActivity.getString(R.string.download_success),
                    mActivity.getString(R.string.ok),
                    NotifiDialog.ClickButton { })
                notifiDialog.show(childFragmentManager, "")
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
}