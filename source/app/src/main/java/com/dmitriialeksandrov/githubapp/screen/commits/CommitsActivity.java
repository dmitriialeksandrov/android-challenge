package com.dmitriialeksandrov.githubapp.screen.commits;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.dmitriialeksandrov.githubapp.AppDelegate;
import com.dmitriialeksandrov.githubapp.R;
import com.dmitriialeksandrov.githubapp.content.CommitResponse;
import com.dmitriialeksandrov.githubapp.repository.GithubRepository;
import com.dmitriialeksandrov.githubapp.rxutils.LifecycleHandler;
import com.dmitriialeksandrov.githubapp.rxutils.LoaderLifecycleHandler;
import com.dmitriialeksandrov.githubapp.screen.general.LoadingDialog;
import com.dmitriialeksandrov.githubapp.screen.general.LoadingView;
import com.dmitriialeksandrov.githubapp.widget.BaseAdapter;
import com.dmitriialeksandrov.githubapp.widget.DividerItemDecoration;
import com.dmitriialeksandrov.githubapp.widget.EmptyRecyclerView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommitsActivity extends AppCompatActivity implements CommitsView, BaseAdapter.OnItemClickListener<CommitResponse>, CommitsAdapter.OnLoadMoreListener {

    private static final String COMMIT_DETAILS_TAG = "commit_details";

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recyclerView)
    EmptyRecyclerView recyclerView;

    @BindView(R.id.empty)
    View emptyView;

    @BindView(R.id.swipe_container)
    SwipeRefreshLayout swipeRefreshLayout;

    @Inject
    GithubRepository repository;

    private LoadingView loadingView;
    private CommitsAdapter adapter;
    private CommitsPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commits);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        AppDelegate.getAppComponent().injectCommitsActivity(this);

        loadingView = LoadingDialog.view(getSupportFragmentManager());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this));
        recyclerView.setEmptyView(emptyView);

        adapter = new CommitsAdapter(new ArrayList<>());
        adapter.attachToRecyclerView(recyclerView);
        adapter.setOnItemClickListener(this);
        adapter.setLoadMoreListener(this);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            presenter.loadLatestCommits();
            swipeRefreshLayout.setRefreshing(false);
        });
        LifecycleHandler lifecycleHandler = LoaderLifecycleHandler.create(this, getSupportLoaderManager());
        presenter = new CommitsPresenter(repository, lifecycleHandler, this);
        presenter.loadLatestCommits();
    }

    @Override
    public void showLoading() {
        loadingView.showLoading();
    }

    @Override
    public void hideLoading() {
        loadingView.hideLoading();
    }

    @Override
    public void showCommits(@NonNull List<CommitResponse> commits) {
        adapter.changeDataSet(commits);
    }

    @Override
    public void addCommits(@NonNull List<CommitResponse> commits) {
        adapter.add(commits);
    }

    @Override
    public void showCommitInfo(@NonNull CommitResponse commitResponse) {
        FragmentManager fm = getSupportFragmentManager();
        CommitDetailsDialog detailsDialog = CommitDetailsDialog.newInstance(commitResponse);
        detailsDialog.show(fm, COMMIT_DETAILS_TAG);
    }

    @Override
    public void showError() {
        Snackbar.make(recyclerView, R.string.error_commits, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onItemClick(@NonNull CommitResponse item) {
        presenter.onItemClick(item);
    }

    @Override
    public void onLoadMore() {
        presenter.loadMoreCommits();
    }
}
