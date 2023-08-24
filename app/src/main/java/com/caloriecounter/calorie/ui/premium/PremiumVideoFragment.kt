package com.caloriecounter.calorie.ui.premium

import android.content.Context
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
import com.caloriecounter.calorie.databinding.FragmentPremiumVideoBinding

class PremiumVideoFragment : BaseFragment<FragmentPremiumVideoBinding?>() {
    public var position: Int = 0

    private var simpleExoplayer: SimpleExoPlayer? = null
    private lateinit var dataSourceFactory: DataSource.Factory

    private var mContext : Context? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.mContext = context;
    }
    override fun getLayoutRes(): Int {
        return R.layout.fragment_premium_video
    }

    override fun initView() {}
    override fun initData() {
        dataSourceFactory = DefaultDataSourceFactory(mContext, "exoplayer-sample")
        initializePlayer()
    }

    override fun setListener() {

    }

    override fun setObserver() {}
    override fun getFrame(): Int {
        return R.id.mainFrame
    }

    private fun initializePlayer() {
        simpleExoplayer = SimpleExoPlayer.Builder(mContext!!).build()
        preparePlayer("default")
        binding?.playerView?.player = simpleExoplayer
        simpleExoplayer?.playWhenReady = true
        simpleExoplayer?.repeatMode = Player.REPEAT_MODE_ONE
        simpleExoplayer?.addListener(object : Player.EventListener {

        })
        binding?.playerView?.useController = false
        binding?.playerView?.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
    }

    private fun buildMediaSource(uri: Uri, type: String): MediaSource {
        return ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(uri)
    }

    private fun preparePlayer(type: String) {
        val uri  = RawResourceDataSource.buildRawResourceUri(R.raw.splash);
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