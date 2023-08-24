package com.caloriecounter.calorie.ui.downloaded.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.caloriecounter.calorie.R
import com.caloriecounter.calorie.WeatherApplication
import com.caloriecounter.calorie.iinterface.DataChange
import com.caloriecounter.calorie.ui.newlivewallpaper.DownloadedContent
import com.caloriecounter.calorie.ui.newlivewallpaper.Utils
import com.caloriecounter.calorie.util.toGone
import com.caloriecounter.calorie.util.toVisible
import kotlinx.android.synthetic.main.item_image.view.*

class ListImageDownloadedAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private val TYPE_ITEM = 0
    private var context: Context? = null
    private var images: List<DownloadedContent>? = null
    private var dataChange : DataChange? = null
    private var clickItem : ClickItem? = null


    constructor()
    constructor(context: Context?, datas: List<DownloadedContent>?) : super() {
        this.context = context
        this.images = datas
    }

    constructor(context: Context?, datas: List<DownloadedContent>?, dataChange : DataChange?, clickItem : ClickItem?) : super() {
        this.context = context
        this.images = datas
        this.dataChange = dataChange
        this.clickItem = clickItem
    }


    override fun getItemViewType(position: Int): Int {
        return TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var v: View = LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        try {
            if(images!!.isNotEmpty()){
                dataChange?.onDataChange(false)
                return images!!.size;
            }else{
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

        constructor(itemView: View?) : super(itemView!!) {
            itemView.setOnClickListener {

            }
        }

        fun bindItem(position: Int) {
            this.position = position


            itemView.setOnClickListener {
                clickItem?.onClickItem(position)
                WeatherApplication.trackingEvent("Click_Image_Downloaded")
            }

            if(images!![position].type == 0){
                itemView.imgVideo.toGone()
                Glide.with(context!!).load(images!![position].image.uri).into(itemView.imgAvatar)
            }else{
                itemView.imgVideo.toVisible()
                val bitmap = Utils.createVideoThumbnailFromUri(
                    context!!, images!![position].wallpaperCard.uri
                )
                Glide.with(context!!).load(bitmap).into(itemView.imgAvatar)


            }
        }

    }


    interface ClickItem{
        fun onClickItem(position: Int);
    }


}

