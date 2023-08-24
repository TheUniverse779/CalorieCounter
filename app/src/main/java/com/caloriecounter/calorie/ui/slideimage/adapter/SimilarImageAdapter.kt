package com.caloriecounter.calorie.ui.slideimage.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.caloriecounter.calorie.R
import com.caloriecounter.calorie.ads.InterAds
import com.caloriecounter.calorie.ui.main.model.image.Image
import com.caloriecounter.calorie.ui.similar.DetailSimilarImageActivity
import kotlinx.android.synthetic.main.item_image.view.*

class SimilarImageAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private val TYPE_ITEM = 0
    private var context: Context? = null
    private var images: List<Image>? = null


    constructor()
    constructor(context: Context?, datas: List<Image>?) : super() {
        this.context = context
        this.images = datas
    }


    override fun getItemViewType(position: Int): Int {
        return TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        return ViewHolder(v)
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

    inner class ViewHolder : RecyclerView.ViewHolder {
        private var position: Int? = 0;

        constructor(itemView: View?) : super(itemView!!) {
            itemView.setOnClickListener {
            }
        }

        fun bindItem(position: Int) {
            this.position = position

            val link = images!![position].variations.preview_small.url
            Glide.with(context!!).load(link).into(itemView.imgAvatar)
            itemView.setOnClickListener {
                InterAds.showAdsBreak(context as Activity) {
                    DetailSimilarImageActivity.startScreen(
                        context!!,
                        images as ArrayList<Image>,
                        position,
                        images!![position].id
                    )
                }

            }
        }

    }

}

