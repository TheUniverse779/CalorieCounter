package com.caloriecounter.calorie.ui.doubledetail.view

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
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.caloriecounter.calorie.R
import com.caloriecounter.calorie.WeatherApplication
import com.caloriecounter.calorie.base.BaseFragment
import com.caloriecounter.calorie.databinding.FragmentDetailDoubleImageBinding
import com.caloriecounter.calorie.iinterface.OnClick
import com.caloriecounter.calorie.ui.doubledetail.adapter.SlideDoubleImageAdapter
import com.caloriecounter.calorie.ui.main.model.doubleimage.DoubleImage
import com.caloriecounter.calorie.ui.main.model.image.Variation
import com.caloriecounter.calorie.ui.slideimage.view.DownloadingDialog
import com.caloriecounter.calorie.ui.slideimage.view.InstallDialog
import com.caloriecounter.calorie.ui.slideimage.view.NotifiDialog
import com.caloriecounter.calorie.ui.slideimage.view.OptionDownloadDialog
import kotlinx.android.synthetic.main.fragment_detail_image.*

class DoubleImageFragment : BaseFragment<FragmentDetailDoubleImageBinding?>() , OnClick {
    private var progressDialog : ProgressDialog? = null
    private var doubleImage : DoubleImage? = null
    private var currentPosition : Int = 0
    private var slideDoubleImageAdapter: SlideDoubleImageAdapter? = null
    private var listDoubleImage = ArrayList<Variation>()
    private var wallpaperManager: WallpaperManager? = null

    private val HOME_SCREEN_TYPE: Int = 0
    private val LOCK_SCREEN_TYPE: Int = 1
    private val BOTH_TYPE: Int = 2

    public fun setData(currentPosition: Int, doubleImage: DoubleImage){
        this.currentPosition = currentPosition
        this.doubleImage = doubleImage


        var lockVariation = doubleImage.lockVariation
        listDoubleImage.add(lockVariation)
        var homeVariation = doubleImage.homeVariation
        listDoubleImage.add(homeVariation)
    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_detail_double_image
    }

    override fun initView() {
        slideDoubleImageAdapter = SlideDoubleImageAdapter(mActivity, listDoubleImage, null)
        vpContent.adapter = slideDoubleImageAdapter
        vpContent.currentItem = currentPosition

        progressDialog = ProgressDialog(mActivity, ProgressDialog.THEME_HOLO_DARK)
    }

    private var clickDismissDialog = false;
    private fun showProgress(message: String) {
        clickDismissDialog = false
        progressDialog?.setMessage(message)
        progressDialog?.show()
        progressDialog?.setOnDismissListener {
            clickDismissDialog = true
        }
    }
    override fun initData() {
        wallpaperManager = WallpaperManager.getInstance(mActivity)
    }
    override fun setListener() {
        btnBack.setOnClickListener {
            mActivity.onBackPressed()
        }

//        btnSetImage.setOnClickListener {
//            WeatherApplication.trackingEvent("Click_Set_Image_Double")
//            var optionDialog = InstallDialog();
//            optionDialog.setOnClick(this)
//            optionDialog.show(childFragmentManager, "");
//        }

        btnDownload.setOnClickListener {
            if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 234)
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 234)
            } else {
                showDownloadDialog()
            }
            WeatherApplication.trackingEvent("Click_Download_Double")


        }

        vpContent.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {
            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })



//        btnShare.setOnClickListener {
//            if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
//                ContextCompat.checkSelfPermission(mActivity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 123)
//                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 123)
//            } else {
//                showProgress(mActivity.getString(R.string.preparing_photo_to_share))
//                ShareImage().execute()
//            }
//            WeatherApplication.trackingEvent("Click_Share_Double")
//
//        }


    }

    inner class ShareImage() : AsyncTask<String?, Int?, String?>() {
        override fun doInBackground(vararg string: String?): String? {
            getImageUri(listDoubleImage!![vpContent.currentItem].adapted.url)
            return null
        }

        override fun onPreExecute() {
            super.onPreExecute()

        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

        }
    }

    private fun showDownloadDialog() {
        var optionDialog = OptionDownloadDialog();
        optionDialog.setCallback {
            download(it.url, doubleImage?.id+"-"+vpContent.currentItem + " - (" + it.resolution.width + " x " + it.resolution.height + ").jpg")
        }
        optionDialog.setVariation(listDoubleImage?.get(vpContent.currentItem))
        optionDialog.show(childFragmentManager, "");
    }

    private fun download(url: String, name: String) {
        var downloadingDialog = DownloadingDialog()
        downloadingDialog.setUrl(url, name)
        downloadingDialog.setDownloadListener(object : DownloadingDialog.DownloadListener{
            override fun onDownloadComplete() {
                val notifiDialog = NotifiDialog.newInstance(mActivity.getString(R.string.download_success), mActivity.getString(R.string.ok), NotifiDialog.ClickButton {  })
                notifiDialog.show(childFragmentManager, "")
            }

            override fun onDownloadError() {
                val notifiDialog = NotifiDialog.newInstance(mActivity.getString(R.string.download_fail), mActivity.getString(R.string.ok), NotifiDialog.ClickButton {  })
                notifiDialog.show(childFragmentManager, "")
            }

        })
        downloadingDialog.isCancelable = false
        downloadingDialog.show(childFragmentManager, "")
    }
    override fun setObserver() {}

    override fun onClickToLockScreen() {
        getBitmapFromImageLink(listDoubleImage!![vpContent.currentItem].adapted.url, LOCK_SCREEN_TYPE)
    }


    override fun onClickToBoth() {
        getBitmapFromImageLink(listDoubleImage!![vpContent.currentItem].adapted.url, BOTH_TYPE)
    }


    override fun onClickToMainScreen() {
        getBitmapFromImageLink(listDoubleImage!![vpContent.currentItem].adapted.url, HOME_SCREEN_TYPE)
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
                    val notifiDialog = NotifiDialog.newInstance(mActivity.getString(R.string.set_wallpaper_error), mActivity.getString(R.string.ok), NotifiDialog.ClickButton {
                        btnDownload.performClick()
                    })
                    notifiDialog.show(childFragmentManager, "")

                }

            });
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
                } catch (e: Exception) {
                    Log.e("", "");
                }
                return null
            }

        }.execute()
    }

    public fun shareImageUri(uri: Uri) {
        try {
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.type = "image/png"
            startActivity(intent)
        } catch (e: Exception) {
            try {
            } catch (e: Exception) {
            }
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
                        progressDialog?.dismiss()
                        val notifiDialog = NotifiDialog.newInstance(mActivity.getString(R.string.share_fail), mActivity.getString(R.string.ok), NotifiDialog.ClickButton {  })
                        notifiDialog.show(childFragmentManager, "")

                        try {
                        } catch (e: Exception) {
                        }
                    }

                });
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
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

    override fun getFrame(): Int {
        return R.id.mainFrame
    }
}