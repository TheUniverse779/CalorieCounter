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
                var liveWallpaperFragment = AllDishFragment()
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
                var homeTabFragment = DiscoveryFragment()
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
        }

        override fun onTabUnselected(tab: TabLayout.Tab) {
            val c = tab.position
            tabAdapter?.setUnSelectView(binding?.tabLayout, c)
        }

        override fun onTabReselected(tab: TabLayout.Tab) {}
    }

    override fun initData() {
        val isPro = Prefs(WeatherApplication.get()).premium


        DailyForecastSchedule.scheduleDailyForecast(mActivity)



        RewardedAds.initRewardAds(mActivity, Callback {
        })






    }


    override fun setListener() {


        binding?.btnSearch?.setOnClickListener {
            SearchActivity.startScreen(mActivity, "")
        }


        binding?.drawerLayout?.setScrimColor(
            mActivity.getResources().getColor(android.R.color.transparent)
        );
        binding?.btnMenu?.setOnClickListener {
            binding?.drawerLayout?.openDrawer(GravityCompat.START, true)
            WeatherApplication.trackingEvent("Click_Menu_Button")
        }




        binding?.btnProMenu?.setOnClickListener {
            WeatherApplication.trackingEvent("Click_Premium")
            startActivity(Intent(mActivity, PremiumActivity::class.java))
            Handler().postDelayed({binding?.drawerLayout?.closeDrawer(GravityCompat.START, true) }, 500)
        }

        binding?.btnHome?.setOnClickListener {
            binding?.drawerLayout?.closeDrawer(GravityCompat.START, true)
            binding?.vpContent?.currentItem = 0
            WeatherApplication.trackingEvent("Click_btnHomeMenu")

        }

        binding?.btnFavorite?.setOnClickListener {
            binding?.drawerLayout?.closeDrawer(GravityCompat.START, true)
            Handler().postDelayed(Runnable {
                MyWallpaperActivity.startScreen(mActivity, Constant.MyWallpaperType.FAVORITE)
            }, 500)
            WeatherApplication.trackingEvent("Click_Favorite_Menu")
        }


        binding?.btnDownload?.setOnClickListener {
            binding?.drawerLayout?.closeDrawer(GravityCompat.START, true)
            Handler().postDelayed(Runnable {
                DownloadedActivity.startScreen(mActivity)
            }, 500)
            WeatherApplication.trackingEvent("Click_Download_Menu")
        }


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
        })
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
    }



}