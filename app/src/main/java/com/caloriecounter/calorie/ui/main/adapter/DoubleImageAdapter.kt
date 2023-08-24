package com.caloriecounter.calorie.ui.main.adapter

import android.content.Context
import android.os.Handler
import android.view.*
import android.view.GestureDetector.SimpleOnGestureListener
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Fade
import androidx.transition.Slide
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.bumptech.glide.Glide
import com.caloriecounter.calorie.R
import com.caloriecounter.calorie.WeatherApplication
import com.caloriecounter.calorie.databinding.ItemDoubleImageBinding
import com.caloriecounter.calorie.iinterface.DataChange
import com.caloriecounter.calorie.ui.doubledetail.view.DetailDoubleImageActivity
import com.caloriecounter.calorie.ui.main.model.doubleimage.DoubleImage

class DoubleImageAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private val TYPE_ITEM = 0
    private var context: Context? = null
    private var images: List<DoubleImage>? = null
    private var dataChange: DataChange? = null

    private var handler: Handler? = null
    private var handler1: Handler? = null

    private var centerPosition: Int? = 0;

    constructor()

    constructor(context: Context?, datas: List<DoubleImage>?, dataChange: DataChange?) : super() {
        this.context = context
        this.images = datas
        this.dataChange = dataChange

    }

    public fun setCenterPosition(centerPosition: Int?) {
        this.centerPosition = centerPosition
        if (centerPosition == -1) {
            handler?.removeCallbacksAndMessages(null)
            handler1?.removeCallbacksAndMessages(null)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        var v: View? = null
        return if (viewType == TYPE_ITEM) {
            val binding: ItemDoubleImageBinding =
                ItemDoubleImageBinding.inflate(inflater, parent, false)
            ViewHolder(binding)
        } else {
            val binding: ItemDoubleImageBinding =
                ItemDoubleImageBinding.inflate(inflater, parent, false)
            ViewHolder(binding)
        }
    }

    override fun getItemCount(): Int {
        try {
            if (images!!.isNotEmpty()) {
                dataChange?.onDataChange(false)
                return images!!.size;
            } else {
                dataChange?.onDataChange(true)
                return 0
            }
        } catch (e: Exception) {
            dataChange?.onDataChange(true)
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

    inner class ViewHolder(var binding: ItemDoubleImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private var position: Int? = 0;
        private var isHold = false


        fun bindItem(position: Int) {
            this.position = position

            val link = images!![position].lockVariation.preview_small.url
            Glide.with(context!!).load(link).into(binding.imgAvatar)
            Glide.with(context!!).load(images!![position].homeVariation.preview_small.url)
                .into(binding.imgAvatar2)
            Glide.with(context!!).load(images!![position].homeVariation.preview_small.url)
                .into(binding.imgBackdrop)

            var gestureDetector = GestureDetector(context, SingleTapConfirm())

            itemView.setOnClickListener {
                DetailDoubleImageActivity.startScreen(context!!, images!![position], 0)
                WeatherApplication.trackingEvent("Click_Item_Double")
            }



            if (centerPosition != -1 && position == centerPosition) {
                doScript()
            }

        }

        private fun doScript() {
            handler?.removeCallbacksAndMessages(null)
            handler1?.removeCallbacksAndMessages(null)


            handler = Handler()
            handler?.postDelayed(Runnable {
                val transition: Transition = Slide(Gravity.TOP);
                transition.duration = 350
                transition.addTarget(R.id.layoutImage)

                TransitionManager.beginDelayedTransition(itemView as ViewGroup, transition)
                binding.layoutImage.visibility = View.GONE
                handler1 = Handler()
                handler1?.postDelayed(Runnable {

                    val transition: Transition = Fade();
                    transition.duration = 200
                    transition.addTarget(R.id.layoutImage)

                    TransitionManager.beginDelayedTransition(itemView as ViewGroup, transition)

                    binding.layoutImage.visibility = View.VISIBLE
                    doScript()
                }, 2000)
            }, 500)
        }

    }

    inner class SingleTapConfirm : SimpleOnGestureListener() {
        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            return true
        }

        override fun onDown(e: MotionEvent): Boolean {
            return true
        }
    }

}

