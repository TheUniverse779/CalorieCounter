package com.caloriecounter.calorie.ui.search.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.caloriecounter.calorie.Constant
import com.caloriecounter.calorie.WeatherApplication
import com.caloriecounter.calorie.databinding.ItemSearchHistoryBinding
import com.caloriecounter.calorie.util.PreferenceUtil


class RecentAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private var context: Context? = null
    public var clickItem: ClickItem? = null
    public var clickDeleteItem: ClickDeleteItem? = null
    private var comments: List<String>? = null

    constructor(comments: List<String>, context: Context, clickItem: ClickItem, clickDeleteItem: ClickDeleteItem) {
        this.context = context
        this.clickItem = clickItem
        this.comments = comments
        this.clickDeleteItem = clickDeleteItem
    }

    fun setData(comments: List<String>) {
        this.comments = comments
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        var v: View? = null
        return if (viewType == TYPE_ITEM) {
            val binding: ItemSearchHistoryBinding =
                ItemSearchHistoryBinding.inflate(inflater, parent, false)
            HeaderViewHolder(binding)
        } else {
            val binding: ItemSearchHistoryBinding =
                ItemSearchHistoryBinding.inflate(inflater, parent, false)
            HeaderViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        try {
            if (getItemViewType(position) == TYPE_ITEM) {
                (holder as HeaderViewHolder).bindItem(position)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getItemCount(): Int {
        return try {
            comments!!.size
        } catch (e: Exception) {
            0
        }
    }

    inner class HeaderViewHolder(var binding: ItemSearchHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        private var position: Int? = 0


        init {

        }


        fun bindItem(position: Int?) {
            this.position = position
            binding.tvTitle.text = position?.let { comments?.get(it) }
            binding.root.setOnClickListener {
                WeatherApplication.trackingEvent("Click_Item_Recent")
                clickItem?.onClickItem(position?.let { it1 -> comments?.get(it1) })
            }

            binding.btnDelete.setOnClickListener {
                try {
                    removeRecentSearch(comments!![position!!])
                    clickDeleteItem?.onClickDeleteItem()
                } catch (e: Exception) {
                }
            }
        }
    }


    private fun removeRecentSearch(string: String): ArrayList<String> {
        val arrayListRecentSearchs: ArrayList<String>
        val json = PreferenceUtil.getInstance(context).getValue(Constant.PrefKey.RECENT_SEARCH, "")
        arrayListRecentSearchs = if (json.isEmpty()) {
            ArrayList()
        } else {
            Gson().fromJson(
                json,
                object : TypeToken<java.util.ArrayList<String?>?>() {}.type
            )
        }

        for (i in 0 until arrayListRecentSearchs.size){
            if(arrayListRecentSearchs[i] == string){
                arrayListRecentSearchs.removeAt(i)
                break
            }
        }

        PreferenceUtil.getInstance(context)
            .setValue(Constant.PrefKey.RECENT_SEARCH, Gson().toJson(arrayListRecentSearchs))

        return arrayListRecentSearchs;
    }

    interface ClickItem {
        fun onClickItem(string: String?)
    }

    interface ClickDeleteItem {
        fun onClickDeleteItem ()
    }

    companion object {
        private const val TYPE_ITEM = 0
    }

}