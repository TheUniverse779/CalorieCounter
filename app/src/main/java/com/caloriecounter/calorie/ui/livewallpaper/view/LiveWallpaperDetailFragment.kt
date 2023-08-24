package com.caloriecounter.calorie.ui.livewallpaper.view

import android.Manifest
import android.app.Activity
import android.app.WallpaperManager
import android.content.ComponentName
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.net.Uri
import android.os.Environment
import android.os.StrictMode
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import com.caloriecounter.calorie.ui.livewallpaper.service.keySharedPrefVideo
import com.caloriecounter.calorie.ui.livewallpaper.service.keyType
import com.caloriecounter.calorie.ui.livewallpaper.service.keyVideo
import com.caloriecounter.calorie.Constant
import com.caloriecounter.calorie.R
import com.caloriecounter.calorie.base.BaseFragment
import com.caloriecounter.calorie.databinding.FragmentDetailLiveWallpaperBinding
import com.caloriecounter.calorie.ui.main.model.image.Image
import com.caloriecounter.calorie.ui.newlivewallpaper.GLWallpaperService
import com.caloriecounter.calorie.ui.newlivewallpaper.LWApplication
import com.caloriecounter.calorie.ui.newlivewallpaper.WallpaperCard
import com.caloriecounter.calorie.ui.slideimage.view.NotifiDialog
import com.caloriecounter.calorie.util.PreferenceUtil
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.caloriecounter.calorie.WeatherApplication
import com.caloriecounter.calorie.iinterface.OnClick
import com.caloriecounter.calorie.ui.main.viewmodel.WeatherViewModel
import com.caloriecounter.calorie.ui.slideimage.view.OptionAfterDownloadDialog
import com.caloriecounter.calorie.ui.slideimage.view.OptionAfterVideoDownloadDialog
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

@AndroidEntryPoint
class LiveWallpaperDetailFragment : BaseFragment<FragmentDetailLiveWallpaperBinding?>() {

    private var image: Image? = null
    private var mp4Url: String = ""
    private var simpleExoplayer: SimpleExoPlayer? = null
    private lateinit var dataSourceFactory: DataSource.Factory
    private var mContext: Context? = null

    private val mainViewModel: WeatherViewModel by viewModels()


    public fun setData(image: Image?) {
        this.image = image
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_detail_live_wallpaper
    }

    override fun initView() {
        try {
            var isShowDialog = PreferenceUtil.getInstance(mActivity)
                .getValue(Constant.PrefKey.IS_SHOW_DIALOG, true)

            if (isShowDialog) {
                var liveNotifiLiveDialog = NotifiLiveDialog.newInstance {
                    PreferenceUtil.getInstance(mActivity)
                        .setValue(Constant.PrefKey.IS_SHOW_DIALOG, false)
                }
                liveNotifiLiveDialog.show(childFragmentManager, "")
            }
        } catch (e: Exception) {
        }
    }

    override fun initData() {


        mp4Url = image!!.videoVariations.preview_small.url
        dataSourceFactory = DefaultDataSourceFactory(mActivity, "exoplayer-sample")
        initializePlayer()


        if (havePermission(REQEST_CODE_INIT)) {
            init()
        }
    }

    private fun init() {
        if (checkFile()) {
            setupPlayerWithFullSharp()
        } else {
            downloadLiveWallpaper(false, null, null)
        }
    }

    private fun checkFile(): Boolean {
        try {
            val downloadDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            val filePathOrigin =
                downloadDir.absolutePath + "/EZT4KWallpaper" + "/" + image!!.id + ".mp4"
            var fileOrigin = File(filePathOrigin)
            return fileOrigin.exists()
        } catch (e: Exception) {
            return false
        }
    }

    private fun setupPlayerWithFullSharp() {
        val downloadDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val filePathOrigin =
            downloadDir.absolutePath + "/EZT4KWallpaper" + "/" + image!!.id + ".mp4"
        var fileOrigin = File(filePathOrigin)

        var uri = Uri.fromFile(fileOrigin)
        uriForPlay = uri
        preparePlayer2(uri, "default")
    }

    private val TYPE_SHARE = 0;
    private val TYPE_SET = 1;

    private fun downloadLiveWallpaper(isShowDialog: Boolean, type: Int?, uri: Uri?) {
        var liveDownloadingDialog = LiveDownloadingDialog()
        liveDownloadingDialog.setDownloadListener(object : LiveDownloadingDialog.DownloadListener {
            override fun onDownloadComplete() {
                setupPlayerWithFullSharp()
                if (isShowDialog) {
                    if (type == TYPE_SHARE) {
                        shareImageUri(uri!!)
                    } else {
                        goToSetWallpaperSystemScreen(uri!!)
                    }
                }
            }

            override fun onDownloadError() {
                try {
                    val notifiDialog = NotifiDialog.newInstance(mActivity.getString(R.string.download_fail), mActivity.getString(R.string.ok), NotifiDialog.ClickButton {  })
                    notifiDialog.show(childFragmentManager, "")
                } catch (e: Exception) {
                }
            }

        })
        liveDownloadingDialog.setUrl(image!!.videoVariations.adapted.url, image!!.id + ".mp4")
        liveDownloadingDialog.isCancelable = false
        liveDownloadingDialog.show(childFragmentManager, "")

    }

    private var uriForPlay: Uri? = null


    fun copyFile(src: File?, dst: File?) {
        val var2 = FileInputStream(src)
        val var3 = FileOutputStream(dst)
        val var4 = ByteArray(1024)
        var var5: Int
        while (var2.read(var4).also { var5 = it } > 0) {
            var3.write(var4, 0, var5)
        }
        var2.close()
        var3.close()
    }


    override fun setListener() {
        binding?.btnBack?.setOnClickListener {
            mActivity.onBackPressed()
        }

//        binding?.btnSetImage?.setOnClickListener {
//            try {
//                mainViewModel.postAction(Constant.Action.SET, null, image!!.id)
//            } catch (e: Exception) {
//            }
//            WeatherApplication.trackingEvent("Click_Set_Image_Live")
//            if (havePermission(REQEST_CODE_SET)) {
//                if (checkFile()) {
//                    val downloadDir =
//                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//                    val file_path = downloadDir.absolutePath + "/EZT4KWallpaper"
//                    var uri = Uri.fromFile(File(file_path + "/" + image!!.id + ".mp4"))
//                    goToSetWallpaperSystemScreen(uri)
//                } else {
//                    val downloadDir =
//                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//                    val file_path = downloadDir.absolutePath + "/EZT4KWallpaper"
//                    var uri = Uri.fromFile(File(file_path + "/" + image!!.id + ".mp4"))
//                    val notifiDialog = NotifiDialog.newInstance(
//                        mActivity.getString(R.string.download_wallpaper_first),
//                        mActivity.getString(R.string.download),
//                        NotifiDialog.ClickButton {
//                            downloadLiveWallpaper(true, TYPE_SET, uri)
//                        })
//                    notifiDialog.show(childFragmentManager, "")
//
//                }
//            }
//        }


        binding?.btnSetImage?.setOnClickListener {
            var optionAfterDownloadDialog = OptionAfterVideoDownloadDialog()
            optionAfterDownloadDialog.setOnClick(object : OnClick {
                override fun onClickToMainScreen() {
                }

                override fun onClickToLockScreen() {
                }

                override fun onClickToBoth() {
                    Log.e("Click", "2");
                }

            }, true)
            optionAfterDownloadDialog.show(mActivity.supportFragmentManager, null)
        }



        binding?.btnDownload?.setOnClickListener {
            try {
                mainViewModel.postAction(Constant.Action.DOWNLOAD, null, image!!.id)
            } catch (e: Exception) {
                Log.e("", "")
            }
            WeatherApplication.trackingEvent("Click_Download_Live_Detail")
            if (havePermission(REQEST_CODE_DOWNLOAD))
                downloadLiveWallpaper(false, null, null)
        }

        binding?.btnShare?.setOnClickListener {
            WeatherApplication.trackingEvent("Click_Share_Live_Detail")
            if (havePermission(REQEST_CODE_SHARE)) {
                if (checkFile()) {
                    val downloadDir =
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    val file_path = downloadDir.absolutePath + "/EZT4KWallpaper"
                    var uri = Uri.fromFile(File(file_path + "/" + image!!.id + ".mp4"))
                    shareImageUri(uri)
                } else {
                    val downloadDir =
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    val file_path = downloadDir.absolutePath + "/EZT4KWallpaper"
                    var uri = Uri.fromFile(File(file_path + "/" + image!!.id + ".mp4"))
                    val notifiDialog = NotifiDialog.newInstance(
                        mActivity.getString(R.string.download_wallpaper_first),
                        mActivity.getString(R.string.download),
                        NotifiDialog.ClickButton {
                            downloadLiveWallpaper(true, TYPE_SHARE, uri)
                        })
                    notifiDialog.show(childFragmentManager, "")

                }
            }
        }

//        binding?.btnSetting?.setOnClickListener {
//            try {
//                var liveNotifiLiveDialog = NotifiLiveDialog.newInstance {
//                    PreferenceUtil.getInstance(mActivity)
//                        .setValue(Constant.PrefKey.IS_SHOW_DIALOG, false)
//                }
//                liveNotifiLiveDialog.show(childFragmentManager, "")
//            } catch (e: Exception) {
//            }
//        }

    }

    override fun setObserver() {}

    private fun initializePlayer() {
        simpleExoplayer = SimpleExoPlayer.Builder(mActivity).build()
        val randomUrl = mp4Url
        preparePlayer(randomUrl, "default")
        binding?.playerView?.player = simpleExoplayer
        simpleExoplayer?.playWhenReady = true
        simpleExoplayer?.repeatMode = Player.REPEAT_MODE_ONE
        simpleExoplayer?.addListener(object : Player.EventListener {})
        binding?.playerView?.useController = false
        binding?.playerView?.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
    }

    private fun buildMediaSource(uri: Uri, type: String): MediaSource {
        return ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(uri)
    }

    private fun preparePlayer(videoUrl: String, type: String) {
        val uri = Uri.parse(videoUrl)
        val mediaSource = buildMediaSource(uri, type)
        simpleExoplayer?.prepare(mediaSource)
    }

    private fun preparePlayer2(uri: Uri, type: String) {
        val mediaSource = buildMediaSource(uri, type)
        simpleExoplayer?.prepare(mediaSource)
    }

    private fun releasePlayer() {
        simpleExoplayer?.release()
    }


    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }

    override fun onPause() {
        super.onPause()
//        simpleExoplayer?.stop()
    }

    override fun onResume() {
        super.onResume()
        try {
            preparePlayer2(uriForPlay!!, "default")
        } catch (e: Exception) {
        }
    }

    private var isVideo = true
    private fun goToSetWallpaperSystemScreen(fUri: Uri) {
        if (fUri == null) {
            return
        }
        val downloadDir =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file_path = downloadDir.absolutePath + "/EZT4KWallpaper"

        var wallpaperCard = WallpaperCard(
            image!!.id + ".mp4",
            file_path + "/" + image!!.id + ".mp4",
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

        simpleExoplayer?.stop()

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

        } else if (requestCode == 779) {
            if (resultCode == Activity.RESULT_OK) {
                val downloadDir =
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                val file_path = downloadDir.absolutePath + "/EZT4KWallpaper"
                var uri = Uri.fromFile(File(file_path + "/" + image!!.id + ".mp4"))
                preparePlayer2(uri, "default")
            }
        }
    }

    private val PREVIEW_REQUEST_CODE = 7

    private fun showAlertError(message: String) {
        AlertDialog.Builder(mActivity)
            .setTitle("Error")
            .setMessage(message)
            .setPositiveButton("OK") { _, _ ->

            }
            .show()
    }

    private fun checkVideo(fUri: Uri): Boolean {
        try {
            var mediaPlayer = MediaPlayer.create(context, fUri).apply {
                isLooping = true
                setVolume(0f, 0f)
            }
            mediaPlayer.release()
            mediaPlayer = null
        } catch (e: Exception) {
            return false
        }
        return true
    }


    private fun checkInputStream(fUri: Uri): Boolean {
        try {
            val cr: ContentResolver = requireContext().contentResolver
            val input = cr.openInputStream(fUri)
            input?.close()
        } catch (e: Exception) {
            return false
        }
        return true
    }

    private fun saveSettings(fUri: Uri, isVideo: Boolean) {
        val sharedPref =
            requireContext().getSharedPreferences(keySharedPrefVideo, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString(keyVideo, fUri.toString())
        editor.putBoolean(keyType, isVideo)
        editor.apply()
    }

    fun addFragment(fragment: Fragment) {
        try {
            val frm = mActivity.supportFragmentManager
            frm.beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .add(R.id.mainFrame, fragment)
                .addToBackStack(fragment.javaClass.simpleName)
                .commit()
        } catch (e: java.lang.Exception) {
        }
    }

    public fun shareImageUri(uri: Uri) {
        try {
            val builder = StrictMode.VmPolicy.Builder()
            StrictMode.setVmPolicy(builder.build())
            val intent = Intent(Intent.ACTION_SEND)
            intent.putExtra(Intent.EXTRA_STREAM, uri)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.type = "video/mp4"
            startActivity(intent)
        } catch (e: java.lang.Exception) {

            e.printStackTrace()
        }
    }

    private val REQEST_CODE_INIT = 789
    private val REQEST_CODE_DOWNLOAD = 678
    private val REQEST_CODE_SHARE = 779
    private val REQEST_CODE_SET = 379
    private fun havePermission(requestCode: Int): Boolean {
        if (ContextCompat.checkSelfPermission(
                mActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                mActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), requestCode)
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), requestCode)
            return false
        } else {
            return true
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQEST_CODE_SET) {
            if (grantResults.isNotEmpty()) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    binding?.btnSetImage?.performClick()
                } else {
                    Toast.makeText(
                        mActivity,
                        mActivity.getString(R.string.please_allow_permission),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
            }
        } else if (requestCode == REQEST_CODE_SHARE) {
            if (grantResults.isNotEmpty()) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    binding?.btnShare?.performClick()
                } else {
                    Toast.makeText(
                        mActivity,
                        mActivity.getString(R.string.please_allow_permission),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
            }
        } else if (requestCode == REQEST_CODE_DOWNLOAD) {
            if (grantResults.isNotEmpty()) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    binding?.btnDownload?.performClick()
                } else {
                    Toast.makeText(
                        mActivity,
                        mActivity.getString(R.string.please_allow_permission),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
            }
        } else if (requestCode == REQEST_CODE_INIT) {
            if (grantResults.isNotEmpty()) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    init()
                } else {
                    Toast.makeText(
                        mActivity,
                        mActivity.getString(R.string.please_allow_permission),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
            }
        }
    }


    override fun getFrame(): Int {
        return R.id.mainFrame
    }
}