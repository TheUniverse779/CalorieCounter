package com.caloriecounter.calorie.ui.livewallpaper.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowManager
import com.caloriecounter.calorie.Constant
import com.caloriecounter.calorie.R
import com.caloriecounter.calorie.ads.InterAds
import com.caloriecounter.calorie.base.BaseActivityNew
import com.caloriecounter.calorie.base.BaseFragment
import com.caloriecounter.calorie.databinding.ActivityLiveWallpaperBinding
import com.caloriecounter.calorie.ui.main.model.image.Image
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LiveWallpaperDetailActivity : BaseActivityNew<ActivityLiveWallpaperBinding?>() {
    private var image : Image? = null
    override fun getLayoutRes(): Int {
        return R.layout.activity_live_wallpaper
    }

    override fun getFrame(): Int {
        return R.id.mainFrame
    }

    override fun getDataFromIntent() {
        image = intent.getSerializableExtra(Constant.IntentKey.DATA) as Image
    }
    override fun doAfterOnCreate() {}
    override fun initFragment(): BaseFragment<*> {
        var liveWallpaperDetailFragment = LiveWallpaperDetailFragment()
        liveWallpaperDetailFragment.setData(image)
        return liveWallpaperDetailFragment
    }

    public companion object {
        public fun startScreen(context: Context, image : Image) {
            InterAds.showAdsBreak(context as Activity) {
                var intent = Intent(context, LiveWallpaperDetailActivity::class.java)
                intent.putExtra(Constant.IntentKey.DATA, image)
                context.startActivity(Intent(intent))
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

    override fun setListener() {

    }
}