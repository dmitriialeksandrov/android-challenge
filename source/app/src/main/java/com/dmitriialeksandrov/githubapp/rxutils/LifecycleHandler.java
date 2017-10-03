package com.dmitriialeksandrov.githubapp.rxutils;

import android.support.annotation.NonNull;

import rx.Observable;

public interface LifecycleHandler {

    @NonNull
    <T> Observable.Transformer<T, T> load(int id);

    @NonNull
    <T> Observable.Transformer<T, T> reload(int id);

    /**
     * This method clears subscriptions and destroys observable for the request with specified id
     *
     * @param id - unique identifier for request on Activity / Fragment
     */
    void clear(int id);
}
