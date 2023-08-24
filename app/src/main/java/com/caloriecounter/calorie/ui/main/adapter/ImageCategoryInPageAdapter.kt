package com.caloriecounter.calorie.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.caloriecounter.calorie.WeatherApplication
import com.caloriecounter.calorie.databinding.ItemImageCategoryInPageBinding
import com.caloriecounter.calorie.iinterface.DataChange
import com.caloriecounter.calorie.ui.categorydetail.view.CategoryDetailActivity
import com.caloriecounter.calorie.ui.main.model.category.Category
import com.caloriecounter.calorie.ui.main.model.image.Image
import com.caloriecounter.calorie.ui.slideimage.view.DetailImageActivity
import com.caloriecounter.calorie.ui.slideimage.view.WalliveDetailImageActivity
import com.caloriecounter.calorie.util.toGone
import com.caloriecounter.calorie.util.toVisible

class ImageCategoryInPageAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private val TYPE_ITEM = 0
    private val TYPE_SEE_MORE = 1
    private var context: Context? = null
    private var images: List<Image>? = null
    private var dataChange: DataChange? = null
    private var category: Category? = null;

    private var presentImageType: String? = null
    private var catId: String? = null
    private var sortBy: String? = null
    private var typeToGetImage: Array<String>? = null

    constructor()
    constructor(context: Context?, datas: List<Image>?) : super() {
        this.context = context
        this.images = datas
    }

    constructor(
        context: Context?,
        datas: List<Image>?,
        category: Category?,
        dataChange: DataChange?
    ) : super() {
        this.context = context
        this.images = datas
        this.dataChange = dataChange
        this.category = category
    }

    public fun setDataPresentImageType(
        presentImageType: String?,
        catId: String?,
        sortBy: String?,
        typeToGetImage: Array<String>?
    ) {
        this.presentImageType = presentImageType
        this.catId = catId
        this.sortBy = sortBy
        this.typeToGetImage = typeToGetImage
    }


    override fun getItemViewType(position: Int): Int {
        if (position == images!!.size) {
            return TYPE_SEE_MORE
        } else {
            return TYPE_ITEM
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemImageCategoryInPageBinding = ItemImageCategoryInPageBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        try {
            return images!!.size
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

    inner class ViewHolder (var binding: ItemImageCategoryInPageBinding) : RecyclerView.ViewHolder(binding.root) {
        private var position: Int? = 0;


        init {

        }

        fun bindItem(position: Int) {
            this.position = position
            val link = images!![position].variations.preview_small.url
            Glide.with(context!!).load(link).into(binding.imgAvatar)
            itemView.setOnClickListener {
                WeatherApplication.trackingEvent("Click_Image_In_category_inPage")
                var listImage = ArrayList<Image>()
                listImage.add(images!![position])
                WalliveDetailImageActivity.startScreen(
                    context!!,
                    listImage,
                    true, images!![position]
                )
            }
//            if(position == images!!.size - 1){
//                binding.layoutCover.toVisible()
//
//                itemView.setOnClickListener {
//                    WeatherApplication.trackingEvent("Click_See_more")
//                    CategoryDetailActivity.startScreen(context!!, category)
//                }
//            }else{
//                binding.layoutCover.toGone()
//            }

        }

    }


}

