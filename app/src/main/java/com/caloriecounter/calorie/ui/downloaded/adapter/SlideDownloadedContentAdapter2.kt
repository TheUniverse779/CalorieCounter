package com.caloriecounter.calorie.ui.downloaded.adapter

import android.content.Context
import android.net.Uri
import android.view.*
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.caloriecounter.calorie.databinding.ItemImageSlideBinding
import com.caloriecounter.calorie.databinding.ItemVideoSlideBinding
import com.caloriecounter.calorie.ui.main.model.image.Image
import com.caloriecounter.calorie.ui.newlivewallpaper.DownloadedContent
import com.caloriecounter.calorie.util.toGone
import com.caloriecounter.calorie.util.toVisible

class SlideDownloadedContentAdapter2 : RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private val TYPE_ITEM = 0
    private val TYPE_ITEM_VIDEO = 1
    private var context: Context? = null
    private var images = ArrayList<DownloadedContent>()

    private var centerPosition: Int? = 0;

    public var simpleExoplayer: SimpleExoPlayer? = null
    private lateinit var dataSourceFactory: DataSource.Factory

    fun setCenterPosition(centerPosition: Int) {
        this.centerPosition = centerPosition;
    }

    constructor()


    constructor(context: Context?, datas: ArrayList<DownloadedContent>) : super() {
        this.context = context
        this.images = datas
    }


    override fun getItemViewType(position: Int): Int {
        if(images!![position].type == 1){
            return TYPE_ITEM_VIDEO
        }else {
            return TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        var v: View? = null
        return if (viewType == TYPE_ITEM) {
            val binding: ItemImageSlideBinding =
                ItemImageSlideBinding.inflate(inflater, parent, false)
            ViewHolder(binding)
        } else {
            val binding: ItemVideoSlideBinding =
                ItemVideoSlideBinding.inflate(inflater, parent, false)
            VideoViewHolder(binding)
        }
    }

    override fun getItemCount(): Int {
        try {
            return images!!.size;
        } catch (e: Exception) {
            return 0
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        try {
            if (getItemViewType(position) == TYPE_ITEM) {
                (holder as ViewHolder).bindItem(position)
            }else{
                (holder as VideoViewHolder).bindItem(position)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    inner class ViewHolder(var binding: ItemImageSlideBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var position: Int? = 0;


        fun bindItem(position: Int) {
            this.position = position
            Glide.with(context!!)
                .load(images[position].image.uri)
                .into(binding.imgCache)

            val link = images[position].image.uri
            Glide.with(context!!).load(link).into(binding.img)

        }

    }


    inner class VideoViewHolder(var binding: ItemVideoSlideBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var position: Int? = 0;


        fun bindItem(position: Int) {
            this.position = position

            Glide.with(context!!).load(images!![position].wallpaperCard.uri).into(binding.imgCache)

            if (position == centerPosition) {
                simpleExoplayer?.release()
                dataSourceFactory = DefaultDataSourceFactory(context, "exoplayer-sample")
                initializePlayer(images!![position].wallpaperCard.uri, binding.imgCache, binding.progress)
                binding?.playerView?.player = simpleExoplayer
//                binding?.playerView?.useController = true
                binding?.playerView?.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
            }

        }

    }


    private fun initializePlayer(url : Uri, imgCache : ImageView, progress : ProgressBar) {
        simpleExoplayer = SimpleExoPlayer.Builder(context!!).build()
        preparePlayer("default", url)

        simpleExoplayer?.playWhenReady = true
        simpleExoplayer?.volume = 0f
        simpleExoplayer?.repeatMode = Player.REPEAT_MODE_ONE
        simpleExoplayer?.addListener(object : Player.EventListener {
            override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                super.onPlayerStateChanged(playWhenReady, playbackState)
                if (playbackState == Player.STATE_BUFFERING) {
                    progress.toVisible()
                } else if (playbackState == Player.STATE_READY) {
                    imgCache.toGone()
                    progress.toGone()
                }
            }
        })

    }

    private fun buildMediaSource(uri: Uri, type: String): MediaSource {
        return ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(uri)
    }

    private fun preparePlayer(type: String, uri :  Uri) {
        val mediaSource = buildMediaSource(uri, type)
        simpleExoplayer?.prepare(mediaSource)
    }


}

