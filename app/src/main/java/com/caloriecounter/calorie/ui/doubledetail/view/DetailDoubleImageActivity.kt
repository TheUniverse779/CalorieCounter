package com.caloriecounter.calorie.ui.doubledetail.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import android.view.View
import android.view.WindowManager
import com.caloriecounter.calorie.Constant
import com.caloriecounter.calorie.R
import com.caloriecounter.calorie.ads.InterAds
import com.caloriecounter.calorie.base.BaseActivityNew
import com.caloriecounter.calorie.base.BaseFragment
import com.caloriecounter.calorie.databinding.ActivityDetailImageBinding
import com.caloriecounter.calorie.ui.main.model.doubleimage.DoubleImage

class DetailDoubleImageActivity : BaseActivityNew<ActivityDetailImageBinding?>() {

    private var currentPosition : Int = 0
    private var doubleImage : DoubleImage? = null

    override fun getLayoutRes(): Int {
        return R.layout.activity_detail_image
    }

    override fun getFrame(): Int {
        return R.id.mainFrame
    }

    override fun getDataFromIntent() {
        currentPosition = intent.getIntExtra(Constant.IntentKey.POSITION, 0)
        doubleImage = intent.getSerializableExtra(Constant.IntentKey.DATA) as DoubleImage?
    }
    override fun doAfterOnCreate() {}
    override fun initFragment(): BaseFragment<*> {
        var doubleImageFragment  = DoubleImageFragment()
        doubleImageFragment.setData(currentPosition, doubleImage!!)
        return doubleImageFragment
    }

    public companion object {
        public fun startScreen(context: Context, doubleImage: DoubleImage, currentPosition : Int) {
            InterAds.showAdsBreak(context as Activity) {
                try {
                    var intent = Intent(context, DetailDoubleImageActivity::class.java)
                    intent.putExtra(Constant.IntentKey.POSITION, currentPosition)
                    intent.putExtra(Constant.IntentKey.DATA, doubleImage)
                    context.startActivity(intent)
                } catch (e: Exception) {
                    Log.e("", "")
                }
            }

        }
    }

    override fun doFirstMethod() {
        super.doFirstMethod()
        if (Build.VERSION.SDK_INT in 19..20) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
        }
        if (Build.VERSION.SDK_INT >= 19) {
            window.decorView.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }

        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            window.statusBarColor = Color.TRANSPARENT
        }
    }

    private fun setWindowFlag(activity: Activity, bits: Int, on: Boolean) {
        val win = activity.window
        val winParams: WindowManager.LayoutParams = win.attributes
        if (on) {
            winParams.flags = winParams.flags or bits
        } else {
            winParams.flags = winParams.flags and bits.inv()
        }
        win.attributes = winParams
    }

    override fun onBackPressed() {
        InterAds.showAdsBreak(this) {
            super.onBackPressed()
        }
    }

    override fun setListener() {

    }
}