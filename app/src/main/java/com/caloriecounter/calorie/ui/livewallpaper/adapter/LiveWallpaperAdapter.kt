package com.caloriecounter.calorie.ui.livewallpaper.adapter

import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.caloriecounter.calorie.R
import com.caloriecounter.calorie.iinterface.DataChange
import com.caloriecounter.calorie.ui.livewallpaper.view.LiveWallpaperDetailActivity
import com.caloriecounter.calorie.ui.main.model.image.Image
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.upstream.DataSource
import com.caloriecounter.calorie.WeatherApplication
import kotlinx.android.synthetic.main.item_live_wallpaper.view.*


class LiveWallpaperAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private val TYPE_ITEM = 0
    private var context: Context? = null
    private var images: List<Image>? = null
    private var dataChange: DataChange? = null

    private var handler: Handler? = null
    private var handler1: Handler? = null

    private var centerPosition: Int? = 0;

    constructor()

    constructor(context: Context?, datas: List<Image>?, dataChange: DataChange?) : super() {
        this.context = context
        this.images = datas
        this.dataChange = dataChange

    }

    public fun setCenterPosition(centerPosition: Int?) {
//        this.centerPosition = centerPosition
//        if (centerPosition == -1) {
//            handler?.removeCallbacksAndMessages(null)
//            handler1?.removeCallbacksAndMessages(null)
//        }
    }

    override fun getItemViewType(position: Int): Int {
        return TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_live_wallpaper, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        try {
            if (images!!.isNotEmpty()) {
                dataChange?.onDataChange(false)
                return images!!.size;
            } else {
                dataChange?.onDataChange(true)
                return 0
            }
        } catch (e: Exception) {
            dataChange?.onDataChange(true)
            return 0
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        try {
            if (getItemViewType(position) == TYPE_ITEM) {
                (holder as ViewHolder).bindItem(position)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    inner class ViewHolder : RecyclerView.ViewHolder {
        private var position: Int? = 0;
        private var isHold = false

        private lateinit var simpleExoplayer: SimpleExoPlayer
        private var playbackPosition: Long = 0
        private var mp4Url = ""

        private lateinit var dataSourceFactory: DataSource.Factory

        constructor(itemView: View?) : super(itemView!!) {

        }

        fun bindItem(position: Int) {
            this.position = position

            itemView.setOnClickListener {
                LiveWallpaperDetailActivity.startScreen(context!!, images!![position])
                WeatherApplication.trackingEvent("Click_Item_Live")
            }

            Glide.with(context!!).load(images!![position].imageVariations.preview_small.url)
                .into(itemView.imgBackdrop)

            val link = images!![position].imageVariations.preview_small.url
            Glide.with(context!!).load(link).into(itemView.imgAvatar)
//            mp4Url = images!![position].videoVariations.preview_small.url
//
//
//
//
//            dataSourceFactory = DefaultDataSourceFactory(context!!, "exoplayer-sample")
//            initializePlayer()


        }

//        private fun initializePlayer() {
//            simpleExoplayer = SimpleExoPlayer.Builder(context!!).build()
//            val randomUrl = mp4Url
//            preparePlayer(randomUrl, "default")
//            itemView.playerView.player = simpleExoplayer
//            simpleExoplayer.seekTo(playbackPosition)
//            simpleExoplayer.playWhenReady = true
//            simpleExoplayer.addListener(this)
//        }
//
//        private fun buildMediaSource(uri: Uri, type: String): MediaSource {
//            return ProgressiveMediaSource.Factory(dataSourceFactory)
//                .createMediaSource(uri)
//        }
//
//        private fun preparePlayer(videoUrl: String, type: String) {
//            val uri = Uri.parse(videoUrl)
//            val mediaSource = buildMediaSource(uri, type)
//            simpleExoplayer.prepare(mediaSource)
//        }
//
//        private fun releasePlayer() {
//            playbackPosition = simpleExoplayer.currentPosition
//            simpleExoplayer.release()
//        }
//
//        override fun onPlayerError(error: ExoPlaybackException) {
//            // handle error
//        }
    }

}

