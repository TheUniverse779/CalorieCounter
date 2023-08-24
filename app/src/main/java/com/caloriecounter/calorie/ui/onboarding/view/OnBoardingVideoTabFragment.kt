package com.caloriecounter.calorie.ui.onboarding.view

import android.net.Uri
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.RawResourceDataSource
import com.caloriecounter.calorie.R
import com.caloriecounter.calorie.base.BaseFragment
import com.caloriecounter.calorie.databinding.FragmentOnboard3Binding

class OnBoardingVideoTabFragment : BaseFragment<FragmentOnboard3Binding?>() {
    public var position: Int = 0
    public var onBoardingFragment: OnBoardingFragment? = null

    private var simpleExoplayer: SimpleExoPlayer? = null
    private lateinit var dataSourceFactory: DataSource.Factory
    override fun getLayoutRes(): Int {
        return R.layout.fragment_onboard_3
    }

    override fun initView() {}
    override fun initData() {
        binding?.tv1?.text = "Unique"
        binding?.tv2?.text = "Video Wallpapers"

        dataSourceFactory = DefaultDataSourceFactory(mActivity, "exoplayer-sample")
        initializePlayer()
    }

    override fun setListener() {

    }

    override fun setObserver() {}
    override fun getFrame(): Int {
        return R.id.mainFrame
    }

    private fun initializePlayer() {
        simpleExoplayer = SimpleExoPlayer.Builder(mActivity).build()
        preparePlayer("default")
        binding?.playerView?.player = simpleExoplayer
        simpleExoplayer?.playWhenReady = true
        simpleExoplayer?.volume = 0f
        simpleExoplayer?.repeatMode = Player.REPEAT_MODE_ONE
        simpleExoplayer?.addListener(object : Player.EventListener {

        })
        binding?.playerView?.useController = false
        binding?.playerView?.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
    }

    private fun buildMediaSource(uri: Uri, type: String): MediaSource {
        return ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(uri)
    }

    private fun preparePlayer(type: String) {
        var asset = when(position){
            0 -> R.raw.intro_1
            1 -> R.raw.intro_2
            2 -> R.raw.intro_3
            else -> {
                R.raw.intro_1
            }
        }
        val uri  = RawResourceDataSource.buildRawResourceUri(asset);
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
}