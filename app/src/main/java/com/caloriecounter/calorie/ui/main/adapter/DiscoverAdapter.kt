package com.caloriecounter.calorie.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.caloriecounter.calorie.databinding.ItemCategoryBinding
import com.caloriecounter.calorie.model.Schedule
import com.caloriecounter.calorie.ui.main.model.dish.Category
import com.caloriecounter.calorie.ui.main.model.dish.Dish
import com.caloriecounter.calorie.ui.main.model.dish.DishData
import com.caloriecounter.calorie.util.Util
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class DiscoverAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private val TYPE_ITEM = 0
    private var context: Context? = null
    private var images: List<String>? = null


    constructor(context: Context?, datas: List<String>?) : super() {
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

    inner class ViewHolder (var binding: ItemCategoryBinding) : RecyclerView.ViewHolder(binding.root) {
        private var position: Int? = 0;
        private var dishAdapter : DishAdapter? = null

        init {
        }

        fun bindItem(position: Int) {
            this.position = position
            val schedules = Gson().fromJson<DishData>(
                Util.loadJSONFromAsset(context, images!![position]), DishData::class.java)
            dishAdapter = DishAdapter(context, schedules.items)
            binding.rclImage.adapter = dishAdapter
            binding.tvTitle.text = images!![position]
        }


        private fun setData(){

        }

    }

}

