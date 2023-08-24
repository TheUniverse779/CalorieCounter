package com.caloriecounter.calorie.ui.downloaded.view

import android.net.Uri
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.caloriecounter.calorie.R
import com.caloriecounter.calorie.base.BaseFragment
import com.caloriecounter.calorie.databinding.ActivityPreviewVideoBinding
import com.caloriecounter.calorie.ui.newlivewallpaper.DownloadedContent
import com.caloriecounter.calorie.util.toGone
import com.caloriecounter.calorie.util.toVisible

class PreviewDownloadedContentFragment : BaseFragment<ActivityPreviewVideoBinding?>() {

    public var image : DownloadedContent?  = null;

    private var simpleExoplayer: SimpleExoPlayer? = null
    private lateinit var dataSourceFactory: DataSource.Factory

    override fun getLayoutRes(): Int {
        return R.layout.activity_preview_video
    }

    override fun initView() {
        binding?.btnBack?.setOnClickListener {
            mActivity.onBackPressed()
        }

        if(image?.type == 1){
            binding?.previewImage?.toGone()
            binding?.imgPreview?.toVisible()

            dataSourceFactory = DefaultDataSourceFactory(mActivity, "exoplayer-sample")
            initializePlayer()
        }else{
            binding?.previewImage?.toVisible()
            binding?.imgPreview?.toGone()

            Glide.with(this).load(image?.image?.uri).into(binding?.previewImage!!)
        }
    }
    override fun initData() {}
    override fun setListener() {}
    override fun setObserver() {}
    override fun getFrame(): Int {
        return R.id.mainFrame
    }

    private fun initializePlayer() {
        simpleExoplayer = SimpleExoPlayer.Builder(mActivity).build()
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
        val mediaSource = buildMediaSource(image!!.wallpaperCard.uri, type)
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