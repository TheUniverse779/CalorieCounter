package com.caloriecounter.calorie.ui.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.caloriecounter.calorie.R
import com.caloriecounter.calorie.WeatherApplication
import com.caloriecounter.calorie.databinding.ItemHeaderWallpaper2Binding
import com.caloriecounter.calorie.databinding.ItemHeaderWallpaperBinding
import com.caloriecounter.calorie.databinding.ItemImageBinding
import com.caloriecounter.calorie.ui.main.adapter.viewholder.AdsViewHolder
import com.caloriecounter.calorie.ui.main.model.image.Image
import com.caloriecounter.calorie.ui.slideimage.view.WalliveDetailImageActivity
import com.caloriecounter.calorie.util.toGone
import com.caloriecounter.calorie.util.toVisible
import kotlinx.android.synthetic.main.item_live_wallpaper.view.imgAvatar

class ImageHomeAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public val TYPE_ITEM_1 = 0
    public val TYPE_ITEM_2 = 1
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
        if(position % 2 == 0){
            return TYPE_ITEM_1
        }else{
            return TYPE_ITEM_2
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        var v: View? = null
        return if (viewType == TYPE_ITEM_1) {
            val binding: ItemHeaderWallpaperBinding = ItemHeaderWallpaperBinding.inflate(inflater, parent, false)
            ViewHolder(binding)
        }else{
            val binding: ItemHeaderWallpaper2Binding = ItemHeaderWallpaper2Binding.inflate(inflater, parent, false)
            ViewHolder2(binding)
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
            if (getItemViewType(position) == TYPE_ITEM_1) {
                (holder as ViewHolder).bindItem(position)
            }else{
                (holder as ViewHolder2).bindItem(position)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    inner class ViewHolder (var binding: ItemHeaderWallpaperBinding) : RecyclerView.ViewHolder(binding.root) {
        private var position: Int? = 0;

        init {

        }

        fun bindItem(position: Int) {
            this.position = position
            Glide.with(context!!).load(images!![position].images[0].variations.preview_small.url).into(binding.img1)
            Glide.with(context!!).load(images!![position].images[1].variations.preview_small.url).into(binding.img2)
            Glide.with(context!!).load(images!![position].images[2].variations.preview_small.url).into(binding.img3)
            Glide.with(context!!).load(images!![position].images[3].variations.preview_small.url).into(binding.img4)
            Glide.with(context!!).load(images!![position].images[4].variations.preview_small.url).into(binding.img5)
            Glide.with(context!!).load(images!![position].images[5].variations.preview_small.url).into(binding.img6)
            Glide.with(context!!).load(images!![position].images[6].variations.preview_small.url).into(binding.img7)
            Glide.with(context!!).load(images!![position].images[7].variations.preview_small.url).into(binding.img8)
            Glide.with(context!!).load(images!![position].images[8].variations.preview_small.url).into(binding.img9)


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

            if(images!![position].images[3].cost.toInt() > 0){
                binding.viewCoin4.toVisible()
            }else{
                binding.viewCoin4.toGone()
            }

            if(images!![position].images[4].cost.toInt() > 0){
                binding.viewCoin5.toVisible()
            }else{
                binding.viewCoin5.toGone()
            }

            if(images!![position].images[5].cost.toInt() > 0){
                binding.viewCoin6.toVisible()
            }else{
                binding.viewCoin6.toGone()
            }

            if(images!![position].images[6].cost.toInt() > 0){
                binding.viewCoin7.toVisible()
            }else{
                binding.viewCoin7.toGone()
            }

            if(images!![position].images[7].cost.toInt() > 0){
                binding.viewCoin8.toVisible()
            }else{
                binding.viewCoin8.toGone()
            }

            if(images!![position].images[8].cost.toInt() > 0){
                binding.viewCoin9.toVisible()
            }else{
                binding.viewCoin9.toGone()
            }

            binding.img1.setOnClickListener {
                clickImage(position, 0)
            }

            binding.img2.setOnClickListener {
                clickImage(position, 1)
            }

            binding.img3.setOnClickListener {
                clickImage(position, 2)
            }

            binding.img4.setOnClickListener {
                clickImage(position, 3)
            }
            binding.img5.setOnClickListener {
                clickImage(position, 4)
            }
            binding.img6.setOnClickListener {
                clickImage(position, 5)
            }
            binding.img7.setOnClickListener {
                clickImage(position, 6)
            }
            binding.img8.setOnClickListener {
                clickImage(position, 7)
            }
            binding.img9.setOnClickListener {
                clickImage(position, 8)
            }


        }


        private fun clickImage(positionParrent : Int, position : Int){
            WeatherApplication.trackingEvent("Click_Item_Image")
            var listImage = ArrayList<Image>()
            listImage.add(images!![positionParrent].images[position])
            WalliveDetailImageActivity.startScreen(
                context!!,
                listImage, true, null
            )
        }

    }


    inner class ViewHolder2 (var binding: ItemHeaderWallpaper2Binding) : RecyclerView.ViewHolder(binding.root) {
        private var position: Int? = 0;

        init {

        }

        fun bindItem(position: Int) {
            Glide.with(context!!).load(images!![position].images[0].variations.preview_small.url).into(binding.img1)
            Glide.with(context!!).load(images!![position].images[1].variations.preview_small.url).into(binding.img2)
            Glide.with(context!!).load(images!![position].images[2].variations.preview_small.url).into(binding.img3)
            Glide.with(context!!).load(images!![position].images[3].variations.preview_small.url).into(binding.img4)
            Glide.with(context!!).load(images!![position].images[4].variations.preview_small.url).into(binding.img5)
            Glide.with(context!!).load(images!![position].images[5].variations.preview_small.url).into(binding.img6)
            Glide.with(context!!).load(images!![position].images[6].variations.preview_small.url).into(binding.img7)
            Glide.with(context!!).load(images!![position].images[7].variations.preview_small.url).into(binding.img8)
            Glide.with(context!!).load(images!![position].images[8].variations.preview_small.url).into(binding.img9)


            if(images!![position].images[0].cost.toInt() > 0){
                binding.viewCoin1.toVisible()
            }else{
                binding.viewCoin1.toGone()
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

            if(images!![position].images[3].cost.toInt() > 0){
                binding.viewCoin4.toVisible()
            }else{
                binding.viewCoin4.toGone()
            }

            if(images!![position].images[4].cost.toInt() > 0){
                binding.viewCoin5.toVisible()
            }else{
                binding.viewCoin5.toGone()
            }

            if(images!![position].images[5].cost.toInt() > 0){
                binding.viewCoin6.toVisible()
            }else{
                binding.viewCoin6.toGone()
            }

            if(images!![position].images[6].cost.toInt() > 0){
                binding.viewCoin7.toVisible()
            }else{
                binding.viewCoin7.toGone()
            }

            if(images!![position].images[7].cost.toInt() > 0){
                binding.viewCoin8.toVisible()
            }else{
                binding.viewCoin8.toGone()
            }

            if(images!![position].images[8].cost.toInt() > 0){
                binding.viewCoin9.toVisible()
            }else{
                binding.viewCoin9.toGone()
            }

            binding.img1.setOnClickListener {
                clickImage(position, 0)
            }

            binding.img2.setOnClickListener {
                clickImage(position, 1)
            }

            binding.img3.setOnClickListener {
                clickImage(position, 2)
            }

            binding.img4.setOnClickListener {
                clickImage(position, 3)
            }
            binding.img5.setOnClickListener {
                clickImage(position, 4)
            }
            binding.img6.setOnClickListener {
                clickImage(position, 5)
            }
            binding.img7.setOnClickListener {
                clickImage(position, 6)
            }
            binding.img8.setOnClickListener {
                clickImage(position, 7)
            }
            binding.img9.setOnClickListener {
                clickImage(position, 8)
            }
        }

        private fun clickImage(positionParrent : Int, position : Int){
            WeatherApplication.trackingEvent("Click_Item_Image")
            var listImage = ArrayList<Image>()
            listImage.add(images!![positionParrent].images[position])
            WalliveDetailImageActivity.startScreen(
                context!!,
                listImage, true, null
            )
        }

    }


}

