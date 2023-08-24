package com.caloriecounter.calorie.ui.preview

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.view.View
import android.view.WindowManager
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.RawResourceDataSource
import com.caloriecounter.calorie.R
import com.caloriecounter.calorie.ads.InterAds
import com.caloriecounter.calorie.base.BaseActivityNew
import com.caloriecounter.calorie.base.BaseFragment
import com.caloriecounter.calorie.databinding.ActivityPreviewVideoBinding
import com.caloriecounter.calorie.ui.main.model.image.Image

class PreviewVideoActivity : BaseActivityNew<ActivityPreviewVideoBinding?>() {
    var image : Image?  = null;

    private var simpleExoplayer: SimpleExoPlayer? = null
    private lateinit var dataSourceFactory: DataSource.Factory
    override fun getLayoutRes(): Int {
        return R.layout.activity_preview_video
    }

    override fun getFrame(): Int {
        return 0
    }

    override fun getDataFromIntent() {
        image = intent?.getSerializableExtra("data") as Image
    }
    override fun doAfterOnCreate() {
        binding?.btnBack?.setOnClickListener {
            onBackPressed()
        }

        dataSourceFactory = DefaultDataSourceFactory(this, "exoplayer-sample")
        initializePlayer()
    }

    override fun setListener() {

    }

    override fun initFragment(): BaseFragment<*>? {
        return null
    }

    override fun doFirstMethod() {
        super.doFirstMethod()
        if (Build.VERSION.SDK_INT in 19..20) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
        }
        if (Build.VERSION.SDK_INT >= 19) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }

        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    private fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
        val win = activity.window
        val winParams: WindowManager.LayoutParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }

    override fun onBackPressed() {
        InterAds.showAdsBreak(this) {
            super.onBackPressed()
        }

    }

    companion object {
        fun startScreen(context: Context, image: Image?) {
            InterAds.showAdsBreak(context as Activity) {
                val intent = Intent(context, PreviewVideoActivity::class.java)
                intent.putExtra("data", image)
                context.startActivity(intent)
            }
        }


    }

    private fun initializePlayer() {
        simpleExoplayer = SimpleExoPlayer.Builder(this).build()
        preparePlayer("default")
        binding?.imgPreview?.player = simpleExoplayer
        simpleExoplayer?.playWhenReady = true
        simpleExoplayer?.repeatMode = Player.REPEAT_MODE_ONE
        simpleExoplayer?.addListener(object : Player.EventListener {

        })
        binding?.imgPreview?.useController = false
        binding?.imgPreview?.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
    }

    private fun buildMediaSource(uri: Uri, type: String): MediaSource {
        return ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(uri)
    }

    private fun preparePlayer(type: String) {
        val mediaSource = buildMediaSource(Uri.parse(image?.videoVariations?.preview_small?.url), type)
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
}