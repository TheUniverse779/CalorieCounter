package com.caloriecounter.calorie.ui.main.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caloriecounter.calorie.WeatherApplication
import com.caloriecounter.calorie.base.BaseActivityNew
import com.caloriecounter.calorie.di.NetworkModule.NormalAPIService
import com.caloriecounter.calorie.di.NetworkModule.ProAPIService
import com.caloriecounter.calorie.di.NetworkModule.AccuAPIService
import com.caloriecounter.calorie.network.APIService
import com.caloriecounter.calorie.ui.main.model.DataResponse
import com.caloriecounter.calorie.ui.main.model.category.CategoryResponse
import com.caloriecounter.calorie.ui.main.model.doubleimage.DoubleImageDataResponse
import com.caloriecounter.calorie.ui.main.model.image.Image
import com.caloriecounter.calorie.ui.search.model.PopularTagResponse
import com.caloriecounter.calorie.util.PreferenceUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(@NormalAPIService private val apiService: APIService, @ProAPIService private val apiServiceWall: APIService, @AccuAPIService private val apiServiceLocation: APIService, private val preferenceUtil: PreferenceUtil) : ViewModel() {
    var categoryResponseLiveData = MutableLiveData<CategoryResponse>()

    var dataResponseLiveData = MutableLiveData<DataResponse>()
    var similarLiveData = MutableLiveData<DataResponse>()

    val doubleImageLiveData = MutableLiveData<DoubleImageDataResponse>()


    val liveRepos = MutableLiveData<DataResponse>()


    val popularTagResponse = MutableLiveData<PopularTagResponse>()
    val randomLiveData = MutableLiveData<Image>()


    val requestFail = MutableLiveData<String>()


    fun getAllCat() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                var currentTime = System.currentTimeMillis()
                val result = apiService.getAllCategories(
                    "1080",
                    "2280",
                    "100"
                )
                withContext(Dispatchers.Main) {
                    if (result != null) {
                        categoryResponseLiveData.value = result
                        try {
                            var time = System.currentTimeMillis();
                            var range = time - currentTime;
                            WeatherApplication.trackingEvent("Time_getAllCat", "Time", range.toString())
                        } catch (e: Exception) {
                        }
                    }
                }
            } catch (e: Exception) {
                try {
                    withContext(Dispatchers.Main) {
                        requestFail.value = "getAllCat"
                    }
                } catch (e: Exception) {
                    Log.e("", "")
                }
                Log.e("getAllCat: ", e.message!!)
                try {
                    WeatherApplication.trackingEvent("Fail_getAllCat", "Cause", e.message + isNetworkAvailable(WeatherApplication.get()))
                } catch (e: Exception) {
                }
            }
        }

    }


    fun getImagesByCatIdWithPosition(categoryId: String, sort : String, type: Array<String>?,uploader: Array<String>?, limit : String, offset : Int, position : Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                var currentTime = System.currentTimeMillis()
                val result = apiService.getImagesByCategoryId(
                    "1080",
                    "2280",
                    categoryId,
                    sort,
                    type,
                    uploader,
                    limit,
                    offset
                )
                withContext(Dispatchers.Main) {
                    if (result != null) {
                        result.position = position
                        dataResponseLiveData.value = result

                        try {
                            var time = System.currentTimeMillis();
                            var range = time - currentTime;
                            WeatherApplication.trackingEvent("Time_getImagesByCatIdWithPosition", "Time", range.toString())
                        } catch (e: Exception) {
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("getImagesByCatIdWithPos", e.message!!)
                try {
                    WeatherApplication.trackingEvent("Fail_getImagesByCatIdWithPosition", "Cause", e.message + isNetworkAvailable(WeatherApplication.get()))
                } catch (e: Exception) {
                }
            }
        }

    }

    fun getNewTrending(limit : String, offset : Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                var currentTime = System.currentTimeMillis()
                val result = apiService.getNewTrending(
                    "1",
                    limit,
                    offset
                )
                withContext(Dispatchers.Main) {
                    if (result != null) {
                        dataResponseLiveData.value = result

                        try {
                            var time = System.currentTimeMillis();
                            var range = time - currentTime;
                            WeatherApplication.trackingEvent("Time_getNewTrending", "Time", range.toString())
                        } catch (e: Exception) {
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("getNewTrending", e.message!!)
                try {
                    WeatherApplication.trackingEvent("Fail_getNewTrending", "Cause", e.message + isNetworkAvailable(WeatherApplication.get()))
                } catch (e: Exception) {
                }
            }
        }

    }

    fun getImagesByCatId(categoryId: String?, sort : String?, type: Array<String>?,uploader: Array<String>?,  limit : String, offset : Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                var currentTime = System.currentTimeMillis()
                val result = apiService.getImagesByCategoryId(
                    "1080",
                    "2280",
                    categoryId,
                    sort,
                    type,
                    uploader,
                    limit,
                    offset
                )
                withContext(Dispatchers.Main) {
                    if (result != null) {
                        result.offset = offset
                        dataResponseLiveData.value = result

                        try {
                            var time = System.currentTimeMillis();
                            var range = time - currentTime;
                            WeatherApplication.trackingEvent("Time_getImagesByCatId", "Time", range.toString())
                        } catch (e: Exception) {
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("getImagesByCatIdWithPos", e.message!!)
                try {
                    withContext(Dispatchers.Main) {
                        requestFail.value = "getImagesByCatId"
                    }
                    WeatherApplication.trackingEvent("Fail_getImagesByCatId", "Cause", e.message + isNetworkAvailable(WeatherApplication.get()))
                } catch (e: Exception) {
                    Log.e("", "")
                }
            }
        }

    }


    fun search(category: String?, sort: String?, type: Array<String>?, limit: String?, offset: Int, query: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                var currentTime = System.currentTimeMillis()
                val result = apiService.search(
                    "1080",
                    "2280",
                    category,sort, type, limit, offset, query
                )
                withContext(Dispatchers.Main) {
                    if (result != null) {
                        result.offset = offset
                        dataResponseLiveData.value = result


                        try {
                            var time = System.currentTimeMillis();
                            var range = time - currentTime;
                            WeatherApplication.trackingEvent("Time_search", "Time", range.toString())
                        } catch (e: Exception) {
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("search", e.message!!)
                try {
                    withContext(Dispatchers.Main) {
                        requestFail.value = "search"
                    }

                    WeatherApplication.trackingEvent("Fail_search", "Cause", e.message+ isNetworkAvailable(WeatherApplication.get()))
                } catch (e: Exception) {
                    Log.e("", "")
                }
            }
        }

    }


    fun getSimilar(category: String?, sort: String?, type: Array<String>?, limit: String?, offset: Int, query: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                var currentTime = System.currentTimeMillis()
                val result = apiService.search(
                    "1080",
                    "2280",
                    category,sort, type, limit, offset, query
                )
                withContext(Dispatchers.Main) {
                    if (result != null) {
                        result.offset = offset
                        similarLiveData.value = result


                        try {
                            var time = System.currentTimeMillis();
                            var range = time - currentTime;
                            WeatherApplication.trackingEvent("Time_getSimilar", "Time", range.toString())
                        } catch (e: Exception) {
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("getSimilar", e.message!!)
                try {
                    withContext(Dispatchers.Main) {
                        requestFail.value = "getSimilar"
                    }

                    WeatherApplication.trackingEvent("Fail_getSimilar", "Cause", e.message)
                } catch (e: Exception) {
                    Log.e("", "")
                }
            }
        }

    }


    fun getDoubleImages(sort: String?,
                        type: Array<String>?,
                        limit: String?,
                        offset: Int){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = apiServiceWall.getDoubleImages("1080", "2280",
                    sort, type, limit, offset
                )
                withContext(Dispatchers.Main) {
                    if (result != null) {
                        result.offset = offset
                        doubleImageLiveData.value = result
                    }
                }
            } catch (e: Exception) {
                Log.e("search", e.message!!)
                try {
                    withContext(Dispatchers.Main) {
                        requestFail.value = "getDoubleImages"
                    }
                } catch (e: Exception) {
                    Log.e("", "")
                }
            }
        }

    }


    fun getCountry(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = apiServiceLocation.getLocation()
                withContext(Dispatchers.Main) {
                    if (result != null) {
                        WeatherApplication.location = result.geoplugin_countryName
                    }
                }
            } catch (e: Exception) {
            }
        }

    }


    fun postAction(type : String, image_id : String?, video_id : String?){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                var result = apiService.postAction(type, image_id, video_id, WeatherApplication.location)
                Log.e("country", WeatherApplication.location)
            } catch (e: Exception) {
                Log.e("", "")
            }
        }

    }




    fun getLiveWallpaper(
                         sort: String?,
                         contentType: String?,
                         limit: String?,
                         offset: Int){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = apiService.getLiveWallpaper("1080",
                    "2280",
                    sort,
                    contentType,
                    limit,
                    offset
                )
                withContext(Dispatchers.Main) {
                    if (result != null) {
                        result.offset = offset
                        liveRepos.value = result
                    }
                }
            } catch (e: Exception) {
                Log.e("getLiveWallpaper", e.message!!+ isNetworkAvailable(WeatherApplication.get()))

                try {
                    WeatherApplication.trackingEvent("Fail_getLiveWallpaper", "Cause", e.message+ isNetworkAvailable(WeatherApplication.get()))
                } catch (e: Exception) {
                }
                try {
                    withContext(Dispatchers.Main) {
                        requestFail.value = "getLiveWallpaper"
                    }
                } catch (e: Exception) {
                    Log.e("", "")
                }
            }
        }

    }


    fun getPopularTag(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                var currentTime = System.currentTimeMillis()
                val result = apiService.getPopularTag(
                )
                withContext(Dispatchers.Main) {
                    if (result != null) {
                        popularTagResponse.value = result

                        try {
                            var time = System.currentTimeMillis();
                            var range = time - currentTime;
                            WeatherApplication.trackingEvent("Time_getPopularTag", "Time", range.toString())
                        } catch (e: Exception) {
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("getPopularTag", e.message!!)
                try {
                    WeatherApplication.trackingEvent("Fail_getPopularTag", "Cause", e.message+ isNetworkAvailable(WeatherApplication.get()))
                } catch (e: Exception) {
                }
            }
        }

    }

    fun getRandom(){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                var currentTime = System.currentTimeMillis()
                val result = apiService.getRandom()
                withContext(Dispatchers.Main) {
                    if (result != null) {
                        randomLiveData.value = result

                        try {
                            var time = System.currentTimeMillis();
                            var range = time - currentTime;
                            WeatherApplication.trackingEvent("Time_getRandom", "Time", range.toString())
                        } catch (e: Exception) {
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("getRandom", e.message!!)
                try {
                    WeatherApplication.trackingEvent("Fail_getRandom", "Cause", e.message + isNetworkAvailable(WeatherApplication.get()))
                } catch (e: Exception) {
                }
            }
        }

    }




    fun isNetworkAvailable(context: Context): Boolean {
        try {
            val manager = context.getSystemService(BaseActivityNew.CONNECTIVITY_SERVICE) as ConnectivityManager
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
        } catch (e: Exception) {
            return false;
        }
    }



}