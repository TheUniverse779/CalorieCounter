package com.caloriecounter.calorie.ui.slideimage.adapter

import android.content.Context
import android.net.Uri
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.RawResourceDataSource
import com.caloriecounter.calorie.R
import com.caloriecounter.calorie.databinding.ItemVideoSlideBinding
import com.caloriecounter.calorie.ui.main.model.image.Image

class SlideVideoAdapter2 : RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private val TYPE_ITEM = 0
    private var context: Context? = null
    private var images = ArrayList<Image>()

    private var centerPosition: Int? = 0;

    fun setCenterPosition(centerPosition: Int) {
        this.centerPosition = centerPosition;
    }

    constructor()


    public var simpleExoplayer: SimpleExoPlayer? = null
    private lateinit var dataSourceFactory: DataSource.Factory

    constructor(context: Context?, datas: ArrayList<Image>) : super() {
        this.context = context
        this.images = datas



    }

    private fun initializePlayer(url : String) {
        simpleExoplayer = SimpleExoPlayer.Builder(context!!).build()
        preparePlayer("default", url)

        simpleExoplayer?.playWhenReady = true
        simpleExoplayer?.volume = 0f
        simpleExoplayer?.repeatMode = Player.REPEAT_MODE_ONE
        simpleExoplayer?.addListener(object : Player.EventListener {

        })

    }

    private fun buildMediaSource(uri: Uri, type: String): MediaSource {
        return ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(uri)
    }

    private fun preparePlayer(type: String, url :  String) {
        val mediaSource = buildMediaSource(Uri.parse(url), type)
        simpleExoplayer?.prepare(mediaSource)
    }

    override fun getItemViewType(position: Int): Int {
        return TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        var v: View? = null
        return if (viewType == TYPE_ITEM) {
            val binding: ItemVideoSlideBinding =
                ItemVideoSlideBinding.inflate(inflater, parent, false)
            ViewHolder(binding)
        } else {
            val binding: ItemVideoSlideBinding =
                ItemVideoSlideBinding.inflate(inflater, parent, false)
            ViewHolder(binding)
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
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    inner class ViewHolder(var binding: ItemVideoSlideBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var position: Int? = 0;


        fun bindItem(position: Int) {
            this.position = position

            if (position == centerPosition) {
                simpleExoplayer?.release()
                dataSourceFactory = DefaultDataSourceFactory(context, "exoplayer-sample")
                initializePlayer(images!![position].videoVariations.preview_small.url)
                binding?.playerView?.player = simpleExoplayer
                binding?.playerView?.useController = true
                binding?.playerView?.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
            }
        }

    }


}

