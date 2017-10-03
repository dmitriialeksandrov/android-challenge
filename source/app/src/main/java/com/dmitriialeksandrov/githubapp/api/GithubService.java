package com.dmitriialeksandrov.githubapp.api;

import com.dmitriialeksandrov.githubapp.content.CommitResponse;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface GithubService {

    @GET("/repos/android/platform_build/commits")
    Observable<List<CommitResponse>> commits(@Query("page") int page);
}
