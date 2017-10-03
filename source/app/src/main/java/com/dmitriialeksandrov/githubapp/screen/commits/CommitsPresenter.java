package com.dmitriialeksandrov.githubapp.screen.commits;

import android.support.annotation.NonNull;

import com.dmitriialeksandrov.githubapp.R;
import com.dmitriialeksandrov.githubapp.content.CommitResponse;
import com.dmitriialeksandrov.githubapp.repository.GithubRepository;
import com.dmitriialeksandrov.githubapp.rxutils.LifecycleHandler;

public class CommitsPresenter {

    private final LifecycleHandler lifecyclerHandler;
    private final CommitsView view;
    private final GithubRepository repository;
    private int lastLoadedPage;

    public CommitsPresenter(@NonNull GithubRepository githubRepository,
                            @NonNull LifecycleHandler lifecycleHandler,
                            @NonNull CommitsView view) {
        repository = githubRepository;
        this.lifecyclerHandler = lifecycleHandler;
        this.view = view;
    }

    public void loadLatestCommits() {
        lastLoadedPage = 0;
        repository.githubCommits(lastLoadedPage)
                .doOnSubscribe(view::showLoading)
                .doOnTerminate(view::hideLoading)
                .compose(lifecyclerHandler.load(R.id.commits_request + lastLoadedPage))
                .subscribe(view::showCommits, throwable -> view.showError());
    }

    public void loadMoreCommits() {
        repository.githubCommits(lastLoadedPage++)
                .doOnSubscribe(view::showLoading)
                .doOnTerminate(view::hideLoading)
                .compose(lifecyclerHandler.load(R.id.commits_request + lastLoadedPage))
                .subscribe(view::addCommits, throwable -> view.showError());
    }

    public void onItemClick(CommitResponse commitResponse) {
        view.showCommitInfo(commitResponse);
    }
}
