package com.dmitriialeksandrov.githubapp.rxutils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.Loader;

import rx.AsyncEmitter;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.MainThreadSubscription;
import rx.functions.Action1;

class RxLoader<D> extends Loader<D> {

    private Observable<D> observable;

    private AsyncEmitter<D> emitter;

    private Subscription subscription;

    @Nullable
    private D data;

    private boolean isErrorReported = false;

    @Nullable
    private Throwable error;

    private boolean isCompleted = false;

    public RxLoader(@NonNull Context context, @NonNull Observable<D> observable) {
        super(context);
        this.observable = observable;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        subscribe();
    }

    @Override
    protected void onStopLoading() {
        super.onStopLoading();
    }

    @Override
    protected void onReset() {
        clearSubscription();
        observable = null;
        data = null;
        error = null;
        isCompleted = false;
        isErrorReported = false;
        emitter = null;
        super.onReset();
    }

    private void subscribe() {
        if (emitter != null && subscription == null && !isCompleted && error == null) {
            subscription = observable.subscribe(new LoaderSubscriber());
        }
    }

    @NonNull
    Observable<D> createObservable() {
        return Observable.fromEmitter(new Action1<AsyncEmitter<D>>() {
            @Override
            public void call(AsyncEmitter<D> asyncEmitter) {
                emitter = asyncEmitter;
                emitter.setSubscription(new MainThreadSubscription() {
                    @Override
                    protected void onUnsubscribe() {
                        clearSubscription();
                    }
                });

                if (data != null) {
                    emitter.onNext(data);
                }
                if (isCompleted) {
                    emitter.onCompleted();
                } else if (error != null && !isErrorReported) {
                    emitter.onError(error);
                    isErrorReported = true;
                }

                subscribe();
            }
        }, AsyncEmitter.BackpressureMode.LATEST);
    }

    private void clearSubscription() {
        if (subscription != null) {
            subscription.unsubscribe();
            subscription = null;
        }
    }

    private class LoaderSubscriber extends Subscriber<D> {

        @Override
        public void onNext(D d) {
            data = d;
            if (emitter != null && isStarted()) {
                emitter.onNext(d);
            }
        }

        @Override
        public void onError(Throwable throwable) {
            error = throwable;
            if (emitter != null && isStarted()) {
                emitter.onError(throwable);
                isErrorReported = true;
            }
        }

        @Override
        public void onCompleted() {
            isCompleted = true;
            if (emitter != null && isStarted()) {
                emitter.onCompleted();
            }
        }
    }
}