package com.dmitriialeksandrov.githubapp.api;

import android.support.annotation.NonNull;

import com.dmitriialeksandrov.githubapp.BuildConfig;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

import static okhttp3.logging.HttpLoggingInterceptor.Level;

public final class LoggingInterceptor implements Interceptor {

    private final Interceptor loggingIntercepter;

    private LoggingInterceptor() {
        loggingIntercepter = new HttpLoggingInterceptor()
                .setLevel(BuildConfig.DEBUG ? Level.BODY : Level.NONE);
    }

    @NonNull
    public static Interceptor create() {
        return new LoggingInterceptor();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        return loggingIntercepter.intercept(chain);
    }

}
