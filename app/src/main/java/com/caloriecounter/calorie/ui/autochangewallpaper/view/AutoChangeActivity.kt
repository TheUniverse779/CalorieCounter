package com.caloriecounter.calorie.ui.autochangewallpaper.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.View
import com.caloriecounter.calorie.R
import com.caloriecounter.calorie.ads.InterAds
import com.caloriecounter.calorie.base.BaseActivityNew
import com.caloriecounter.calorie.base.BaseFragment
import com.caloriecounter.calorie.databinding.ActivityAutoChangeWallpaperBinding


class AutoChangeActivity : BaseActivityNew<ActivityAutoChangeWallpaperBinding?>() {
    override fun getLayoutRes(): Int {
        return R.layout.activity_auto_change_wallpaper
    }

    override fun getFrame(): Int {
        return R.id.mainFrame
    }


    override fun getDataFromIntent() {}
    override fun doAfterOnCreate() {
        initBanner(binding?.adViewContainer)
    }
    override fun initFragment(): BaseFragment<*>? {
        return AutoChangeFragment()
    }

    public companion object {
        public fun startScreen(context: Context) {
            InterAds.showAdsBreak(context as Activity) {
                var intent = Intent(context, AutoChangeActivity::class.java)
                context.startActivity(Intent(intent))
            }

        }
    }

    override fun onBackPressed() {
        InterAds.showAdsBreak(this) {
            super.onBackPressed()
        }
    }

    override fun afterSetContentView() {
        super.afterSetContentView()
        setFullScreen()
    }

    private fun setFullScreen() {
        super.afterSetContentView()
        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.statusBarColor = Color.TRANSPARENT
    }

    override fun setListener() {

    }

    override fun onResume() {
        super.onResume()
        initBanner(binding?.adViewContainer)
    }
}