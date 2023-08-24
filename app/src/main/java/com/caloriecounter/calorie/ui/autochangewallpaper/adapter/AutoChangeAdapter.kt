package com.caloriecounter.calorie.ui.autochangewallpaper.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.caloriecounter.calorie.R
import com.caloriecounter.calorie.WeatherApplication
import com.caloriecounter.calorie.iinterface.DataChange
import com.caloriecounter.calorie.ui.newlivewallpaper.task.Image
import kotlinx.android.synthetic.main.item_image_auto_change.view.*

class AutoChangeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private val TYPE_ITEM = 0
    private var context: Context? = null
    private var images: List<Image>? = null
    private var dataChange : DataChange? = null
    private var clickItem : ClickItem? = null


    constructor()
    constructor(context: Context?, datas: List<Image>?) : super() {
        this.context = context
        this.images = datas
    }

    constructor(context: Context?, datas: List<Image>?, dataChange : DataChange?, clickItem : ClickItem?) : super() {
        this.context = context
        this.images = datas
        this.dataChange = dataChange
        this.clickItem = clickItem
    }


    override fun getItemViewType(position: Int): Int {
        return TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var v: View = LayoutInflater.from(parent.context).inflate(R.layout.item_image_auto_change, parent, false)
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

            Glide.with(context!!).load(images!![position].uri).into(itemView.imgAvatar)
            itemView.setOnClickListener {
                if(itemView.imgCheck.visibility == View.GONE){
                    itemView.imgUnCheck.performClick()
                }else{
                    itemView.imgCheck.performClick()
                }
                WeatherApplication.trackingEvent("Click_Item_Image_Auto_change")
            }

            if(images!![position].isSelectedAuto){
                itemView.imgCheck.visibility = View.VISIBLE
                itemView.imgUnCheck.visibility = View.GONE
            }else{
                itemView.imgCheck.visibility = View.GONE
                itemView.imgUnCheck.visibility = View.VISIBLE
            }

            itemView.imgCheck.setOnClickListener {
                itemView.imgCheck.visibility = View.GONE
                itemView.imgUnCheck.visibility = View.VISIBLE
                images!![position].isSelectedAuto = false
                clickItem?.onClickItem(position)
                WeatherApplication.trackingEvent("Click_Check_Auto_change")
            }

            itemView.imgUnCheck.setOnClickListener {
                itemView.imgCheck.visibility = View.VISIBLE
                itemView.imgUnCheck.visibility = View.GONE
                images!![position].isSelectedAuto = true
                clickItem?.onClickItem(position)
                WeatherApplication.trackingEvent("Click_unCheck_Auto_change")
            }
        }

    }


    interface ClickItem{
        fun onClickItem(position: Int);
    }


}

