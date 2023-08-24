package com.caloriecounter.calorie.ui.categorydetail.view

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
import com.caloriecounter.calorie.databinding.ActivityCategoryDetailBinding
import com.caloriecounter.calorie.ui.main.model.category.Category
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoryDetailActivity : BaseActivityNew<ActivityCategoryDetailBinding>() {


    private var category: Category? = null
    override fun getLayoutRes(): Int {
        return R.layout.activity_category_detail
    }

    override fun getFrame(): Int {
        return R.id.mainFrame
    }

    override fun getDataFromIntent() {
        category = intent.getSerializableExtra(Constant.IntentKey.CATEGORY) as Category
    }

    override fun doAfterOnCreate() {
        initBanner(binding?.adViewContainer)
    }
    override fun initFragment(): BaseFragment<*>? {
        var categoryDetailFragment = CategoryDetailTabFragment()
        categoryDetailFragment.setData(category, 0, null, Constant.SortBy.DOWNLOAD, Constant.PresentImageType.CATEGORY)
        return categoryDetailFragment
    }


    public companion object {
        public fun startScreen(context: Context, category: Category?) {
            InterAds.showAdsBreak(context as Activity) {
                var intent = Intent(context, CategoryDetailActivity::class.java)
                intent.putExtra(Constant.IntentKey.CATEGORY, category)
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




    /** Called when returning to the activity  */
    public override fun onResume() {
        super.onResume()
        initBanner(binding?.adViewContainer)
    }

    /** Called before the activity is destroyed  */
    public override fun onDestroy() {
        
        super.onDestroy()
    }

    override fun onBackPressed() {
        InterAds.showAdsBreak(this) {
            super.onBackPressed()
        }
    }

    override fun setListener() {
    }
}