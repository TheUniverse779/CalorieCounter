package com.caloriecounter.calorie.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.caloriecounter.calorie.databinding.ItemDishBinding
import com.caloriecounter.calorie.ui.main.model.dish.Dish

class DishAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private val TYPE_ITEM = 0
    private var context: Context? = null
    private var images: List<Dish>? = null


    constructor(context: Context?, datas: List<Dish>?) : super() {
        this.context = context
        this.images = datas
    }


    override fun getItemViewType(position: Int): Int {
        return TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemDishBinding = ItemDishBinding.inflate(inflater, parent, false)
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

    inner class ViewHolder (var binding: ItemDishBinding) : RecyclerView.ViewHolder(binding.root) {
        private var position: Int? = 0;

        init {
        }

        fun bindItem(position: Int) {
            this.position = position
            Glide.with(context!!).load(images!![position].imageUrl).into(binding.imgAvatar)
            binding.tvTitle.text = images!![position].name
            binding.tvCalo.text = images!![position].nutritionalContents.energy.value.toInt().toString()
        }


        private fun setData(){

        }

    }

}

