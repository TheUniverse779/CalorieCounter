package com.caloriecounter.calorie.ui.slideimage.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import com.caloriecounter.calorie.Constant
import com.caloriecounter.calorie.R
import com.caloriecounter.calorie.base.BaseActivityNew
import com.caloriecounter.calorie.base.BaseFragment
import com.caloriecounter.calorie.databinding.ActivityDetailImageBinding
import com.caloriecounter.calorie.ui.main.model.image.Image
import com.caloriecounter.calorie.util.PreferenceUtil
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.caloriecounter.calorie.ads.InterAds
import com.caloriecounter.calorie.ui.slideimage.event.OnDialogDismiss
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

@AndroidEntryPoint
class DetailImageActivity : BaseActivityNew<ActivityDetailImageBinding?>() {
    private var listImage: ArrayList<Image>? = null

    private var catId : String? = null
    private var typeToGetImage: Array<String>? = null
    private var sortBy: String? = null
    private var presentImageType: String? = null
    private var keyword: String? = null


    private var currentPosition : Int = 0
    private var loadMoreAble : Boolean = true
    private var isFromNotification : Boolean = false
    private var ids : Array<String>? = null
    private var resultPosition : Int = 0
    override fun getLayoutRes(): Int {
        return R.layout.activity_detail_image
    }

    override fun getFrame(): Int {
        return R.id.mainFrame
    }

    override fun getDataFromIntent() {
        listImage = intent.getSerializableExtra(Constant.IntentKey.LIST_IMAGE) as ArrayList<Image>?
        catId = intent.getStringExtra(Constant.IntentKey.CATE_ID)
        typeToGetImage = intent.getStringArrayExtra(Constant.IntentKey.TYPE_TO_GET_IMAGE)
        sortBy = intent.getStringExtra(Constant.IntentKey.SORT_BY)
        presentImageType = intent.getStringExtra(Constant.IntentKey.PRESENT_IMAGE_TYPE)
        keyword = intent.getStringExtra(Constant.IntentKey.KEYWORD)
        currentPosition = intent.getIntExtra(Constant.IntentKey.POSITION, 0)
        loadMoreAble = intent.getBooleanExtra(Constant.IntentKey.LOAD_MORE_ABLE, true)
        isFromNotification = intent.getBooleanExtra(Constant.IntentKey.IS_FROM_NOTIFICATION, false)
        ids = intent.getStringArrayExtra(Constant.IntentKey.IDS)
    }
    override fun doAfterOnCreate() {
        initBanner(binding?.adViewContainer)
    }
    override fun initFragment(): BaseFragment<*> {
        var imageDetailFragment  = ImageDetailFragment()
        imageDetailFragment.setData(catId, typeToGetImage, sortBy, presentImageType,keyword, getListImageFromCache(), resultPosition, loadMoreAble, isFromNotification, ids)
        return imageDetailFragment
    }

    public companion object {
        public fun startScreen(context: Context,
                               catId: String?,
                               listImage: ArrayList<Image>,
                               currentPosition : Int,
                               loadMoreAble : Boolean,
                               sortBy: String?,
                               typeToGetImage: Array<String>?,
                               presentImageType: String?,
                               keyword: String?,
                               isFromNotification : Boolean,
                               ids : Array<String>?,
                               canShowAds : Boolean
        ) {
            if(canShowAds){
                InterAds.showAdsBreak(context as Activity){
                    try {
                        var intent = Intent(context, DetailImageActivity::class.java)
                        var bundle = Bundle()
                        bundle.putString(Constant.IntentKey.CATE_ID, catId)
                        bundle.putString(Constant.IntentKey.SORT_BY, sortBy)
                        bundle.putStringArray(Constant.IntentKey.TYPE_TO_GET_IMAGE, typeToGetImage)
                        bundle.putString(Constant.IntentKey.PRESENT_IMAGE_TYPE, presentImageType)
                        bundle.putString(Constant.IntentKey.KEYWORD, keyword)
                        bundle.putBoolean(Constant.IntentKey.IS_FROM_NOTIFICATION, isFromNotification)
                        bundle.putStringArray(Constant.IntentKey.IDS, ids)

                        PreferenceUtil.getInstance(context).setValue(Constant.PrefKey.LIST_IMAGE, Gson().toJson(listImage))
                        bundle.putInt(Constant.IntentKey.POSITION, currentPosition)
                        bundle.putBoolean(Constant.IntentKey.LOAD_MORE_ABLE, loadMoreAble)
                        intent.putExtras(bundle)
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        Log.e("", "")
                    }
                }
            }else{
                try {
                    var intent = Intent(context, DetailImageActivity::class.java)
                    var bundle = Bundle()
                    bundle.putString(Constant.IntentKey.CATE_ID, catId)
                    bundle.putString(Constant.IntentKey.SORT_BY, sortBy)
                    bundle.putStringArray(Constant.IntentKey.TYPE_TO_GET_IMAGE, typeToGetImage)
                    bundle.putString(Constant.IntentKey.PRESENT_IMAGE_TYPE, presentImageType)
                    bundle.putString(Constant.IntentKey.KEYWORD, keyword)
                    bundle.putBoolean(Constant.IntentKey.IS_FROM_NOTIFICATION, isFromNotification)
                    bundle.putStringArray(Constant.IntentKey.IDS, ids)

                    PreferenceUtil.getInstance(context).setValue(Constant.PrefKey.LIST_IMAGE, Gson().toJson(listImage))
                    bundle.putInt(Constant.IntentKey.POSITION, currentPosition)
                    bundle.putBoolean(Constant.IntentKey.LOAD_MORE_ABLE, loadMoreAble)
                    intent.putExtras(bundle)
                    context.startActivity(intent)
                } catch (e: Exception) {
                    Log.e("", "")
                }
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
        val json = PreferenceUtil.getInstance(this).getValue(Constant.PrefKey.LIST_IMAGE, "")
        resultList = if (json.isEmpty()) {
            ArrayList()
        } else {
            Gson().fromJson(json,
                    object : TypeToken<java.util.ArrayList<Image?>?>() {}.type)
        }

        var image = resultList[currentPosition]

        var result = ArrayList<Image>()

        for (i in 0 until resultList.size){
            if(resultList[i].itemType == 0){
                result.add(resultList[i])
            }
        }

        for (i in 0 until result.size){
            if(image.id == result[i].id){
                resultPosition = i
                break
            }
        }

        return result;
    }

    override fun onBackPressed() {
        InterAds.showAdsBreak(this){
            super.onBackPressed()
        }
    }

    override fun setListener() {
    }


    fun FullScreencall() {
        try {
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
        } catch (e: Exception) {
        }
    }


    override fun onResume() {
        super.onResume()
        FullScreencall()
        initBanner(binding?.adViewContainer)
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



}