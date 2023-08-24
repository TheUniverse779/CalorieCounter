package com.caloriecounter.calorie.ui.autochangewallpaper.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.app.WallpaperManager
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Build
import android.os.CountDownTimer
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.caloriecounter.calorie.ui.autochangewallpaper.adapter.AutoChangeAdapter
import com.caloriecounter.calorie.Constant
import com.caloriecounter.calorie.R
import com.caloriecounter.calorie.base.BaseFragment
import com.caloriecounter.calorie.databinding.FragmentAutoChangeWallpaperBinding
import com.caloriecounter.calorie.iinterface.DataChange
import com.caloriecounter.calorie.model.Schedule
import com.caloriecounter.calorie.ui.autochangewallpaper.service.UIWidgetWallpaper
import com.caloriecounter.calorie.ui.newlivewallpaper.LWApplication
import com.caloriecounter.calorie.ui.newlivewallpaper.task.Image
import com.caloriecounter.calorie.ui.newlivewallpaper.task.LoadAllImageFromFolder
import com.caloriecounter.calorie.ui.slideimage.view.NotifiDialog
import com.caloriecounter.calorie.util.PreferenceUtil
import com.caloriecounter.calorie.util.Util
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.caloriecounter.calorie.WeatherApplication


class AutoChangeFragment : BaseFragment<FragmentAutoChangeWallpaperBinding?>() {
    private var imageAdapter: AutoChangeAdapter? = null;
    private var listImage = ArrayList<Image>()
    private var progressDialog: ProgressDialog? = null
    override fun getLayoutRes(): Int {
        return R.layout.fragment_auto_change_wallpaper
    }

    private fun showProgress(message: String) {
        progressDialog?.setMessage(message)
        progressDialog?.show()
    }

    override fun initView() {
        progressDialog = ProgressDialog(mActivity, ProgressDialog.THEME_HOLO_DARK)
    }

    override fun initData() {
        startLoop()
        imageAdapter = AutoChangeAdapter(mActivity, listImage, DataChange {
            if(it){
                binding?.layoutTip?.visibility = View.VISIBLE
            }else{
                binding?.layoutTip?.visibility = View.GONE
            }
        }, object : AutoChangeAdapter.ClickItem {
            override fun onClickItem(position: Int) {
//                var downloadedImageDetailFragment = DownloadedImageDetailFragment()
//                downloadedImageDetailFragment.setData(listImage, position)
//                addFragment(downloadedImageDetailFragment)
                if (isRunning) {
                    try {
                        var isShow = PreferenceUtil.getInstance(mActivity)
                            .getValue(Constant.PrefKey.IS_SHOW_DIALOG_AUTO_CHANGE, true)
                        if (isShow) {
                            var notiDialog = NotifiDialog.newInstance(
                                mActivity.getString(R.string.note_auto_change),
                                mActivity.getString(R.string.ok_got_it),
                                NotifiDialog.ClickButton {
                                    PreferenceUtil.getInstance(mActivity)
                                        .setValue(
                                            Constant.PrefKey.IS_SHOW_DIALOG_AUTO_CHANGE,
                                            false
                                        )
                                })
                            notiDialog.show(childFragmentManager, "")
                        }
                    } catch (e: Exception) {
                    }
                }

            }

        })
        binding?.rclContent?.run {
            adapter = imageAdapter
            setHasFixedSize(true)
            setItemViewCacheSize(20)
            isDrawingCacheEnabled = true
            drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
        }

        if (ContextCompat.checkSelfPermission(
                mActivity,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                mActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 789)
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 789)
        } else {
            getImage()
        }

        val schedules = Gson().fromJson<java.util.ArrayList<Schedule>>(
            Util.loadJSONFromAsset(mActivity, "schedule"),
            object : TypeToken<List<Schedule?>?>() {}.type
        )

        var schedule = PreferenceUtil.getInstance(mActivity).getValue(Constant.PrefKey.SCHEDULE, 0)


//        val adapter: ArrayAdapter<Schedule> = ArrayAdapter<Schedule>(
//            mActivity,
//            android.R.layout.simple_spinner_item,
//            schedules
//        )
//
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//
//        binding?.spinner?.adapter = adapter
//
//        binding?.spinner?.onItemSelectedListener = object : OnItemSelectedListener {
//            override fun onItemSelected(
//                parent: AdapterView<*>?,
//                view: View,
//                position: Int,
//                id: Long
//            ) {
//                binding?.tvLabel?.text = schedules[position].label
//                PreferenceUtil.getInstance(mActivity).setValue(Constant.PrefKey.SCHEDULE, position)
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {}
//        }
//
//        binding?.spinner?.setSelection(schedule)

        binding?.simpleSeekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding?.tvLabel?.text = "Frequency of changing wallpaper: "+schedules[progress].label
                PreferenceUtil.getInstance(mActivity).setValue(Constant.PrefKey.SCHEDULE, progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

        binding?.simpleSeekBar?.progress = schedule

    }

    override fun setListener() {
        binding?.btnBack?.setOnClickListener {
            mActivity.onBackPressed()
        }

        binding?.btnStart?.setOnClickListener {
            try {
                WeatherApplication.trackingEvent("Click_btn_Auto_change")
            } catch (e: Exception) {
            }

            if (ContextCompat.checkSelfPermission(
                    mActivity,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    mActivity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 789)
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 789)
            } else {
                if (binding?.imgPlay?.visibility == View.VISIBLE) {
                    if (listImage != null && listImage.size > 1) {
                        var listImage1 = ArrayList<Image>();
                        for (i in 0 until listImage.size) {
                            if (listImage[i].isSelectedAuto) {
                                listImage1.add(listImage[i])
                            }
                        }
                        if (listImage1 != null && listImage1.size > 1) {
                            LWApplication.setListImageDownloaded(listImage1)
                            val intent = Intent(WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER)
                            intent.putExtra(
                                WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                                ComponentName(
                                    mActivity,
                                    UIWidgetWallpaper::class.java
                                )
                            )
                            startActivity(intent)
                        } else {
                            Toast.makeText(
                                mActivity,
                                mActivity.getString(R.string.choose_2_image),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    } else {
                        Toast.makeText(
                            mActivity,
                            mActivity.getString(R.string.download_2_image),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                } else {
                    setWallpaper()
                }
            }




        }

        binding?.btnTip?.setOnClickListener {
            try {
                var notiDialog = NotifiDialog.newInstance(
                    mActivity.getString(R.string.note_auto_change),
                    mActivity.getString(R.string.ok_got_it),
                    NotifiDialog.ClickButton {
                        PreferenceUtil.getInstance(mActivity)
                            .setValue(
                                Constant.PrefKey.IS_SHOW_DIALOG_AUTO_CHANGE,
                                false
                            )
                    })
                notiDialog.show(childFragmentManager, "")
            } catch (e: Exception) {
            }
        }
    }


    @SuppressLint("StaticFieldLeak")
    private fun setWallpaper() {
        showProgress(mActivity.getString(R.string.stop_auto_change))
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.wallpaper_default)
        val manager =
            WallpaperManager.getInstance(mActivity)
        object : AsyncTask<Void?, Void?, Void?>() {


            override fun onPostExecute(aVoid: Void?) {
                super.onPostExecute(aVoid)
                try {
                    progressDialog?.dismiss()
                    Toast.makeText(mActivity, mActivity.getString(R.string.stopped), Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                }
            }

            override fun doInBackground(vararg p0: Void?): Void? {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        manager!!.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM)
                    } else {
                        manager!!.setBitmap(bitmap)
                    }

                } catch (e: Exception) {
                    Log.e("", "")
                }
                return null
            }

        }.execute()
    }

    override fun setObserver() {}

    public fun getImage() {
        val sdCard = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        val savedPath = sdCard.absolutePath + "/EZT4KWallpaper/"
        val loadAllImageFromFolder = LoadAllImageFromFolder(mActivity.contentResolver)
        loadAllImageFromFolder.setFolderPath(savedPath)
        loadAllImageFromFolder.onLoadDoneListener = object :
            LoadAllImageFromFolder.OnLoadDoneListener {
            override fun onLoadDone(images: List<Image>) {
                listImage.clear()
                listImage.addAll(images)

                mActivity.runOnUiThread {
                    imageAdapter!!.notifyDataSetChanged()
                }

            }

            override fun onLoadError() {}
        }
        loadAllImageFromFolder.execute()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 789) {
            if (grantResults.isNotEmpty()) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getImage()
                } else {
                }
            } else {
            }
        }
    }

    private var countDownTimer: CountDownTimer? = null
    private var isRunning = false
    private fun startLoop() {
        if (countDownTimer != null) {
            countDownTimer?.cancel()
        }
        countDownTimer = object : CountDownTimer(1000, 1000) {
            override fun onTick(l: Long) {

            }

            override fun onFinish() {
                try {
                    if (Util.isMyServiceRunning(UIWidgetWallpaper::class.java, mActivity)) {
                        binding?.imgPlay?.visibility = View.GONE
                        binding?.imgStop?.visibility = View.VISIBLE
                        binding?.tvState?.text = "Stop wallpaper"
                        binding?.tvStateTitle?.text = "Auto wallpaper changer is running..."
                        binding?.tvStateTitle?.visibility = View.VISIBLE
                        isRunning = true
                    } else {
                        binding?.imgPlay?.visibility = View.VISIBLE
                        binding?.imgStop?.visibility = View.GONE
                        binding?.tvState?.text = "Start wallpaper"
                        binding?.tvStateTitle?.text = ""
                        binding?.tvStateTitle?.visibility = View.GONE
                        isRunning = false
                    }
                } catch (e: Exception) {
                }
                startLoop()
            }
        }
        countDownTimer?.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }

    override fun getFrame(): Int {
        return R.id.mainFrame
    }
}