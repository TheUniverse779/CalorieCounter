package com.caloriecounter.calorie.ui.mywallpaper.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.View
import com.caloriecounter.calorie.Constant
import com.caloriecounter.calorie.R
import com.caloriecounter.calorie.ads.InterAds
import com.caloriecounter.calorie.base.BaseActivityNew
import com.caloriecounter.calorie.base.BaseFragment
import com.caloriecounter.calorie.databinding.ActivityMyWallpaperBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyWallpaperActivity : BaseActivityNew<ActivityMyWallpaperBinding?>() {
    private var myWallPaperType : String = Constant.MyWallpaperType.FAVORITE
    override fun getLayoutRes(): Int {
        return R.layout.activity_my_wallpaper
    }

    override fun getFrame(): Int {
        return R.id.mainFrame
    }

    override fun getDataFromIntent() {
        myWallPaperType = intent.getStringExtra(Constant.IntentKey.TYPE_MY_WALLPAPER).toString()
    }
    override fun doAfterOnCreate() {}
    override fun initFragment(): BaseFragment<*>? {
        var myWallpaperFragment = MyWallpaperFragment()
        myWallpaperFragment.setMyWallPaperType(myWallPaperType)
        return myWallpaperFragment
    }

    public companion object {
        public fun startScreen(context: Context, myWallPaperType : String) {
            InterAds.showAdsBreak(context as Activity) {
                var intent = Intent(context, MyWallpaperActivity::class.java)
                intent.putExtra(Constant.IntentKey.TYPE_MY_WALLPAPER, myWallPaperType)
                context.startActivity(Intent(intent))
            }

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