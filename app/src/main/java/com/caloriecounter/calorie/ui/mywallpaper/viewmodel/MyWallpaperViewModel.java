package com.caloriecounter.calorie.ui.mywallpaper.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.caloriecounter.calorie.WeatherApplication;
import com.caloriecounter.calorie.model.Favorite;
import com.caloriecounter.calorie.model.Recent;
import com.caloriecounter.calorie.network.APIService;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class MyWallpaperViewModel extends ViewModel {
    private APIService repoRepository;
    private CompositeDisposable disposable;


    private MutableLiveData<List<Favorite>> repos = new MutableLiveData<>();
    private MutableLiveData<List<Recent>> reposRecent = new MutableLiveData<>();

    public MyWallpaperViewModel(APIService repoRepository) {
        this.repoRepository = repoRepository;
        disposable = new CompositeDisposable();
    }

    public MyWallpaperViewModel() {
        disposable = new CompositeDisposable();
    }

    public MutableLiveData<List<Favorite>> getRepos(){
        return repos;
    }

    public MutableLiveData<List<Recent>> getReposRecent(){
        return reposRecent;
    }

    public void getAllFavoriteImage() {
        disposable.add(WeatherApplication.get().getDatabase().foodDao().getAllFavorite().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableSingleObserver<List<Favorite>>() {
                    @Override
                    public void onSuccess(List<Favorite> favorites) {
                        repos.setValue(favorites);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                }));
    }

    public void getAllRecentImage() {
        disposable.add(WeatherApplication.get().getDatabase().foodDao().getAllRecent().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableSingleObserver<List<Recent>>() {
                    @Override
                    public void onSuccess(List<Recent> favorites) {
                        reposRecent.setValue(favorites);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                }));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (disposable != null) {
            disposable.clear();
            disposable = null;
        }
    }

}
