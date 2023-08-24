package com.caloriecounter.calorie.ui.slideimage.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.caloriecounter.calorie.R
import com.caloriecounter.calorie.WeatherApplication
import com.caloriecounter.calorie.ui.search.view.SearchActivity
import kotlinx.android.synthetic.main.item_tag_in_detail.view.*

class TagAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private val TYPE_ITEM = 0
    private var context: Context? = null
    private var tags: List<String>? = null


    constructor()

    constructor(context: Context?, tags: List<String>?) : super() {
        this.context = context
        this.tags = tags
    }


    override fun getItemViewType(position: Int): Int {
        return TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var v: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_tag_in_detail, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        try {
            if (tags!!.isNotEmpty()) {
                return tags!!.size;
            } else {
                return 0
            }
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
            itemView.tvTag.text = tags!![position]
            itemView.setOnClickListener {
                WeatherApplication.trackingEvent("Click_Item_Tag")
                SearchActivity.startScreen(context!!, tags!![position])
            }
        }

    }

}

