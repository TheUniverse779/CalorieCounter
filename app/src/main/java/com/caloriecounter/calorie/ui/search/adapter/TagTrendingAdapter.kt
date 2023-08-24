package com.caloriecounter.calorie.ui.search.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.caloriecounter.calorie.WeatherApplication
import com.caloriecounter.calorie.databinding.ItemTagTrendingBinding
import com.caloriecounter.calorie.ui.search.model.PopularTag
import com.caloriecounter.calorie.ui.search.view.SearchFragment
import com.caloriecounter.calorie.ui.search.view.SearchResultFragment
import com.caloriecounter.calorie.util.GlideBlurTransformation

class TagTrendingAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private val TYPE_ITEM = 0
    private var context: Context? = null
    private var images: List<PopularTag>? = null
    private var fragment : SearchFragment? = null


    constructor()
    constructor(context: Context?, datas: List<PopularTag>?, fragment : SearchFragment?) : super() {
        this.context = context
        this.images = datas
        this.fragment = fragment
    }


    override fun getItemViewType(position: Int): Int {
        return TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        var v: View? = null
        return if (viewType == TYPE_ITEM) {
            val binding: ItemTagTrendingBinding =
                ItemTagTrendingBinding.inflate(inflater, parent, false)
            ViewHolder(binding)
        } else {
            val binding: ItemTagTrendingBinding =
                ItemTagTrendingBinding.inflate(inflater, parent, false)
            ViewHolder(binding)
        }

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

    inner class ViewHolder(var binding: ItemTagTrendingBinding) : RecyclerView.ViewHolder(binding.root) {
        private var position: Int? = 0;

        init {

        }

        fun bindItem(position: Int) {
            this.position = position
            Glide.with(context!!).load(images!![position].preview).transform( GlideBlurTransformation(context)).into(binding.imgAvatar)
            binding.tvTitle.text = images!![position].title
            binding.root.setOnClickListener {
                WeatherApplication.trackingEvent("Click_Item_Tag_Trending")
                var searchResultFragment = SearchResultFragment()
                searchResultFragment.setKeyword(images!![position].title)
                searchResultFragment.setShowKeyboard(false)
                fragment?.addFragment(searchResultFragment)
            }
        }
    }

}



