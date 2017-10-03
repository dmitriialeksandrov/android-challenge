package com.dmitriialeksandrov.githubapp.di;

import com.dmitriialeksandrov.githubapp.screen.commits.CommitsActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {DataModule.class})
public interface AppComponent {

    void injectCommitsActivity(CommitsActivity commitsActivity);

}
