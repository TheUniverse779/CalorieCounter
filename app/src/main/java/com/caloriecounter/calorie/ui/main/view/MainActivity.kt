package com.caloriecounter.calorie.ui.main.view

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.View
import com.caloriecounter.calorie.Constant
import com.caloriecounter.calorie.R
import com.caloriecounter.calorie.WeatherApplication
import com.caloriecounter.calorie.ads.NativeAdsExitApp
import com.caloriecounter.calorie.base.BaseActivityNew
import com.caloriecounter.calorie.base.BaseFragment
import com.caloriecounter.calorie.databinding.ActivityMainBinding
import com.caloriecounter.calorie.ui.charging.iinterface.PermissionCallback
import com.caloriecounter.calorie.ui.charging.view.PermissionDialog
import com.caloriecounter.calorie.ui.charging.view.TutorialDialog
import com.caloriecounter.calorie.ui.main.model.image.Image
import com.caloriecounter.calorie.ui.slideimage.view.DetailImageActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivityNew<ActivityMainBinding>() {

    private var image : Image? = null


    override fun getLayoutRes(): Int {
        return R.layout.activity_main
    }

    override fun getFrame(): Int {
        return R.id.mainFrame
    }

    override fun getDataFromIntent() {
        image = intent.getSerializableExtra(Constant.IntentKey.DATA) as Image?

    }

    override fun doAfterOnCreate() {
        initBanner(binding?.adViewContainer)

        if (image != null) {
            var listImage = ArrayList<Image>()
            listImage.add(image!!)
            DetailImageActivity.startScreen(
                this,
                null,
                listImage,
                0,
                false,
                null,
                null,
                null,
                null,
                true,
                null,
                false
            )
            WeatherApplication.trackingEvent("Click_noti")
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let {
            image = intent.getSerializableExtra(Constant.IntentKey.DATA) as Image?
            if (image != null) {
                var listImage = ArrayList<Image>()
                listImage.add(image!!)
                DetailImageActivity.startScreen(
                    this,
                    null,
                    listImage,
                    0,
                    false,
                    null,
                    null,
                    null,
                    null,
                    true,
                    null,
                    false
                )
                WeatherApplication.trackingEvent("Click_noti")
            }
        }

    }



    override fun setListener() {
    }

    override fun afterSetContentView() {
        super.afterSetContentView()
        setFullScreen()
    }

    override fun initFragment(): BaseFragment<*>? {
        var homeFragment = HomeFragment();
        return homeFragment
    }

    private fun setFullScreen() {
        super.afterSetContentView()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.statusBarColor = Color.TRANSPARENT
    }

    public companion object {
        public fun startScreen(context: Context, image: Image?) {
            var intent = Intent(context, MainActivity::class.java)
            intent.putExtra(Constant.IntentKey.DATA, image)
            context.startActivity(Intent(intent))
        }

    }

    override fun onBackPressed() {
//        super.onBackPressed()
//        var confirmExitDialog = ConfirmExitDialog();
//        confirmExitDialog.show(supportFragmentManager, null)
        NativeAdsExitApp.showExitDialog(this)
    }

    override fun onResume() {
        super.onResume()
        initBanner(binding?.adViewContainer)

        if (permissionDialog != null && permissionDialog!!.isShowing) {
            permissionDialog!!.update()
        }
    }


    private var permissionDialog: PermissionDialog? = null
    fun showPermission() {
        if (permissionDialog != null && permissionDialog!!.isShowing) {
            return
        }
        permissionDialog = PermissionDialog(this@MainActivity, object : PermissionCallback {
            override fun OnSkip() {
            }

            override fun OnBack(dialog: Dialog?) {
                dialog?.dismiss()
            }
        }, true)
        permissionDialog?.show()
    }

    fun showTutorialDialog() {
        TutorialDialog(this).show()
    }


}