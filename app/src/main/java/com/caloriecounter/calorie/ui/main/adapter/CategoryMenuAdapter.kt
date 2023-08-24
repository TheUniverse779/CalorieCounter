package com.caloriecounter.calorie.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.caloriecounter.calorie.R
import com.caloriecounter.calorie.ui.main.model.category.Category

class CategoryMenuAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private val TYPE_ITEM = 0
    private var context: Context? = null
    private var images: List<Category>? = null
    private var clickItem: ClickItem? = null


    constructor()
    constructor(context: Context?, datas: List<Category>?) : super() {
        this.context = context
        this.images = datas
    }

    constructor(context: Context?, datas: List<Category>?, clickItem: ClickItem) : super() {
        this.context = context
        this.images = datas
        this.clickItem = clickItem
    }


    override fun getItemViewType(position: Int): Int {
        return TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var v: View = LayoutInflater.from(parent.context).inflate(R.layout.item_menu_category, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        try {
            if (images!!.isNotEmpty()) {
                return images!!.size;
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

        }

    }

    public interface ClickItem {
        fun onClickItem(category: Category)
    }
}

