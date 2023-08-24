package com.caloriecounter.calorie.database;



import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;


import static androidx.room.OnConflictStrategy.REPLACE;

import com.caloriecounter.calorie.model.Favorite;
import com.caloriecounter.calorie.model.Recent;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface WeatherDao {


    @Query("SELECT * FROM Favorite")
    public Single<List<Favorite>> getAllFavorite();

    @Query("SELECT * FROM Favorite Where id = :idQuery")
    public Favorite getFavoriteFromId(String idQuery);

    @Query("DELETE FROM Favorite")
    public void clearFavorite();

    @Insert(onConflict = REPLACE)
    public void insertFavorite(Favorite favorite);

    @Delete
    public void deleteFavorite(Favorite favorite);

    /////////////


    @Query("SELECT * FROM Recent")
    public Single<List<Recent>> getAllRecent();

    @Query("SELECT * FROM Recent Where id = :idQuery")
    public Favorite getRecentFromId(String idQuery);

    @Query("DELETE FROM Recent")
    public void clearRecent();

    @Insert(onConflict = REPLACE)
    public void insertRecent(Recent recent);

    @Delete
    public void deleteRecent(Recent recent);


//    @Insert(onConflict = REPLACE)
//    public void insertLocation(SearchCityResponse location);
//
//    @Query("SELECT * FROM SearchCityResponse ORDER BY updateAt DESC")
//    public List<SearchCityResponse> findAllLocation();
//
//    @Query("DELETE FROM SearchCityResponse")
//    public void deleteAllLocation();
//
//    @Query("SELECT * FROM SearchCityResponse Where name = :idQuery")
//    public List<SearchCityResponse> getLocationByName(String idQuery);
//
//
//    @Insert(onConflict = REPLACE)
//    public void insertAllLocation(List<SearchCityResponse> locationByLatLongAccus);
//
////    @Query("SELECT * FROM SearchCityResponse Where LocalizedName = :idQuery")
////    public SearchCityResponse getLocationByLatLongAccuId(String idQuery);
//
//    @Delete
//    public void deleteLocation(SearchCityResponse locationByLatLongAccu);

////
//    @Insert(onConflict = REPLACE)
//    public void insertAllFood(List<Post> postForSearches);
//
//    @Insert(onConflict = REPLACE)
//    public void insertFood(Post food);
//
//    ////
//    @Query("DELETE FROM Post")
//    public void deleteAllFood();
//////
//////
//////    @Insert(onConflict = REPLACE)
//////    public void insertAllFoodForNewSearch(List<FoodForNewSearch> foodForSearches);
//////
//////
//////    @Insert(onConflict = REPLACE)
//////    public void insertFoodForSearch(FoodForSearch foodForSearch);
//
//    //
//////    @Query("SELECT * FROM FoodForNewSearch")
//////    public List<FoodForNewSearch> findAllFoodForNewSearch();
//////
//////
//    @Query("SELECT * FROM Post")
//    public List<Post> findAllFood();
//
//    @Query("SELECT * FROM Post Where title Like '%' || :search || '%'")
//    public List<Post> newSearch(String search);
//////
//////    @Query("SELECT * From FoodForSearch WHERE name IN (:search)")
//////    public List<FoodForSearch> search(List<String> search);
//
////
////
////    @Update
////    public void updateWishList(Food food);
////
////    @Update
////    public void deleteAllWishList(List<Food> foods);
////
////
////
//////    @Insert(onConflict = REPLACE)
//////    public void insertDataOffline(OfflineData offlineData);
//////
//////    @Query("SELECT * FROM OfflineData Where idQuery = :idQuery")
//////    public OfflineData getDataOffline(String idQuery);
//////
////////    @Query("SELECT * FROM OfflineData")
////////    public OfflineData findAllOfflineData();
//////
//////    @Query("DELETE FROM FoodForNewSearch")
//////    public void deleteAllFoodForNewSearch();
//////
//////    //===============
//////    @Query("DELETE FROM FoodWishList")
//////    public void deleteAllFoodWishList();
//////
//////    @Query("SELECT * FROM FoodWishList")
//////    public List<FoodWishList> findAllFoodWishList();
//////
//////    @Insert(onConflict = REPLACE)
//////    public void insertFoodWishList(FoodWishList foodWishList);
//////
//////    @Delete
//////    void deleteFoodWishList(FoodWishList foodWishList);
//
//    @Query("SELECT * FROM Post Where category_id = :idQuery")
//    public List<Post> getPostByCategoryId(String idQuery);
//
//
//    @Insert(onConflict = REPLACE)
//    public void insertRecent(Recent recent);
//
//    @Delete
//    public void deleteRecent(Recent recent);
//
//    @Query("DELETE FROM Recent")
//    public void deleteAllRecent();
//
//    @Query("SELECT * FROM Recent")
//    public List<Post> findAllRecent();
//
//    @Query("SELECT * FROM Recent Where uuid = :idQuery")
//    public Post getRecentByUUID(String idQuery);
//
//
//    @Insert(onConflict = REPLACE)
//    public void insertFavorite(Favorite favorite);
//
//    @Delete
//    public void deleteFavorite(Favorite favorite);
//
//    @Query("SELECT * FROM Favorite")
//    public List<Favorite> findAllFavorite();
//
//    @Query("SELECT * FROM Favorite Where uuid = :idQuery")
//    public Favorite checkFavorite(String idQuery);
//
//    @Query("SELECT * FROM Post Where id = :idQuery")
//    public Post getPost(String idQuery);
//
//    @Query("SELECT * FROM Post Where note != :idQuery")
//    public List<Post> getAllNote(String idQuery);
}

