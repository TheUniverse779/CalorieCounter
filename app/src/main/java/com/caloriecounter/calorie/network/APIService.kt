package com.caloriecounter.calorie.network

import com.caloriecounter.calorie.ui.main.model.DataResponse
import com.caloriecounter.calorie.ui.main.model.Location
import com.caloriecounter.calorie.ui.main.model.category.CategoryResponse
import com.caloriecounter.calorie.ui.main.model.doubleimage.DoubleImageDataResponse
import com.caloriecounter.calorie.ui.main.model.image.Image
import com.caloriecounter.calorie.ui.search.model.PopularTagResponse
import com.caloriecounter.calorie.ui.search.model.SearchResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query


interface APIService {


    @GET("data")
    suspend fun getImagesByCategoryId(
        @Query("screen[width]") width: String?,
        @Query("screen[height]") height: String?,
        @Query("category_id") categoryId: String?,
        @Query("sort") sort: String?,
        @Query("types[]") types: Array<String>?,
        @Query("uploader_types[]") uploader_types: Array<String>?,
        @Query("limit") limit: String?,
        @Query("offset") offset: Int
    ): DataResponse


    @GET("categories")
    suspend fun getAllCategories(
        @Query("screen[width]") width: String,
        @Query("screen[height]") height: String,
        @Query("limit") limit: String
    ): CategoryResponse


    @GET("data")
    suspend fun search(
        @Query("screen[width]") width: String?,
        @Query("screen[height]") height: String?,
        @Query("category_id") categoryId: String?,
        @Query("sort") sort: String?,
        @Query("types[]") types: Array<String>?,
        @Query("limit") limit: String?,
        @Query("offset") offset: Int,
        @Query(encoded = true, value = "query") query: String?
    ): DataResponse

    @Headers(
        "Authorization: Bearer VN.M.4CA7uiFYxyuzBDo2NP_dN9k-Rvv7th1sueQBsrPI8bKXp0-j4EJ3MsEVe51FatLQJxzhycMVeW3wgpQa40tjS2BfyJkD-X6KsZLKEFatc5cQihddIz4bax4jrC15vJh6zpiW.vDuPhCSXskR0u_yDwRKrFQvl5-5Iz3k_wYglHyN1BZjM1iCGGn7GA63jnnduhjoHCGVAK8vQsM0qKhllfglCmDcJ_mchBAoyVUz78xIG9z00kNkAR09ri_pecyTGF9D33v6nHx8SV7-3hCkT6Eig-A",
        "mfp-client-id: mfp-mobile-android-google",
        "mfp-user-id: 173325443583277"
        )
    @GET("https://api.myfitnesspal.com/v2/search/nutrition?scope=all&max_items=1000")
    suspend fun searchDish(
        @Query(encoded = true, value = "q") query: String?
    ): SearchResponse


    @GET("double-images")
    suspend fun getDoubleImages(
        @Query("screen[width]") width: String?,
        @Query("screen[height]") height: String?,
        @Query("sort") sort: String?,
        @Query("types[]") types: Array<String>?,
        @Query("limit") limit: String?,
        @Query("offset") offset: Int
    ): DoubleImageDataResponse


    @GET("videos")
    suspend fun getLiveWallpaper(
        @Query("screen[width]") width: String?,
        @Query("screen[height]") height: String?,
        @Query("sort") sort: String?,
        @Query("content_type") contentType: String?,
        @Query("limit") limit: String?,
        @Query("offset") offset: Int
    ): DataResponse

    @GET("popular-tags")
    suspend fun getPopularTag(): PopularTagResponse

    @GET("get-ramdom-image")
    suspend fun getRandom(): Image


    @GET("json.gp")
    suspend fun getLocation(): Location

    @GET("data")
    suspend fun getNewTrending(@Query("is_trend") is_trend: String?,@Query("limit") limit: String?,
                               @Query("offset") offset: Int): DataResponse


    @FormUrlEncoded
    @POST("update-statistical")
    suspend fun postAction(@Field("type") type : String, @Field("image_id") image_id : String?, @Field("video_id") video_id : String?, @Field("country") country : String? )



}