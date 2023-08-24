package com.caloriecounter.calorie.ui.similar

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.caloriecounter.calorie.Constant
import com.caloriecounter.calorie.R
import com.caloriecounter.calorie.ads.InterAds
import com.caloriecounter.calorie.base.BaseActivityNew
import com.caloriecounter.calorie.base.BaseFragment
import com.caloriecounter.calorie.databinding.ActivityDetailImageBinding
import com.caloriecounter.calorie.ui.main.model.image.Image
import com.caloriecounter.calorie.ui.slideimage.event.OnDialogDismiss
import com.caloriecounter.calorie.util.PreferenceUtil
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

@AndroidEntryPoint
class DetailSimilarImageActivity : BaseActivityNew<ActivityDetailImageBinding?>() {
    private var currentPosition : Int = 0
    private var imageId : String? = null
    override fun getLayoutRes(): Int {
        return R.layout.activity_detail_image
    }

    override fun getFrame(): Int {
        return R.id.mainFrame
    }

    override fun getDataFromIntent() {
        currentPosition = intent.getIntExtra(Constant.IntentKey.POSITION, 0)
        imageId = intent.getStringExtra(Constant.IntentKey.IMAGE_ID)
    }
    override fun doAfterOnCreate() {}
    override fun initFragment(): BaseFragment<*> {
        var imageDetailFragment  = ImageSimilarDetailFragment()
        imageDetailFragment.setData( getListImageFromCache(), currentPosition, imageId)
        return imageDetailFragment
    }

    public companion object {
        public fun startScreen(context: Context,
                               listImage: ArrayList<Image>,
                               currentPosition : Int,
                               imageId : String?
        ) {
            try {
                var intent = Intent(context, DetailSimilarImageActivity::class.java)
                var bundle = Bundle()

                PreferenceUtil.getInstance(context).setValue(Constant.PrefKey.LIST_SIMILAR_IMAGE, Gson().toJson(listImage))
                bundle.putInt(Constant.IntentKey.POSITION, currentPosition)
                bundle.putString(Constant.IntentKey.IMAGE_ID, imageId)
                intent.putExtras(bundle)
                context.startActivity(intent)
            } catch (e: Exception) {
                Log.e("", "")
            }
        }
    }

    override fun doFirstMethod() {
        super.doFirstMethod()
//        if (Build.VERSION.SDK_INT in 19..20) {
//            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true)
//        }
//        if (Build.VERSION.SDK_INT >= 19) {
//            window.decorView.systemUiVisibility =
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//        }
//
//        if (Build.VERSION.SDK_INT >= 21) {
//            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
//            window.statusBarColor = Color.TRANSPARENT
//        }
        FullScreencall()
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


    private fun getListImageFromCache() : ArrayList<Image>{
        val resultList: ArrayList<Image>
        val json = PreferenceUtil.getInstance(this).getValue(Constant.PrefKey.LIST_SIMILAR_IMAGE, "")
        resultList = if (json.isEmpty()) {
            ArrayList()
        } else {
            Gson().fromJson(json,
                    object : TypeToken<java.util.ArrayList<Image?>?>() {}.type)
        }

        return resultList;
    }

    override fun onBackPressed() {
        InterAds.showAdsBreak(this){
            super.onBackPressed()
        }
    }

    override fun setListener() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }


    @Subscribe
    public fun onDialogDismiss(onDialogDismiss: OnDialogDismiss){
        FullScreencall()
    }

    fun FullScreencall() {
        val decorView2 = window.decorView
        decorView2.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                or View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                or View.SYSTEM_UI_FLAG_IMMERSIVE)
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            val v = this.window.decorView
            v.systemUiVisibility = View.GONE
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            val decorView = window.decorView
            val uiOptions =
                (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        or View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        or View.SYSTEM_UI_FLAG_IMMERSIVE)
            decorView.systemUiVisibility = uiOptions
        }
        decorView2.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
    }


    override fun onResume() {
        super.onResume()
        initBanner(binding?.adViewContainer)
    }
}