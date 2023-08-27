package com.caloriecounter.calorie.ui.dishdetail.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.caloriecounter.calorie.databinding.ItemInformationBinding
import com.caloriecounter.calorie.ui.dishdetail.model.Information

class InformationAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private val TYPE_ITEM = 0
    private var context: Context? = null
    private var images: List<Information>? = null


    constructor(context: Context?, datas: List<Information>?) : super() {
        this.context = context
        this.images = datas
    }


    override fun getItemViewType(position: Int): Int {
        return TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemInformationBinding = ItemInformationBinding.inflate(inflater, parent, false)
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

    inner class ViewHolder (var binding: ItemInformationBinding) : RecyclerView.ViewHolder(binding.root) {
        private var position: Int? = 0;

        init {
        }

        fun bindItem(position: Int) {
            this.position = position
            binding.tvCalo.text = images!![position].name
            binding.tvCaloValue.text = images!![position].value.toInt().toString()
        }


        private fun setData(){

        }

    }

}

