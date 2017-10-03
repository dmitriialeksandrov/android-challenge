package com.dmitriialeksandrov.githubapp.di;

import android.support.annotation.NonNull;

import com.dmitriialeksandrov.githubapp.BuildConfig;
import com.dmitriialeksandrov.githubapp.api.GithubService;
import com.dmitriialeksandrov.githubapp.api.LoggingInterceptor;
import com.dmitriialeksandrov.githubapp.repository.DefaultGithubRepository;
import com.dmitriialeksandrov.githubapp.repository.GithubRepository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class DataModule {

    @Provides
    @Singleton
    GithubRepository provideGithubRepository(
            @NonNull GithubService githubService) {
        return new DefaultGithubRepository(githubService);
    }

    @Provides
    @Singleton
    GithubService provideGithubService(@NonNull Retrofit retrofit) {
        return retrofit.create(GithubService.class);
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(@NonNull OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.API_ENDPOINT)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(LoggingInterceptor.create())
                .build();
    }

    @Provides
    @Singleton
    Realm provideRealm() {
        return Realm.getDefaultInstance();
    }

}
