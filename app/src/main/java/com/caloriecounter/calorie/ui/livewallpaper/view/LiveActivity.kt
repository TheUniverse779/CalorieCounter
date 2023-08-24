package com.caloriecounter.calorie.ui.livewallpaper.view

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.View
import com.caloriecounter.calorie.R
import com.caloriecounter.calorie.ads.InterAds
import com.caloriecounter.calorie.base.BaseActivityNew
import com.caloriecounter.calorie.base.BaseFragment
import com.caloriecounter.calorie.databinding.ActivityLiveWallpaperBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LiveActivity : BaseActivityNew<ActivityLiveWallpaperBinding?>() {
    override fun getLayoutRes(): Int {
        return R.layout.activity_live_wallpaper
    }

    override fun getFrame(): Int {
        return R.id.mainFrame
    }

    override fun getDataFromIntent() {}
    override fun doAfterOnCreate() {}
    override fun initFragment(): BaseFragment<*>? {
        var liveWallpaperFragment = LiveWallpaperFragment()
        liveWallpaperFragment.setShowToolbar(true)
        return liveWallpaperFragment
    }

    public companion object {
        public fun startScreen(context: Context) {
            var intent = Intent(context, LiveActivity::class.java)
            context.startActivity(Intent(intent))
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


    override fun onBackPressed() {
        InterAds.showAdsBreak(this) {
            super.onBackPressed()
        }
    }

    override fun setListener() {

    }
}