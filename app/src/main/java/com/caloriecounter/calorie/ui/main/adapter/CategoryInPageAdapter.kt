package com.caloriecounter.calorie.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.caloriecounter.calorie.Constant
import com.caloriecounter.calorie.WeatherApplication
import com.caloriecounter.calorie.databinding.ItemCategoryInPageBinding
import com.caloriecounter.calorie.iinterface.DataChange
import com.caloriecounter.calorie.ui.categorydetail.view.CategoryDetailActivity
import com.caloriecounter.calorie.ui.main.SpeedyLinearLayoutManager
import com.caloriecounter.calorie.ui.main.model.category.Category
import com.caloriecounter.calorie.ui.main.model.image.Image
import com.caloriecounter.calorie.ui.main.view.CategoryFragment
import com.caloriecounter.calorie.ui.main.viewmodel.WeatherViewModel
import com.caloriecounter.calorie.util.toGone

class CategoryInPageAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private val TYPE_ITEM = 0
    private var context: Context? = null
    private var images: List<Category>? = null
    private var viewModel : WeatherViewModel? = null
    private var categoryFragment : CategoryFragment


    constructor(context: Context?, datas: List<Category>?, viewModel : WeatherViewModel, categoryFragment : CategoryFragment) : super() {
        this.context = context
        this.images = datas
        this.viewModel = viewModel
        this.categoryFragment = categoryFragment
    }


    override fun getItemViewType(position: Int): Int {
        return TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemCategoryInPageBinding = ItemCategoryInPageBinding.inflate(inflater, parent, false)
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

    inner class ViewHolder (var binding: ItemCategoryInPageBinding) : RecyclerView.ViewHolder(binding.root) {
        private var position: Int? = 0;
        private var listImage: ArrayList<Image>? = null
        private var imageAdapter: ImageCategoryInPageAdapter? = null


        init {

        }




        fun bindItem(position: Int) {
            this.position = position
            binding.tvTitle.text = images!![position].title

            viewModel?.dataResponseLiveData?.observe(categoryFragment, Observer {
                if(it.position == position) {
                    images!![position].images = it.items

                    var list = ArrayList<Image>()

                    for (i in 0 until 1000){
                        list.addAll(it.items)
                    }

                    binding.shimmer.toGone()
                    imageAdapter = ImageCategoryInPageAdapter(context, list, images!![position], DataChange {
                        if (it){
                            binding.shimmer.visibility = View.VISIBLE
                        }else{
                            binding.shimmer.visibility = View.GONE
                        }
                    })
                    imageAdapter?.setDataPresentImageType(Constant.PresentImageType.CATEGORY, images!![position].id, Constant.SortBy.DOWNLOAD, null)
                    binding.rclImage.adapter = imageAdapter

                    var layoutManager = SpeedyLinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                    layoutManager.stackFromEnd = position % 2 != 0
                    binding.rclImage.layoutManager = layoutManager
                }
            })
            try {
                if(images!![position].images.size >0){
                    binding.shimmer.toGone()

                    var list = ArrayList<Image>()

                    for (i in 0 until 1000){
                        list.addAll(images!![position].images)
                    }

                    imageAdapter = ImageCategoryInPageAdapter(context, list, images!![position], DataChange {

                    })
                    imageAdapter?.setDataPresentImageType(Constant.PresentImageType.CATEGORY, images!![position].id, Constant.SortBy.DOWNLOAD, null)
                    binding.rclImage.adapter = imageAdapter

                    var layoutManager = SpeedyLinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                    layoutManager.stackFromEnd = position % 2 != 0
                    binding.rclImage.layoutManager = layoutManager
                }else {
                    viewModel?.getImagesByCatIdWithPosition(images!![position].id, Constant.SortBy.DOWNLOAD, null, null,"5", 0, position)
                }
            } catch (e: Exception) {
                viewModel?.getImagesByCatIdWithPosition(images!![position].id, Constant.SortBy.DOWNLOAD, null, null,"5", 0, position)
            }

            binding.root.setOnClickListener {
                CategoryDetailActivity.startScreen(context!!, images!![position])
                WeatherApplication.trackingEvent("Click_Title_Category")
            }

        }


        private fun setData(){

        }

    }

}

