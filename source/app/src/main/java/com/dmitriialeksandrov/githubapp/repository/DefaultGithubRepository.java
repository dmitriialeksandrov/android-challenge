package com.dmitriialeksandrov.githubapp.repository;

import android.support.annotation.NonNull;

import com.dmitriialeksandrov.githubapp.api.GithubService;
import com.dmitriialeksandrov.githubapp.content.CommitResponse;
import com.dmitriialeksandrov.githubapp.rxutils.RxUtils;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import rx.Observable;

public class DefaultGithubRepository implements GithubRepository {

    private final GithubService githubService;

    public DefaultGithubRepository(@NonNull GithubService githubService) {
        this.githubService = githubService;
    }

    @NonNull
    @Override
    public Observable<List<CommitResponse>> githubCommits(int page) {
        return githubService.commits(page)
                .flatMap(commitResponses -> {

                    Realm.getDefaultInstance().executeTransaction(realm -> {
                        realm.delete(CommitResponse.class);
                        realm.insert(commitResponses);
                    });
                    return Observable.just(commitResponses);
                })
                .onErrorResumeNext(throwable -> {
                    Realm realm = Realm.getDefaultInstance();
                    RealmResults<CommitResponse> commitResponses = realm.where(CommitResponse.class).findAll();
                    return Observable.just(realm.copyFromRealm(commitResponses));
                })
                .compose(RxUtils.async());
    }

}
