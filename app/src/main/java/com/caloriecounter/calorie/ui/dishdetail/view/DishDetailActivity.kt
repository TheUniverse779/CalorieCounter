package com.caloriecounter.calorie.ui.dishdetail.view

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
import com.caloriecounter.calorie.ui.main.model.image.Image
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DishDetailActivity : BaseActivityNew<ActivityMainBinding>() {

    private var image : Dish? = null


    override fun getLayoutRes(): Int {
        return R.layout.activity_main
    }

    override fun getFrame(): Int {
        return R.id.mainFrame
    }

    override fun getDataFromIntent() {
        image = intent.getSerializableExtra(Constant.IntentKey.DATA) as Dish?

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
        var dishDetailFragment = DishDetailFragment();
        dishDetailFragment.dish = image
        return dishDetailFragment
    }

    private fun setFullScreen() {
        super.afterSetContentView()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.statusBarColor = Color.TRANSPARENT
    }

    public companion object {
        public fun startScreen(context: Context, image: Dish?) {
            var intent = Intent(context, DishDetailActivity::class.java)
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