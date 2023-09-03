package com.caloriecounter.calorie.ui.search.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.caloriecounter.calorie.databinding.ItemDishBinding
import com.caloriecounter.calorie.ui.search.model.SearchContent
import java.lang.reflect.Array

class DishSearchAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private val TYPE_ITEM = 0
    private var context: Context? = null
    private var images: ArrayList<SearchContent>? = null


    constructor(context: Context?, datas: ArrayList<SearchContent>?) : super() {
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
            binding.tvTitle.text = images!![position].item.name
//            binding.tvCalo.text = images!![position].nutritionalContents.energy.value.toInt().toString()

            binding.root.setOnClickListener {
//                DishDetailActivity.startScreen(context!!, images!![position])
            }
        }


        private fun setData(){

        }

    }

}

