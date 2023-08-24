package com.caloriecounter.calorie.ui.search.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.View
import com.caloriecounter.calorie.R
import com.caloriecounter.calorie.ads.InterAds
import com.caloriecounter.calorie.base.BaseActivityNew
import com.caloriecounter.calorie.base.BaseFragment
import com.caloriecounter.calorie.databinding.ActivitySearchBinding
import com.caloriecounter.calorie.util.PreferenceUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchActivity : BaseActivityNew<ActivitySearchBinding>() {

    @Inject
    lateinit var preferenceUtil: PreferenceUtil
    private var keyword : String? = null


    override fun getLayoutRes(): Int {
        return R.layout.activity_search
    }

    override fun getFrame(): Int {
        return R.id.mainFrame
    }

    override fun getDataFromIntent() {
        keyword = intent.getStringExtra("data")
    }

    override fun doAfterOnCreate() {
        initBanner(binding?.adViewContainer)
    }

    override fun onResume() {
        super.onResume()
        initBanner(binding?.adViewContainer)
    }

    override fun setListener() {
    }



    public var searchMainFragment: SearchFragment? = null

    override fun afterSetContentView() {
        super.afterSetContentView()
        setFullScreen()
    }

    override fun initFragment(): BaseFragment<*>? {
        if(keyword == null) {
            searchMainFragment = SearchFragment();
            return searchMainFragment
        }else{
            var searchResultFragment = SearchResultFragment();
            searchResultFragment.setKeyword(keyword)
            searchResultFragment.setShowKeyboard(false)
            return searchResultFragment
        }
    }

    private fun setFullScreen() {
        super.afterSetContentView()
        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.statusBarColor = Color.TRANSPARENT
    }


    public companion object {

        public fun startScreen(context: Context, keyword : String?) {
            InterAds.showAdsBreak(context as Activity){
                var intent = Intent(context, SearchActivity::class.java)
                intent.putExtra("data", keyword)
                context.startActivity(Intent(intent))
            }

        }
    }

    override fun onBackPressed() {
        InterAds.showAdsBreak(this){
            super.onBackPressed()
        }
    }
}