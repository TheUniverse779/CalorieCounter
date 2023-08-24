package com.caloriecounter.calorie.ui.preview

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowManager
import com.bumptech.glide.Glide
import com.caloriecounter.calorie.R
import com.caloriecounter.calorie.ads.InterAds
import com.caloriecounter.calorie.base.BaseActivityNew
import com.caloriecounter.calorie.base.BaseFragment
import com.caloriecounter.calorie.databinding.ActivityPreviewBinding

class PreviewActivity : BaseActivityNew<ActivityPreviewBinding?>() {
    var link = "";
    override fun getLayoutRes(): Int {
        return R.layout.activity_preview
    }

    override fun getFrame(): Int {
        return 0
    }

    override fun getDataFromIntent() {
        link = intent?.getStringExtra("data")!!
        Glide.with(this).load(link).into(binding?.imgPreview!!)
    }
    override fun doAfterOnCreate() {
        binding?.btnBack?.setOnClickListener {
            onBackPressed()
        }
    }

    override fun setListener() {

    }

    override fun initFragment(): BaseFragment<*>? {
        return null
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

    companion object {
        fun startScreen(context: Context, link: String?) {
            InterAds.showAdsBreak(context as Activity) {
                val intent = Intent(context, PreviewActivity::class.java)
                intent.putExtra("data", link)
                context.startActivity(intent)
            }
        }


    }
}