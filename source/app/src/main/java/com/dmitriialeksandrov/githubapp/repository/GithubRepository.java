package com.dmitriialeksandrov.githubapp.repository;

import android.support.annotation.NonNull;

import com.dmitriialeksandrov.githubapp.content.CommitResponse;

import java.util.List;

import rx.Observable;

public interface GithubRepository {

    @NonNull
    Observable<List<CommitResponse>> githubCommits(int page);

}
