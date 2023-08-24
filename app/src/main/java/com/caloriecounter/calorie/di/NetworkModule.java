package com.caloriecounter.calorie.di;

import com.caloriecounter.calorie.network.APIService;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.concurrent.TimeUnit;

import javax.inject.Qualifier;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;

import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class NetworkModule {
    @NormalRetrofit
    @Provides
    Retrofit provideCachedRetrofit() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(httpLoggingInterceptor);
        builder.connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(40, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS);
        return new Retrofit.Builder().baseUrl("https://wallpaper.eztechglobal.com/api/v1/")
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    @ProRetrofit
    @Provides
    Retrofit provideCRetrofit() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(httpLoggingInterceptor);
        builder.connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(40, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS);
        return new Retrofit.Builder().baseUrl("http://192.168.31.99/api/v1/")
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    @AccuRetrofit
    @Provides
    Retrofit provideAccuRetrofit() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(httpLoggingInterceptor);
        builder.connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(40, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS);
        return new Retrofit.Builder().baseUrl("http://www.geoplugin.net/")
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    @WeatherAPIRetrofit
    @Provides
    Retrofit provideWeatherRetrofit() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.addInterceptor(httpLoggingInterceptor);
        builder.connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(40, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS);
        return new Retrofit.Builder().baseUrl("https://api.weatherapi.com/v1/")
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    @NormalAPIService
    @Provides
    APIService provideAPIService(@NormalRetrofit Retrofit retrofit){
        return retrofit.create(APIService.class);
    }


    @ProAPIService
    @Provides
    APIService provideProAPIService(@ProRetrofit Retrofit retrofit){
        return retrofit.create(APIService.class);
    }

    @AccuAPIService
    @Provides
    APIService provideAccuAPIService(@AccuRetrofit Retrofit retrofit){
        return retrofit.create(APIService.class);
    }

    @WeatherAPIService
    @Provides
    APIService provideWeatherAPIService(@WeatherAPIRetrofit Retrofit retrofit){
        return retrofit.create(APIService.class);
    }






    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    public @interface NormalRetrofit {}

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ProRetrofit {}

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    public @interface AccuRetrofit {}

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    public @interface WeatherAPIRetrofit {}



    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    public @interface NormalAPIService {}

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ProAPIService {}

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    public @interface AccuAPIService {}

    @Qualifier
    @Retention(RetentionPolicy.RUNTIME)
    public @interface WeatherAPIService {}
}
