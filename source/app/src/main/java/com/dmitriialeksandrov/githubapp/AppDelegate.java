package com.dmitriialeksandrov.githubapp;

import android.app.Application;
import android.support.annotation.NonNull;

import com.dmitriialeksandrov.githubapp.di.AppComponent;
import com.dmitriialeksandrov.githubapp.di.DaggerAppComponent;
import com.dmitriialeksandrov.githubapp.di.DataModule;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.rx.RealmObservableFactory;

public class AppDelegate extends Application {

    private static AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .rxFactory(new RealmObservableFactory())
                .build();
        Realm.setDefaultConfiguration(configuration);
        appComponent = DaggerAppComponent.builder()
                .dataModule(new DataModule())
                .build();
    }

    @NonNull
    public static AppComponent getAppComponent() {
        return appComponent;
    }
}
