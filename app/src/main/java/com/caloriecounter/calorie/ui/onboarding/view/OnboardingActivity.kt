package com.caloriecounter.calorie.ui.onboarding.view

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.View
import com.caloriecounter.calorie.Constant
import com.caloriecounter.calorie.R
import com.caloriecounter.calorie.base.BaseActivityNew
import com.caloriecounter.calorie.base.BaseFragment
import com.caloriecounter.calorie.databinding.ActivityOnboardingBinding
import com.caloriecounter.calorie.ui.main.model.image.Image
import com.caloriecounter.calorie.ui.main.view.MainActivity

class OnboardingActivity : BaseActivityNew<ActivityOnboardingBinding?>() {

    override fun getLayoutRes(): Int {
        return R.layout.activity_onboarding
    }

    override fun getFrame(): Int {
        return R.id.mainFrame
    }

    override fun getDataFromIntent() {

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)


    }

    override fun afterSetContentView() {
        super.afterSetContentView()
        setFullScreen()
    }

    override fun doAfterOnCreate() {

    }

    override fun setListener() {
    }

    private fun setFullScreen() {
        super.afterSetContentView()
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
        window.statusBarColor = Color.TRANSPARENT
    }

    override fun initFragment(): BaseFragment<*>? {
        return OnBoardingFragment()
    }

    public companion object {
        public fun startScreen(context: Context, image: Image?) {
            var intent = Intent(context, OnboardingActivity::class.java)
            context.startActivity(Intent(intent))
        }

    }


}