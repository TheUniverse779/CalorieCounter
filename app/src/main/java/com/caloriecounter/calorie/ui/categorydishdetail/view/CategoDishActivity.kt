package com.caloriecounter.calorie.ui.categorydishdetail.view

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.View
import com.caloriecounter.calorie.Constant
import com.caloriecounter.calorie.R
import com.caloriecounter.calorie.base.BaseActivityNew
import com.caloriecounter.calorie.base.BaseFragment
import com.caloriecounter.calorie.databinding.ActivityMainBinding
import com.caloriecounter.calorie.ui.main.model.dish.Dish
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CategoDishActivity : BaseActivityNew<ActivityMainBinding>() {

    private var image : String? = null


    override fun getLayoutRes(): Int {
        return R.layout.activity_main
    }

    override fun getFrame(): Int {
        return R.id.mainFrame
    }

    override fun getDataFromIntent() {
        image = intent.getStringExtra(Constant.IntentKey.DATA)

    }

    override fun doAfterOnCreate() {
        initBanner(binding?.adViewContainer)


    }


    override fun setListener() {
    }

    override fun afterSetContentView() {
        super.afterSetContentView()
        setFullScreen()
    }

    override fun initFragment(): BaseFragment<*>? {
        var categoryDishlFragment = CategoryDishlFragment();
        categoryDishlFragment.setData(image!!)
        return categoryDishlFragment
    }

    private fun setFullScreen() {
        super.afterSetContentView()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.statusBarColor = Color.TRANSPARENT
    }

    public companion object {
        public fun startScreen(context: Context, image: String?) {
            var intent = Intent(context, CategoDishActivity::class.java)
            intent.putExtra(Constant.IntentKey.DATA, image)
            context.startActivity(Intent(intent))
        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onResume() {
        super.onResume()
        initBanner(binding?.adViewContainer)

    }

}