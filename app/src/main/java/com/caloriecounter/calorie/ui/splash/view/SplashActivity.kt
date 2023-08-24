package com.caloriecounter.calorie.ui.splash.view

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.RawResourceDataSource
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.caloriecounter.calorie.Constant
import com.caloriecounter.calorie.R
import com.caloriecounter.calorie.ads.Callback
import com.caloriecounter.calorie.ads.InterAds
import com.caloriecounter.calorie.ads.OpenAds
import com.caloriecounter.calorie.base.BaseActivityNew
import com.caloriecounter.calorie.base.BaseFragment
import com.caloriecounter.calorie.databinding.ActivitySplashBinding
import com.caloriecounter.calorie.ui.main.model.image.Image
import com.caloriecounter.calorie.ui.main.view.MainActivity
import com.caloriecounter.calorie.ui.onboarding.view.OnboardingActivity
import com.caloriecounter.calorie.ui.splash.SplashViewModel
import com.caloriecounter.calorie.util.PreferenceUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : BaseActivityNew<ActivitySplashBinding>() {

    private val weatherViewModel: SplashViewModel by viewModels()


    private var image : Image? = null



    private var isRunning = false
    private var isFetchFireBaseDone = false
    val new_ui = "new_ui"
    private var isLoadAdsDone = false
    private val isActivityStarted = false
    private val passDraw: String? = null
    @Inject
    lateinit var preferenceUtil: PreferenceUtil

    override fun getLayoutRes(): Int {
        return R.layout.activity_splash
    }

    override fun getFrame(): Int {
        return R.id.mainFrame
    }

    override fun getDataFromIntent() {
        image = intent.getSerializableExtra(Constant.IntentKey.DATA) as Image?
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let {
            image = intent.getSerializableExtra(Constant.IntentKey.DATA) as Image?
        }

    }

    override fun afterSetContentView() {
        super.afterSetContentView()
        setFullScreen()


    }

    private fun initializePlayer() {
        simpleExoplayer = SimpleExoPlayer.Builder(this).build()
        preparePlayer("default")
        binding?.playerView?.player = simpleExoplayer
        simpleExoplayer?.playWhenReady = true
        simpleExoplayer?.repeatMode = Player.REPEAT_MODE_ONE
        simpleExoplayer?.addListener(object : Player.EventListener {

        })
        binding?.playerView?.useController = false
        binding?.playerView?.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
    }

    private fun buildMediaSource(uri: Uri, type: String): MediaSource {
        return ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(uri)
    }

    private fun preparePlayer(type: String) {
        val uri  = RawResourceDataSource.buildRawResourceUri(R.raw.splash);
        val mediaSource = buildMediaSource(uri, type)
        simpleExoplayer?.prepare(mediaSource)
    }


    private fun releasePlayer() {
        simpleExoplayer?.release()
    }

    override fun doAfterOnCreate() {
//        Handler().postDelayed(Runnable {
////            WeatherApplication.get().showAdIfAvailable(this@SplashActivity, WeatherApplication.OnShowAdCompleteListener {
//                MainActivity.startScreen(this@SplashActivity)
//                finish()
////            })
//        }, 1000)

        pingAll()
        initAds()
        dataSourceFactory = DefaultDataSourceFactory(this, "exoplayer-sample")
        initializePlayer()
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
        return null
    }

    private fun initAds() {
        if (isNetworkAvailable(this)) {
            initRemoteConfig()
            InterAds.initInterAds(this, null)
            isRunning = true
            val handler = Handler()
            val runnable = Runnable {
                isLoadAdsDone = true
                isFetchFireBaseDone = true
                yasuo()
            }
            handler.postDelayed(runnable, 5000)
            OpenAds.initOpenAds(this@SplashActivity) {
                isLoadAdsDone = true
                handler.removeCallbacks(runnable)
                if (!isDestroyed) {
                    yasuo()
                }
            }
        } else {
            Handler(Looper.getMainLooper()).postDelayed({ this@SplashActivity.intent("1") }, 400)
        }
    }

    private fun initRemoteConfig() {
        val mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(3600)
            .build()
        mFirebaseRemoteConfig.fetchAndActivate()
            .addOnCompleteListener(this) {
                isFetchFireBaseDone = true
                yasuo()
//                fetchConfig()
            }
    }

    private fun yasuo() {
        if (isFetchFireBaseDone && isLoadAdsDone && !isActivityStarted) {
            if (isRunning) {
                riven()
            } else {
                Handler(Looper.getMainLooper()).postDelayed({ this@SplashActivity.intent("2") }, 400)
            }
        }
    }

    private fun riven() {
        if (OpenAds.isCanShowOpenAds()) {
            OpenAds.showOpenAds(this, Callback {
                Handler(Looper.getMainLooper()).postDelayed(
                    {
                        intent("3")
                    },
                    400
                )
            })
        } else {
            intent("4")
        }
    }

    private var isIntented : Boolean = false
    private fun intent(debug : String){
        Log.e("trackingggg", debug)


        if(!isIntented) {
            if(!preferenceUtil.getValue("firstOpen", false)){
                preferenceUtil.setValue("firstOpen", true)
                OnboardingActivity.startScreen(this@SplashActivity, image)
                finish()
            }else {
                MainActivity.startScreen(this@SplashActivity, null)
                finish()
            }
            isIntented = true
        }
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val manager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        var networkInfo: NetworkInfo? = null
        if (manager != null) {
            networkInfo = manager.activeNetworkInfo
        }
        var isAvailable = false
        if (networkInfo != null && networkInfo.isConnected) {
            // Network is present and connected
            isAvailable = true
        }
        return isAvailable
    }

    override fun onResume() {
        super.onResume()
        isRunning = true
    }

    override fun onPause() {
        super.onPause()
        isRunning = false
    }

    private fun pingAll(){

        weatherViewModel.getImagesByCatId(null, Constant.SortBy.RATING, null, null,"60", 0) // home


        val type: Array<String> = arrayOf("private")
        weatherViewModel.getImagesByCatId(null, Constant.SortBy.RATING, type, null,"60", 0) // special tab


        val uploader: Array<String> = arrayOf("wlc_ai_art")
        weatherViewModel.getImagesByCatId(null, Constant.SortBy.RATING, type, uploader,"60", 0)


        weatherViewModel.getImagesByCatId("192", Constant.SortBy.DOWNLOAD, type, null,"60", 0)

        weatherViewModel.getLiveWallpaper( Constant.SortBy.DOWNLOAD, "android_video", "30", 0)

        weatherViewModel.getAllCat()
    }

    private var simpleExoplayer: SimpleExoPlayer? = null
    private lateinit var dataSourceFactory: DataSource.Factory


    override fun onDestroy() {
        super.onDestroy()
        releasePlayer()
    }
}