package com.caloriecounter.calorie.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.caloriecounter.calorie.R
import com.caloriecounter.calorie.WeatherApplication
import com.caloriecounter.calorie.databinding.ItemHeaderWallpaperBinding
import com.caloriecounter.calorie.databinding.ItemImageBinding
import com.caloriecounter.calorie.ui.main.adapter.viewholder.AdsViewHolder
import com.caloriecounter.calorie.ui.main.model.image.Image
import com.caloriecounter.calorie.ui.slideimage.view.WalliveDetailImageActivity
import com.caloriecounter.calorie.util.toGone
import com.caloriecounter.calorie.util.toVisible
import kotlinx.android.synthetic.main.item_live_wallpaper.view.imgAvatar

class ImageAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public val TYPE_ITEM = 0
    public val TYPE_PROMOTION = 1
    public val TYPE_ADS = 2
    public val TYPE_HEADER = 3
    private var context: Context? = null
    private var images: List<Image>? = null
    private var canLoadMoreWhenClickToDetail: Boolean = true

    private var presentImageType: String? = null
    private var catId: String? = null
    private var sortBy: String? = null
    private var typeToGetImage: Array<String>? = null
    private var keyword: String? = null

    private var isShowPromotion = false


    private var canLoadSimilarWhenGoDetail: Boolean = true
    constructor()
    constructor(context: Context?, datas: List<Image>?) : super() {
        this.context = context
        this.images = datas
    }

    constructor(
        context: Context?,
        datas: List<Image>?,
        canLoadMoreWhenClickToDetail: Boolean,
        isShowPromotion: Boolean,
        canLoadSimilarWhenGoDetail: Boolean
    ) : super() {
        this.context = context
        this.images = datas
        this.canLoadMoreWhenClickToDetail = canLoadMoreWhenClickToDetail
        this.isShowPromotion = isShowPromotion
        this.canLoadSimilarWhenGoDetail = canLoadSimilarWhenGoDetail
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

    public fun setDataPresentImageType(keyword: String?) {
        this.keyword = keyword
    }


    override fun getItemViewType(position: Int): Int {
//        if (isShowPromotion) {
//            if (position == 0) {
//                return TYPE_PROMOTION
//            } else {
//                if(position % 10 == 0){
//                    return TYPE_ADS
//                }else {
//                    return TYPE_ITEM
//                }
//            }
//        } else {
//            if(position % 10 == 0){
//                return TYPE_ADS
//            }else {
//                return TYPE_ITEM
//            }
//        }

        if(images!![position].itemType == TYPE_PROMOTION){
            return TYPE_PROMOTION
        }else if(images!![position].itemType == TYPE_ADS){
            return TYPE_ADS
        }else{
            return TYPE_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        var v: View? = null
        return if (viewType == TYPE_ITEM) {
            val binding: ItemImageBinding = ItemImageBinding.inflate(inflater, parent, false)
            ViewHolder(binding)
        } else if (viewType == TYPE_ADS) {
            val v: View =
                LayoutInflater.from(parent.context).inflate(R.layout.layout_ads, parent, false)
            AdsViewHolder(v, context)
        }else{
            val binding: ItemHeaderWallpaperBinding = ItemHeaderWallpaperBinding.inflate(inflater, parent, false)
            PromotionViewHolder(binding)
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
            } else if (getItemViewType(position) == TYPE_PROMOTION) {
                (holder as PromotionViewHolder).bindItem(position)
            }else if(getItemViewType(position) == TYPE_ADS){
                (holder as AdsViewHolder).bind(R.layout.ad_unified, images, position)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    inner class ViewHolder (var binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root) {
        private var position: Int? = 0;

        init {

        }

        fun bindItem(position: Int) {
            this.position = position

//            if(images!![position].content_type.equals("private")){
//                binding.imgShowAds.toVisible()
//            }else{
//                binding.imgShowAds.toGone()
//            }

            if(images!![position].videoVariations != null){
                val linkVideo = images!![position].imageVariations.preview_small.url
                Glide.with(context!!).load(linkVideo).into(itemView.imgAvatar)
                binding.imgVideo.toVisible()


                binding.root.setOnClickListener {
                    WeatherApplication.trackingEvent("Click_Item_Image")
                    WalliveDetailImageActivity.startScreen(
                        context!!,
                        images as ArrayList<Image>, canLoadSimilarWhenGoDetail, images!![position]
                    )
                }
            }else{
                val link = images!![position].variations.preview_small.url
                Glide.with(context!!).load(link).into(binding.imgAvatar)
                binding.imgVideo.toGone()


                binding.root.setOnClickListener {
                    WeatherApplication.trackingEvent("Click_Item_Image")
                    if(canLoadSimilarWhenGoDetail) {
                        var listImage = ArrayList<Image>()
                        listImage.add(images!![position])
                        WalliveDetailImageActivity.startScreen(
                            context!!,
                            listImage,
                            canLoadSimilarWhenGoDetail, null
                        )
                    }else{
                        WalliveDetailImageActivity.startScreen(
                            context!!,
                            images as ArrayList<Image>,
                            canLoadSimilarWhenGoDetail, images!![position]
                        )
                    }
                }
            }

            if(images!![position].cost.toInt() > 0){
                binding.viewCoin.toVisible()
            }else{
                binding.viewCoin.toGone()
            }


//            binding.tvCoin.text =





        }

    }

    inner class PromotionViewHolder(var binding: ItemHeaderWallpaperBinding) : RecyclerView.ViewHolder(binding.root) {
        private var position: Int? = 0;

        fun bindItem(position: Int) {
            this.position = position
            Glide.with(context!!).load(images!![position].images[0].variations.preview_small.url).into(binding.img1)
            Glide.with(context!!).load(images!![position].images[1].variations.preview_small.url).into(binding.img2)
            Glide.with(context!!).load(images!![position].images[2].variations.preview_small.url).into(binding.img3)

//            binding.tvCoin1.text = images!![position].images[0].cost
//            binding.tvCoin2.text = images!![position].images[1].cost
//            binding.tvCoin3.text = images!![position].images[2].cost


            if(images!![position].images[0].cost.toInt() > 0){
                binding.viewCoin.toVisible()
            }else{
                binding.viewCoin.toGone()
            }

            if(images!![position].images[1].cost.toInt() > 0){
                binding.viewCoin2.toVisible()
            }else{
                binding.viewCoin2.toGone()
            }

            if(images!![position].images[2].cost.toInt() > 0){
                binding.viewCoin3.toVisible()
            }else{
                binding.viewCoin3.toGone()
            }

            binding.img1.setOnClickListener {
                WeatherApplication.trackingEvent("Click_Item_Image")
                var listImage = ArrayList<Image>()
                listImage.add(images!![position].images[0])
                WalliveDetailImageActivity.startScreen(
                    context!!,
                    listImage, true, null
                )
            }

            binding.img2.setOnClickListener {
                WeatherApplication.trackingEvent("Click_Item_Image")
                var listImage = ArrayList<Image>()
                listImage.add(images!![position].images[1])
                WalliveDetailImageActivity.startScreen(
                    context!!,
                    listImage, true, null
                )
            }

            binding.img3.setOnClickListener {
                WeatherApplication.trackingEvent("Click_Item_Image")
                var listImage = ArrayList<Image>()
                listImage.add(images!![position].images[2])
                WalliveDetailImageActivity.startScreen(
                    context!!,
                    listImage, true, null
                )
            }


        }

    }

}

