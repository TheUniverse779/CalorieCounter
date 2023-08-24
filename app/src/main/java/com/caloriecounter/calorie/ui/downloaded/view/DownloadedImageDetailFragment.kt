package com.caloriecounter.calorie.ui.downloaded.view

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.caloriecounter.calorie.Constant
import com.caloriecounter.calorie.R
import com.caloriecounter.calorie.WeatherApplication
import com.caloriecounter.calorie.ads.RewardedAds
import com.caloriecounter.calorie.base.BaseFragment
import com.caloriecounter.calorie.databinding.FragmentDetailDownloadedImageBinding
import com.caloriecounter.calorie.iinterface.DialogState
import com.caloriecounter.calorie.iinterface.OnClick
import com.caloriecounter.calorie.ui.downloaded.adapter.SlideDownloadedContentAdapter2
import com.caloriecounter.calorie.ui.main.event.CoinChange
import com.caloriecounter.calorie.ui.main.view.AskUserGoProDialog
import com.caloriecounter.calorie.ui.main.view.AskUserViewAdsDialog2
import com.caloriecounter.calorie.ui.newlivewallpaper.DownloadedContent
import com.caloriecounter.calorie.ui.newlivewallpaper.GLWallpaperService
import com.caloriecounter.calorie.ui.newlivewallpaper.LWApplication
import com.caloriecounter.calorie.ui.newlivewallpaper.MainActivity
import com.caloriecounter.calorie.ui.slideimage.view.OptionAfterDownloadDialog
import com.caloriecounter.calorie.ui.slideimage.view.OptionAfterVideoDownloadDialog
import com.caloriecounter.calorie.ui.slideimage.view.setPreviewBothSide
import com.caloriecounter.calorie.util.GlideBlurTransformation
import com.caloriecounter.calorie.util.PreferenceUtil
import com.caloriecounter.calorie.util.toGone
import com.caloriecounter.calorie.util.toVisible
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject


@AndroidEntryPoint
class DownloadedImageDetailFragment : BaseFragment<FragmentDetailDownloadedImageBinding?>(),
    OnClick {
    private var imageAdapter: SlideDownloadedContentAdapter2? = null
    private var listImage: ArrayList<DownloadedContent>? = null

    private var currentPosition: Int = 0

    private var wallpaperManager: WallpaperManager? = null

    private var progressDialog : ProgressDialog? = null

    @Inject
    lateinit var preferenceUtil: PreferenceUtil

    public fun setData(listImage: ArrayList<DownloadedContent>?, currentPosition: Int) {
        this.listImage = listImage
        this.currentPosition = currentPosition
    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_detail_downloaded_image
    }

    override fun initView() {
        imageAdapter = SlideDownloadedContentAdapter2(mActivity, listImage!!)
        binding?.vpContent?.adapter = imageAdapter
        binding?.vpContent?.setPreviewBothSide(R.dimen._20dp,R.dimen._20dp)
        progressDialog = ProgressDialog(mActivity, ProgressDialog.THEME_HOLO_DARK)


        binding?.vpContent?.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {

                imageAdapter?.setCenterPosition(position)

                Handler().postDelayed(Runnable {

                    imageAdapter?.notifyItemChanged(position)

                    if(listImage!![position].type == 1){
//                        Glide.with(mActivity)
//                            .load(listImage!![position].imageVariations.adapted.url)
//                            .transform(GlideBlurTransformation(mActivity))
//                            .into(binding?.imgBackdrop!!)
                        imageAdapter?.setCenterPosition(position)
                        imageAdapter?.notifyItemChanged(position)
                    }else {
                        Glide.with(mActivity)
                            .load(listImage!![position].image.uri)
                            .transform(GlideBlurTransformation(mActivity))
                            .into(binding?.imgBackdrop!!)
                    }

                }, 300)

            }

        })

        Handler().postDelayed(Runnable { binding?.vpContent?.currentItem = currentPosition }, 100)



    }

    private fun showProgress(message: String){
        progressDialog?.setMessage(message)
        progressDialog?.show()
    }

    override fun initData() {
        wallpaperManager = WallpaperManager.getInstance(mActivity)

        binding?.tvCoin?.text = PreferenceUtil.getInstance(mActivity).getValue(Constant.SharePrefKey.COIN, 0).toString()

    }



    override fun setListener() {
        binding?.btnBack?.setOnClickListener {
            mActivity.onBackPressed()
        }

        binding?.btnDownload?.setOnClickListener {
            if(listImage!![binding?.vpContent?.currentItem!!].type == 0) {
                var optionAfterDownloadDialog = OptionAfterDownloadDialog()
                optionAfterDownloadDialog.setOnClick(this@DownloadedImageDetailFragment, false)
                optionAfterDownloadDialog.show(mActivity.supportFragmentManager, null)
                WeatherApplication.trackingEvent("Click_Set_Image_Downloaded_Image_Detail")
            }else{
                var optionAfterDownloadDialog = OptionAfterVideoDownloadDialog()
                optionAfterDownloadDialog.setOnClick(object : OnClick {
                    override fun onClickToMainScreen() {
                    }

                    override fun onClickToLockScreen() {
                    }

                    override fun onClickToBoth() {
                        LWApplication.testCard = listImage!![binding?.vpContent?.currentItem!!].wallpaperCard
                        // When card is clicked we go to preview mode.
                        // When card is clicked we go to preview mode.
                        LWApplication.setPreviewWallpaperCard(listImage!![binding?.vpContent?.currentItem!!].wallpaperCard)
                        val intent = Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER)
                        intent.putExtra(
                            WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                            ComponentName(mActivity, GLWallpaperService::class.java)
                        )

                        startActivityForResult(intent, PREVIEW_REQUEST_CODE)

                    }

                }, false)
                optionAfterDownloadDialog.show(mActivity.supportFragmentManager, null)

            }
        }


        binding?.btnView?.setOnClickListener {
            var previewDownloadedContentFragment = PreviewDownloadedContentFragment()
            previewDownloadedContentFragment.image = listImage!![binding?.vpContent?.currentItem!!]
            addFragment(previewDownloadedContentFragment)
        }


        binding?.viewCoin?.setOnClickListener {
            if (RewardedAds.isCanShowAds()) {
                if (preferenceUtil.getValue(Constant.SharePrefKey.COUNT_REWARD, 0) >= 3) {
                    var askUserGoProDialog = AskUserGoProDialog();
                    askUserGoProDialog.setDialogState( object : DialogState {
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
                    askUserViewAdsDialog2.setDialogState( object : DialogState {
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


    }

    private val PREVIEW_REQUEST_CODE = 7


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
    }




    inner class ShareImage() : AsyncTask<String?, Int?, String?>() {
        override fun doInBackground(vararg string: String?): String? {
            shareImageUri(listImage!![binding?.vpContent?.currentItem!!].image.uri)
            return null
        }

        override fun onPreExecute() {
            super.onPreExecute()

        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                progressDialog!!.dismiss()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    public fun shareImageUri(uri: Uri) {
        try {
            val builder = VmPolicy.Builder()
            StrictMode.setVmPolicy(builder.build())
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.type = "image/png"
            startActivity(intent)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun getImageUri(linkImage: String?): Uri? {
        return try {
            val url = URL(linkImage)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            val myBitmap = BitmapFactory.decodeStream(input)
            val bytes = ByteArrayOutputStream()
            myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
            var ts = System.currentTimeMillis().toString()
            val path = MediaStore.Images.Media.insertImage(mActivity.contentResolver, myBitmap, ts+"Title", ts)
            Uri.parse(path)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun setObserver() {
    }

    @SuppressLint("StaticFieldLeak")
    override fun onClickToLockScreen() {
        showProgress(mActivity.getString(R.string.setting_wallpaper))
        object : AsyncTask<Void?, Void?, Void?>() {


            override fun onPostExecute(aVoid: Void?) {
                super.onPostExecute(aVoid)
                progressDialog?.dismiss()
                Toast.makeText(mActivity, mActivity.getString(R.string.set_image_as_lock_screen_successfully), Toast.LENGTH_LONG).show()
            }

            override fun doInBackground(vararg p0: Void?): Void? {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        wallpaperManager!!.setBitmap(getBitmapFromURI(listImage!![binding?.vpContent?.currentItem!!].image.uri), null, true, WallpaperManager.FLAG_LOCK)
                    } else {
                        wallpaperManager!!.setBitmap(getBitmapFromURI(listImage!![binding?.vpContent?.currentItem!!].image.uri))
                    }

                } catch (e: java.lang.Exception) {
                }
                return null
            }

        }.execute()
    }

    @SuppressLint("StaticFieldLeak")
    override fun onClickToBoth() {
        showProgress(mActivity.getString(R.string.setting_wallpaper))
        object : AsyncTask<Void?, Void?, Void?>() {


            override fun onPostExecute(aVoid: Void?) {
                super.onPostExecute(aVoid)
                progressDialog?.dismiss()
                Toast.makeText(mActivity, mActivity.getString(R.string.set_as_both_success), Toast.LENGTH_LONG).show()
            }

            override fun doInBackground(vararg p0: Void?): Void? {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        wallpaperManager!!.setBitmap(getBitmapFromURI(listImage!![binding?.vpContent?.currentItem!!].image.uri), null, true, WallpaperManager.FLAG_SYSTEM)
                    } else {
                        wallpaperManager!!.setBitmap(getBitmapFromURI(listImage!![binding?.vpContent?.currentItem!!].image.uri))
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        wallpaperManager!!.setBitmap(getBitmapFromURI(listImage!![binding?.vpContent?.currentItem!!].image.uri), null, true, WallpaperManager.FLAG_LOCK)
                    } else {
                        wallpaperManager!!.setBitmap(getBitmapFromURI(listImage!![binding?.vpContent?.currentItem!!].image.uri))
                    }
                } catch (e: java.lang.Exception) {
                    Log.e("", "")
                }
                return null
            }

        }.execute()
    }

    @SuppressLint("StaticFieldLeak")
    override fun onClickToMainScreen() {
        showProgress(mActivity.getString(R.string.setting_wallpaper))
        object : AsyncTask<Void?, Void?, Void?>() {


            override fun onPostExecute(aVoid: Void?) {
                super.onPostExecute(aVoid)
                progressDialog?.dismiss()
                Toast.makeText(mActivity, mActivity.getString(R.string.set_image_as_main_screen_success), Toast.LENGTH_LONG).show()
            }

            override fun doInBackground(vararg p0: Void?): Void? {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        wallpaperManager!!.setBitmap(getBitmapFromURI(listImage!![binding?.vpContent?.currentItem!!].image.uri), null, true, WallpaperManager.FLAG_SYSTEM)
                    } else {
                        wallpaperManager!!.setBitmap(getBitmapFromURI(listImage!![binding?.vpContent?.currentItem!!].image.uri))
                    }

                } catch (e: Exception) {
                    Log.e("", "");
                }
                return null
            }

        }.execute()
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
        }
    }

    private fun getBitmapFromURI(uri : Uri) : Bitmap{
        return MediaStore.Images.Media.getBitmap(mActivity.contentResolver, uri)
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