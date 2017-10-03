package com.dmitriialeksandrov.githubapp.rxutils;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import rx.Observable;

public class LoaderLifecycleHandler implements LifecycleHandler {

    private final Context context;
    private final LoaderManager loaderManager;

    /**
     * Creates a new instance of {@link LifecycleHandler}
     * You don't have to store it somewhere in a variable, since it has no state
     *
     * @param context       - typically it's your activity instance
     * @param loaderManager - loader manager of your activity or fragment
     * @return instance of LifecycleHandler
     */
    @NonNull
    public static LifecycleHandler create(@NonNull Context context, @NonNull LoaderManager loaderManager) {
        return new LoaderLifecycleHandler(context, loaderManager);
    }

    private LoaderLifecycleHandler(@NonNull Context context, @NonNull LoaderManager loaderManager) {
        this.context = context;
        this.loaderManager = loaderManager;
    }

    @NonNull
    @Override
    public <T> Observable.Transformer<T, T> load(@IdRes final int loaderId) {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(final Observable<T> observable) {
                if (loaderManager.getLoader(loaderId) == null) {
                    loaderManager.initLoader(loaderId, Bundle.EMPTY, new RxLoaderCallbacks<>(observable));
                }
                final RxLoader<T> loader = (RxLoader<T>) loaderManager.getLoader(loaderId);
                return loader.createObservable();
            }
        };
    }

    @NonNull
    @Override
    public <T> Observable.Transformer<T, T> reload(@IdRes final int loaderId) {
        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(final Observable<T> observable) {
                loaderManager.restartLoader(loaderId, Bundle.EMPTY, new RxLoaderCallbacks<>(observable));
                final RxLoader<T> loader = (RxLoader<T>) loaderManager.getLoader(loaderId);
                return loader.createObservable();
            }
        };
    }

    @Override
    public void clear(int id) {
        loaderManager.destroyLoader(id);
    }

    private class RxLoaderCallbacks<D> implements LoaderManager.LoaderCallbacks<D> {

        private final Observable<D> mObservable;

        public RxLoaderCallbacks(@NonNull Observable<D> observable) {
            mObservable = observable;
        }

        @NonNull
        @Override
        public Loader<D> onCreateLoader(int id, Bundle args) {
            return new RxLoader<>(context, mObservable);
        }

        @Override
        public void onLoadFinished(Loader<D> loader, D data) {
            // Do nothing
        }

        @Override
        public void onLoaderReset(Loader<D> loader) {
            // Do nothing
        }
    }
}
