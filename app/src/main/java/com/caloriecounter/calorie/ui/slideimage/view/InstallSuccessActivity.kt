package com.caloriecounter.calorie.ui.slideimage.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.View
import android.view.WindowManager
import com.caloriecounter.calorie.base.BaseActivityNew
import com.caloriecounter.calorie.R
import com.caloriecounter.calorie.ads.Callback
import com.caloriecounter.calorie.ads.InterAds
import com.caloriecounter.calorie.base.BaseFragment
import com.caloriecounter.calorie.databinding.ActivityInstallSucessBinding

class InstallSuccessActivity : BaseActivityNew<ActivityInstallSucessBinding?>() {
    private var type : Int = 0;
    override fun getLayoutRes(): Int {
        return R.layout.activity_install_sucess
    }

    override fun getFrame(): Int {
        return R.id.mainFrame
    }

    override fun getDataFromIntent() {
        type = intent.getIntExtra("type", 0)

        if(type == 0){
            binding?.tvMessage?.text = resources.getString(R.string.set_image_as_main_screen_success)
        }else if(type == 1){
            binding?.tvMessage?.text = resources.getString(R.string.set_image_as_lock_screen_successfully)
        }else{
            binding?.tvMessage?.text = resources.getString(R.string.set_as_both_success)
        }
    }
    override fun doAfterOnCreate() {}
    override fun setListener() {
        binding?.btnContinue?.setOnClickListener {
            onBackPressed()
        }
    }
    override fun initFragment(): BaseFragment<*>? {
        return null
    }

    override fun afterSetContentView() {
        super.afterSetContentView()


    }

    override fun doFirstMethod() {
        super.doFirstMethod()
        FullScreencall()
    }
    public companion object {
        public fun startScreen(context: Context, type : Int) {
            InterAds.showAdsBreak(context as Activity, Callback {
                var intent = Intent(context, InstallSuccessActivity::class.java)
                intent.putExtra("type", type)
                context.startActivity(Intent(intent))
            })

        }

    }


    override fun onBackPressed() {
        InterAds.showAdsBreak(this){
            super.onBackPressed()
        }
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
}