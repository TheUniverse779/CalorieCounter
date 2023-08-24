package com.caloriecounter.calorie.ui.main.view

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.caloriecounter.calorie.Constant
import com.caloriecounter.calorie.R
import com.caloriecounter.calorie.WeatherApplication
import com.caloriecounter.calorie.ads.Callback
import com.caloriecounter.calorie.ads.Prefs
import com.caloriecounter.calorie.ads.RewardedAds
import com.caloriecounter.calorie.base.BaseFragment
import com.caloriecounter.calorie.databinding.FragmentHomeBinding
import com.caloriecounter.calorie.iinterface.DialogState
import com.caloriecounter.calorie.ui.autochangewallpaper.view.AutoChangeActivity
import com.caloriecounter.calorie.ui.charging.view.AllAnimationFragment
import com.caloriecounter.calorie.ui.downloaded.view.DownloadedActivity
import com.caloriecounter.calorie.ui.main.adapter.CategoryMenuAdapter
import com.caloriecounter.calorie.ui.main.adapter.TabAdapter
import com.caloriecounter.calorie.ui.main.event.CoinChange
import com.caloriecounter.calorie.ui.main.event.OnClickPromotion
import com.caloriecounter.calorie.ui.main.model.Menu
import com.caloriecounter.calorie.ui.main.model.MenuData
import com.caloriecounter.calorie.ui.main.model.category.Category
import com.caloriecounter.calorie.ui.main.model.image.Image
import com.caloriecounter.calorie.ui.main.viewmodel.WeatherViewModel
import com.caloriecounter.calorie.ui.mywallpaper.view.MyWallpaperActivity
import com.caloriecounter.calorie.ui.premium.PremiumActivity
import com.caloriecounter.calorie.ui.search.view.SearchActivity
import com.caloriecounter.calorie.ui.slideimage.event.OnDialogDismiss
import com.caloriecounter.calorie.ui.slideimage.view.DailyGiftDialog2
import com.caloriecounter.calorie.ui.slideimage.view.WalliveDetailImageActivity
import com.caloriecounter.calorie.util.AppUtil
import com.caloriecounter.calorie.util.DailyForecastSchedule
import com.caloriecounter.calorie.util.PreferenceUtil
import com.caloriecounter.calorie.util.toGone
import com.caloriecounter.calorie.util.toInvisible
import com.caloriecounter.calorie.util.toVisible
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding?>() {
    private val weatherViewModel: WeatherViewModel by viewModels()

    @Inject
    lateinit var preferenceUtil: PreferenceUtil


    private var tabAdapter: TabAdapter? = null
    private var menuData: MenuData? = null
    private var menus: ArrayList<Menu>? = null

    private var listMenuCategory = ArrayList<Category>()
    private var categoryMenuAdapter: CategoryMenuAdapter? = null

    private var mCategory: Category? = null

    override fun getLayoutRes(): Int {
        return R.layout.fragment_home
    }

    override fun initView() {
        menuData =
            Gson().fromJson(AppUtil.loadJSONFromAsset(mActivity, "menu"), MenuData::class.java)
        menus = menuData?.menu

        tabAdapter = TabAdapter(childFragmentManager, mActivity)
        menus?.iterator()?.forEach { i ->
            if (i.type == "category") {
                var categoryFragment = CategoryFragment()
                tabAdapter?.addFragment(categoryFragment, "")
            } else if (i.type == "double") {
                var doubleImageFragment = DoubleFragment()
                doubleImageFragment.setData(i.contentType)
                tabAdapter?.addFragment(doubleImageFragment, "")
            } else if (i.type == "newest") {
                var newestHomeTabFragment = NewestHomeTabFragment()
                tabAdapter?.addFragment(newestHomeTabFragment, "")
            } else if (i.type == "special") {
                var homeTabFragment = SpecialTabFragment()
                homeTabFragment.setData(i.contentType)
                tabAdapter?.addFragment(homeTabFragment, "")
            } else if (i.type == "live") {
                var liveWallpaperFragment = WalliveLiveTabFragment()
                tabAdapter?.addFragment(liveWallpaperFragment, "")
            } else if (i.type == "ai") {
                var specialArtTabFragment = SpecialArtTabFragment()
                tabAdapter?.addFragment(specialArtTabFragment, "")
            } else if (i.type == "other") {
                var specialArtTabFragment = OtherFragment()
                tabAdapter?.addFragment(specialArtTabFragment, "")
            } else if (i.type == "charging") {
                var specialArtTabFragment = AllAnimationFragment()
                tabAdapter?.addFragment(specialArtTabFragment, "")
            } else if (i.type == "trending") {
                var specialArtTabFragment = NewTrendingFragment()
                tabAdapter?.addFragment(specialArtTabFragment, "")
            } else {
                var homeTabFragment = HomeTabFragment()
                homeTabFragment.setData(i.contentType)
                tabAdapter?.addFragment(homeTabFragment, "")
            }

        }
        binding?.vpContent?.adapter = tabAdapter
        binding?.tabLayout?.setupWithViewPager(binding?.vpContent)

        var index = 0;
        menus?.iterator()?.forEach { i ->
            val tabHome: View = LayoutInflater.from(mActivity).inflate(R.layout.item_tab, null)
            val tvHome = tabHome.findViewById<TextView>(R.id.tvTitle)
            val indicator = tabHome.findViewById<View>(R.id.indicator)
            tvHome.text = i.label



            if (i.isSelected) {
                tvHome.setTextColor(Color.parseColor("#FFFFFF"))
                indicator.toVisible()

                val typeface = ResourcesCompat.getFont(
                    mActivity, R.font.svn_avo_bold
                )
                tvHome.setTypeface(typeface)
            } else {
                tvHome.setTextColor(Color.parseColor("#80FFFFFF"))
                indicator.toInvisible()

                val typeface = ResourcesCompat.getFont(
                    mActivity, R.font.svn_avo
                )
                tvHome.setTypeface(typeface)
            }
            binding?.tabLayout?.getTabAt(index)?.customView = tabHome
            index += 1
        }

        binding?.tabLayout?.addOnTabSelectedListener(onTabSelectedListener);
        binding?.vpContent?.offscreenPageLimit = 10

    }

    private val onTabSelectedListener: TabLayout.OnTabSelectedListener = object :
        TabLayout.OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab) {
            val c = tab.position
            tabAdapter?.setOnSelectView(binding?.tabLayout, c)
            changeMenu(c)
            when (c) {
                0 -> {
                    binding?.tvTitle?.text = "Wallive"
                    WeatherApplication.trackingEvent("Click_Home_Screen")
                }
                1 -> {
                    binding?.tvTitle?.text = "Video"
                    WeatherApplication.trackingEvent("Click_Special_art_Screen")
                }
                2 -> {
                    binding?.tvTitle?.text = "Exclusive"
                    WeatherApplication.trackingEvent("Click_Exclusive_Screen")
                }
                3 -> {
                    binding?.tvTitle?.text = "Explore"
                    WeatherApplication.trackingEvent("Click_Other_Screen")
                }
                4 -> WeatherApplication.trackingEvent("Click_Charging_Screen")
                5 -> WeatherApplication.trackingEvent("Click_Live_Screen")
                6 -> WeatherApplication.trackingEvent("Click_Category_Screen")
            }
        }

        override fun onTabUnselected(tab: TabLayout.Tab) {
            val c = tab.position
            tabAdapter?.setUnSelectView(binding?.tabLayout, c)
        }

        override fun onTabReselected(tab: TabLayout.Tab) {}
    }

    override fun initData() {
        val isPro = Prefs(WeatherApplication.get()).premium

        if(isPro == 1){
            binding?.btnProMenu?.toGone()
            binding?.layoutProBottom?.toGone()
        }else{
            binding?.btnProMenu?.toVisible()
            binding?.layoutProBottom?.toVisible()
        }


        binding?.tvCoin?.text = preferenceUtil.getValue(Constant.SharePrefKey.COIN, 0).toString()

        DailyForecastSchedule.scheduleDailyForecast(mActivity)

        try {
            weatherViewModel.getCountry()
        } catch (e: Exception) {
        }

        RewardedAds.initRewardAds(mActivity, Callback {
        })


        if(isPro != 1) {
            Handler().postDelayed(Runnable {
                var currentDay = getDate(System.currentTimeMillis());
                var lastDay = preferenceUtil.getValue(Constant.SharePrefKey.DAY, "")
                if (currentDay != lastDay) {
                    weatherViewModel.getImagesByCatId(null, Constant.SortBy.RATING, arrayOf("private"), null, "200", 0)
                    preferenceUtil.setValue(Constant.SharePrefKey.DAY, getDate(System.currentTimeMillis()))
                } else {
                    if (preferenceUtil.getValue(Constant.SharePrefKey.IMAGE, "").isNotEmpty()) {
                        showBox(Gson().fromJson(preferenceUtil.getValue(Constant.SharePrefKey.IMAGE, ""), Image::class.java))
                    } else {
                        weatherViewModel.getImagesByCatId(null, Constant.SortBy.RATING, arrayOf("private"), null, "200", 0)
                    }
                }

            }, 2000)
        }

    }


    private var handler = Handler()
    private fun showBox(image: Image){
        binding?.motionLayout?.toVisible()
        handler.postDelayed(Runnable {
            binding?.motionLayout?.transitionToEnd()

            binding?.motionLayout?.addTransitionListener(object : MotionLayout.TransitionListener{
                override fun onTransitionStarted(
                    motionLayout: MotionLayout?,
                    startId: Int,
                    endId: Int
                ) {

                }

                override fun onTransitionChange(
                    motionLayout: MotionLayout?,
                    startId: Int,
                    endId: Int,
                    progress: Float
                ) {
                }

                override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                    Handler().postDelayed(Runnable {
                        binding?.motionLayout?.transitionToStart()
                        if (currentId != R.id.start) {
                            showDailyGift(image)
                        }
                    }, 2000)
                }

                override fun onTransitionTrigger(
                    motionLayout: MotionLayout?,
                    triggerId: Int,
                    positive: Boolean,
                    progress: Float
                ) {
                }

            })
        }, 7000)
    }



    private fun showDailyGift(image: Image){
        var dailyGiftDialog = DailyGiftDialog2.newInstance {
            image.cost = "0"
            var listImage = ArrayList<Image>()
            listImage.add(image)
            WalliveDetailImageActivity.startScreen(mActivity, listImage, false, null)
        }
        dailyGiftDialog.setImage(image)
        dailyGiftDialog.setDialogState( object : DialogState{
            override fun onDialogShow() {
                binding?.blurFull?.toVisible()
                binding?.blurFull2?.toVisible()
            }

            override fun onDialogDismiss() {
                binding?.blurFull?.toGone()
                binding?.blurFull2?.toGone()
            }

        })

        dailyGiftDialog.show(mActivity.supportFragmentManager, null)
    }

    private fun getDate(time: Long): String? {
        val cal: Calendar = Calendar.getInstance(Locale.ENGLISH)
        cal.timeInMillis = time
        return DateFormat.format("dd-MM-yyyy", cal).toString()
    }


    override fun setListener() {

        binding?.view?.setOnClickListener {
            var image = Gson().fromJson(preferenceUtil.getValue(Constant.SharePrefKey.IMAGE, ""), Image::class.java)
            showDailyGift(image)
        }

        binding?.blurFull?.setOnClickListener {
            binding?.blurFull?.toGone()
        }

        binding?.drawerLayout?.setScrimColor(
            mActivity.getResources().getColor(android.R.color.transparent)
        );
        binding?.btnMenu?.setOnClickListener {
            binding?.drawerLayout?.openDrawer(GravityCompat.START, true)
            WeatherApplication.trackingEvent("Click_Menu_Button")
        }

        binding?.btnSearch?.setOnClickListener {
            SearchActivity.startScreen(mActivity, null)
            WeatherApplication.trackingEvent("Click_Search_Button")
        }

        binding?.viewCoin?.setOnClickListener {
            if (RewardedAds.isCanShowAds()) {
                if (preferenceUtil.getValue(Constant.SharePrefKey.COUNT_REWARD, 0) >= 3) {
                    var askUserGoProDialog = AskUserGoProDialog();
                    askUserGoProDialog.setDialogState( object : DialogState{
                        override fun onDialogShow() {
                            binding?.blurFull?.toVisible()
                        }

                        override fun onDialogDismiss() {
                            binding?.blurFull?.toGone()
                        }

                    })
                    askUserGoProDialog.setOnClick {
                        RewardedAds.showAdsBreak(mActivity) {
                            preferenceUtil.setValue(Constant.SharePrefKey.COUNT_REWARD, 0)
                            preferenceUtil.setValue(
                                Constant.SharePrefKey.COIN,
                                preferenceUtil.getValue(Constant.SharePrefKey.COIN, 0) + 1
                            )
                            EventBus.getDefault().post(CoinChange())
                        }
                    }
                    askUserGoProDialog.show(mActivity.supportFragmentManager, null)
                } else {
                    var askUserViewAdsDialog2 = AskUserViewAdsDialog2();
                    askUserViewAdsDialog2.setDialogState( object : DialogState{
                        override fun onDialogShow() {
                            binding?.blurFull?.toVisible()
                        }

                        override fun onDialogDismiss() {
                            binding?.blurFull?.toGone()
                        }

                    })
                    askUserViewAdsDialog2.setOnClick {
                        RewardedAds.showAdsBreak(mActivity) {
                            preferenceUtil.setValue(
                                Constant.SharePrefKey.COUNT_REWARD,
                                preferenceUtil.getValue(
                                    Constant.SharePrefKey.COUNT_REWARD,
                                    0
                                ) + 1
                            )
                            preferenceUtil.setValue(
                                Constant.SharePrefKey.COIN,
                                preferenceUtil.getValue(Constant.SharePrefKey.COIN, 0) + 1
                            )
                            EventBus.getDefault().post(CoinChange())
                        }
                    }
                    askUserViewAdsDialog2.show(mActivity.supportFragmentManager, null)
                }
            } else {
                Toast.makeText(mActivity, "No ads now, please come back later", Toast.LENGTH_SHORT).show()
            }



        }

        binding?.btnProBottom?.setOnClickListener {
            WeatherApplication.trackingEvent("Click_Premium")
            startActivity(Intent(mActivity, PremiumActivity::class.java))
        }


        binding?.btnProMenu?.setOnClickListener {
            WeatherApplication.trackingEvent("Click_Premium")
            startActivity(Intent(mActivity, PremiumActivity::class.java))
            Handler().postDelayed({binding?.drawerLayout?.closeDrawer(GravityCompat.START, true) }, 500)
        }


        binding?.btnTerm1?.setOnClickListener {
            val url = "https://docs.google.com/document/d/1prnBnlhwxSPGcXZ7EKvqKOFaxHObRMUS3BtgcMw5wHM/edit?usp=sharing"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            mActivity.startActivity(i)
            Handler().postDelayed({binding?.drawerLayout?.closeDrawer(GravityCompat.START, true) }, 500)
        }

        binding?.btnTerm2?.setOnClickListener {
            val url = "https://docs.google.com/document/d/1prnBnlhwxSPGcXZ7EKvqKOFaxHObRMUS3BtgcMw5wHM/edit?usp=sharing"
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            mActivity.startActivity(i)
            Handler().postDelayed({binding?.drawerLayout?.closeDrawer(GravityCompat.START, true) }, 500)
        }

        binding?.btnSupport?.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse("mailto:phuongchau2783463@gmail.com") // only email apps should handle this
                intent.putExtra(Intent.EXTRA_SUBJECT, "App feedback")
                startActivity(intent)
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(
                    activity,
                    "There are no email client installed on your device.", Toast.LENGTH_SHORT
                ).show()
            }
        }


//        binding?.btnLiveWallpaperMenu?.setOnClickListener {
//            binding?.drawerLayout?.closeDrawer(GravityCompat.START, true)
//            binding?.vpContent?.currentItem = 5
//            WeatherApplication.trackingEvent("Click_btnLiveWallpaperMenu")
//
//        }
//
//        binding?.btnExclusive?.setOnClickListener {
//            binding?.drawerLayout?.closeDrawer(GravityCompat.START, true)
//            binding?.vpContent?.currentItem = 2
//            WeatherApplication.trackingEvent("Click_btnExclusiveMenu")
//
//        }
//
//        binding?.btnCategory?.setOnClickListener {
//            binding?.drawerLayout?.closeDrawer(GravityCompat.START, true)
//            binding?.vpContent?.currentItem = 6
//            WeatherApplication.trackingEvent("Click_btnCategoryMenu")
//
//        }
//
//        binding?.btnDouble?.setOnClickListener {
//            binding?.drawerLayout?.closeDrawer(GravityCompat.START, true)
//            binding?.vpContent?.currentItem = 6
//            WeatherApplication.trackingEvent("Click_btnDoubleMenu")
//
//        }
//
        binding?.btnHome?.setOnClickListener {
            binding?.drawerLayout?.closeDrawer(GravityCompat.START, true)
            binding?.vpContent?.currentItem = 0
            WeatherApplication.trackingEvent("Click_btnHomeMenu")

        }
//
//        binding?.btnSpecialArt?.setOnClickListener {
//            binding?.drawerLayout?.closeDrawer(GravityCompat.START, true)
//            binding?.vpContent?.currentItem = 1
//            WeatherApplication.trackingEvent("Click_btnSpecialArtMenu")
//
//        }

        binding?.btnFavorite?.setOnClickListener {
            binding?.drawerLayout?.closeDrawer(GravityCompat.START, true)
            Handler().postDelayed(Runnable {
                MyWallpaperActivity.startScreen(mActivity, Constant.MyWallpaperType.FAVORITE)
            }, 500)
            WeatherApplication.trackingEvent("Click_Favorite_Menu")
        }

//        binding?.btnRecent?.setOnClickListener {
//            binding?.drawerLayout?.closeDrawer(GravityCompat.START, true)
//            Handler().postDelayed(Runnable {
//                MyWallpaperActivity.startScreen(mActivity, Constant.MyWallpaperType.RECENT)
//            }, 500)
//            WeatherApplication.trackingEvent("Click_Recent_Menu")
//        }

        binding?.btnDownload?.setOnClickListener {
            binding?.drawerLayout?.closeDrawer(GravityCompat.START, true)
            Handler().postDelayed(Runnable {
                DownloadedActivity.startScreen(mActivity)
            }, 500)
            WeatherApplication.trackingEvent("Click_Download_Menu")
        }
//        binding?.btnAutoMenu?.setOnClickListener {
//            binding?.drawerLayout?.closeDrawer(GravityCompat.START, true)
//            Handler().postDelayed(Runnable {
//                AutoChangeActivity.startScreen(mActivity)
//            }, 500)
//            WeatherApplication.trackingEvent("Click_Auto_change_Menu")
//        }
//
//        binding?.btnDownloadLive?.setOnClickListener {
//            binding?.drawerLayout?.closeDrawer(GravityCompat.START, true)
//            Handler().postDelayed(Runnable {
//                InterAds.showAdsBreak(mActivity) {
//                    startActivity(Intent(mActivity, MainActivity::class.java))
//                }
//            }, 500)
//            WeatherApplication.trackingEvent("Click_Download_live_Menu")
//
//        }
//
//        binding?.btnRateApp?.setOnClickListener {
//            val ratingDialog: RatingDialog = RatingDialog.newInstance("", "") {
//                val goToMarket =
//                    Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + mActivity.packageName))
//                goToMarket.addFlags(
//                    Intent.FLAG_ACTIVITY_NO_HISTORY or
//                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
//                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK
//                )
//                try {
//                    startActivity(goToMarket)
//                } catch (anfe: ActivityNotFoundException) {
//                    startActivity(
//                        Intent(
//                            Intent.ACTION_VIEW,
//                            Uri.parse("https://play.google.com/store/apps/details?id=" + mActivity.packageName)
//                        )
//                    )
//                }
//            }
//            ratingDialog.show(mActivity.supportFragmentManager, "")
//            WeatherApplication.trackingEvent("Click_Rate_Home")
//            binding?.drawerLayout?.closeDrawer(GravityCompat.START, true)
//        }

//        binding?.btnRate?.setOnClickListener {
//            val ratingDialog: RatingDialog = RatingDialog.newInstance("", "") {
//                val goToMarket =
//                    Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + mActivity.packageName))
//                goToMarket.addFlags(
//                    Intent.FLAG_ACTIVITY_NO_HISTORY or
//                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
//                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK
//                )
//                try {
//                    startActivity(goToMarket)
//                } catch (anfe: ActivityNotFoundException) {
//                    startActivity(
//                        Intent(
//                            Intent.ACTION_VIEW,
//                            Uri.parse("https://play.google.com/store/apps/details?id=" + mActivity.packageName)
//                        )
//                    )
//                }
//            }
//            ratingDialog.show(mActivity.supportFragmentManager, "")
//            WeatherApplication.trackingEvent("Click_Rate_Home")
//            binding?.drawerLayout?.closeDrawer(GravityCompat.START, true)
//        }

        binding?.drawerLayout?.addDrawerListener(object : DrawerLayout.DrawerListener {
            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {

            }

            override fun onDrawerOpened(drawerView: View) {
                binding?.blurFull?.toVisible()
            }

            override fun onDrawerClosed(drawerView: View) {
                binding?.blurFull?.toGone()
            }

            override fun onDrawerStateChanged(newState: Int) {
            }

        })
    }

    private fun clearBackground() {
//        binding?.btnHome?.background = null
//        binding?.btnExclusive?.background = null
//        binding?.btnDouble?.background = null
//        binding?.btnCategory?.background = null
//        binding?.btnLiveWallpaperMenu?.background = null
//        binding?.btnSpecialArt?.background = null
//
//        binding?.tvHome?.setTextColor(mActivity.resources.getColor(R.color.white))
//        binding?.tvExclusive?.setTextColor(mActivity.resources.getColor(R.color.white))
//        binding?.tvDouble?.setTextColor(mActivity.resources.getColor(R.color.white))
//        binding?.tvCategory?.setTextColor(mActivity.resources.getColor(R.color.white))
//        binding?.tvLiveWallpaperMenu?.setTextColor(mActivity.resources.getColor(R.color.white))
//        binding?.tvSpecialArt?.setTextColor(mActivity.resources.getColor(R.color.white))

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun setObserver() {
        weatherViewModel.dataResponseLiveData.observe(this, Observer {
            var items = it.items.shuffled() as ArrayList
            preferenceUtil.setValue(Constant.SharePrefKey.IMAGE, Gson().toJson(items[0]))
            showBox(items[0])
        })

//        weatherViewModel.requestFail.observe(this) {
//            canLoadMore = true
//            binding?.progressBar?.toGone()
//            binding?.tvRefresh?.toGone()
//
//            try {
//                binding?.loadMoreProgressBar?.visibility = View.GONE
//            } catch (e: Exception) {
//            }
//            binding?.swipeToRefresh?.isRefreshing = false
//
//            try {
//                if(listImage.size == 0){
//                    binding?.layoutReload?.toVisible()
//                }else{
//                    binding?.layoutReload?.toGone()
//                }
//            } catch (e: Exception) {
//            }
//        }

//        getData()
    }

    override fun getFrame(): Int {
        return R.id.mainFrame
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
        try {
            handler.removeCallbacksAndMessages(null);
        } catch (e: Exception) {
        }
    }


    @Subscribe
    public fun onClickPromotion(onClickPromotion: OnClickPromotion) {
        when (onClickPromotion.key) {
            "auto" -> {
                AutoChangeActivity.startScreen(mActivity)
            }

            "live" -> binding?.vpContent?.currentItem = 5
            "category" -> binding?.vpContent?.currentItem = 6
        }
    }

    private fun changeMenu(position: Int) {
        clearBackground()
//        when(position){
//            0 -> {
//                binding?.btnHome?.background = AppCompatResources.getDrawable(mActivity,R.drawable.gradient_menu)
//                binding?.tvHome?.setTextColor(mActivity.resources.getColor(R.color.color_brand))
//            }
//
//            1 -> {
//                binding?.btnSpecialArt?.background = AppCompatResources.getDrawable(mActivity,R.drawable.gradient_menu)
//                binding?.tvSpecialArt?.setTextColor(mActivity.resources.getColor(R.color.color_brand))
//            }
//
//            2 -> {
//                binding?.btnExclusive?.background = AppCompatResources.getDrawable(mActivity,R.drawable.gradient_menu)
//                binding?.tvExclusive?.setTextColor(mActivity.resources.getColor(R.color.color_brand))
//            }
//
//            5 -> {
//                binding?.btnLiveWallpaperMenu?.background = AppCompatResources.getDrawable(mActivity,R.drawable.gradient_menu)
//                binding?.tvLiveWallpaperMenu?.setTextColor(mActivity.resources.getColor(R.color.color_brand))
//            }
//
//            6 -> {
//                binding?.btnCategory?.background = AppCompatResources.getDrawable(mActivity,R.drawable.gradient_menu)
//                binding?.tvCategory?.setTextColor(mActivity.resources.getColor(R.color.color_brand))
//            }
//
////            6 -> {
////                binding?.btnDouble?.background = AppCompatResources.getDrawable(mActivity,R.drawable.gradient_menu)
////                binding?.tvDouble?.setTextColor(mActivity.resources.getColor(R.color.color_brand))
////            }
//        }
    }


    @Subscribe
    public fun onCoinChange(onCoinChange: CoinChange) {
        binding?.tvCoin?.text = preferenceUtil.getValue(Constant.SharePrefKey.COIN, 0).toString()
    }

    @Subscribe
    public fun onDialogDismiss(onDialogDismiss: OnDialogDismiss){
        binding?.blurFull?.toGone()
    }

}