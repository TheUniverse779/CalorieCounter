package com.caloriecounter.calorie.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.caloriecounter.calorie.databinding.ItemCategoryBinding
import com.caloriecounter.calorie.ui.main.model.dish.Category

class DiscoverAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private val TYPE_ITEM = 0
    private var context: Context? = null
    private var images: List<Category>? = null


    constructor(context: Context?, datas: List<Category>?) : super() {
        this.context = context
        this.images = datas
    }


    override fun getItemViewType(position: Int): Int {
        return TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemCategoryBinding = ItemCategoryBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        try {
//            return images!!.size;
            return 20;
        } catch (e: Exception) {
            return 20
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

    inner class ViewHolder (var binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        private var position: Int? = 0;
        private var dishAdapter : DishAdapter? = null

        init {
        }

        fun bindItem(position: Int) {
            this.position = position
            dishAdapter = DishAdapter(context, null)
            binding.rclImage.adapter = dishAdapter
        }


        private fun setData(){

        }

    }

}

