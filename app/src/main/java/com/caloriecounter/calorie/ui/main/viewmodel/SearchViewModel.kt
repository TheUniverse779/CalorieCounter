package com.caloriecounter.calorie.ui.main.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caloriecounter.calorie.di.NetworkModule.NormalAPIService
import com.caloriecounter.calorie.network.APIService
import com.caloriecounter.calorie.ui.main.model.DataResponse
import com.caloriecounter.calorie.ui.main.model.category.CategoryResponse
import com.caloriecounter.calorie.ui.main.model.doubleimage.DoubleImageDataResponse
import com.caloriecounter.calorie.util.PreferenceUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(@NormalAPIService private val apiService: APIService, private val preferenceUtil: PreferenceUtil) : ViewModel() {
    var categoryResponseLiveData = MutableLiveData<CategoryResponse>()

    var dataResponseLiveData = MutableLiveData<DataResponse>()

    val doubleImageLiveData = MutableLiveData<DoubleImageDataResponse>()


    fun getAllCat() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = apiService.getAllCategories(
                    "1080",
                    "2280",
                    "100"
                )
                withContext(Dispatchers.Main) {
                    if (result != null) {
                        categoryResponseLiveData.value = result
                    }
                }
            } catch (e: Exception) {
                Log.e("getAllCat: ", e.message!!)
            }
        }

    }


    fun getImagesByCatIdWithPosition(categoryId: String, sort : String, type: Array<String>?,uploader: Array<String>?, limit : String, offset : Int, position : Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
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
                    }
                }
            } catch (e: Exception) {
                Log.e("getImagesByCatIdWithPos", e.message!!)
            }
        }

    }

    fun getImagesByCatId(categoryId: String?, sort : String, type: Array<String>?,uploader: Array<String>?, limit : String, offset : Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
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
                    }
                }
            } catch (e: Exception) {
                Log.e("getImagesByCatIdWithPos", e.message!!)
            }
        }

    }


    fun search(category: String?, sort: String?, type: Array<String>?, limit: String?, offset: Int, query: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = apiService.search(
                    "1080",
                    "2280",
                    category,sort, type, limit, offset, query
                )
                withContext(Dispatchers.Main) {
                    if (result != null) {
                        result.offset = offset
                        dataResponseLiveData.value = result
                    }
                }
            } catch (e: Exception) {
                Log.e("search", e.message!!)
            }
        }

    }


    fun getDoubleImages(sort: String?,
                        type: Array<String>?,
                        limit: String?,
                        offset: Int){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = apiService.getDoubleImages("1080", "2280",
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
            }
        }

    }



}