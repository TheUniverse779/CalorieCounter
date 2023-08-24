package com.caloriecounter.calorie.ui.charging.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import com.caloriecounter.calorie.R
import com.caloriecounter.calorie.ads.InterAds
import com.caloriecounter.calorie.base.BaseActivityNew
import com.caloriecounter.calorie.base.BaseFragment
import com.caloriecounter.calorie.databinding.ActivityChargingAnimationDetailBinding

class ChargingDetailActivity : BaseActivityNew<ActivityChargingAnimationDetailBinding?>() {
    private var json: String? = null
    override fun getLayoutRes(): Int {
        return R.layout.activity_charging_animation_detail
    }

    override fun getFrame(): Int {
        return R.id.mainFrame
    }

    override fun getDataFromIntent() {
        json = intent.getStringExtra("json")
    }

    override fun doAfterOnCreate() {
        initBanner(binding?.adViewContainer)
    }
    override fun setListener() {}
    override fun initFragment(): BaseFragment<*> {
        return ChargingAnimationPreviewFragment.newInstance(json)
    }

    public companion object {
        public fun startScreen(context: Context, json: String?) {
            InterAds.showAdsBreak(context as Activity) {
                try {
                    var intent = Intent(context, ChargingDetailActivity::class.java)
                    intent.putExtra("json", json)
                    context.startActivity(intent)
                } catch (e: Exception) {
                    Log.e("", "")
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        initBanner(binding?.adViewContainer)
    }
}