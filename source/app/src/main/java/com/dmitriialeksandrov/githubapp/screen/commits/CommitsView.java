package com.dmitriialeksandrov.githubapp.screen.commits;

import android.support.annotation.NonNull;

import com.dmitriialeksandrov.githubapp.content.Commit;
import com.dmitriialeksandrov.githubapp.content.CommitResponse;
import com.dmitriialeksandrov.githubapp.screen.general.LoadingView;

import java.util.List;

public interface CommitsView extends LoadingView {

    void showCommits(@NonNull List<CommitResponse> commitList);

    void addCommits(@NonNull List<CommitResponse> commitList);

    void showCommitInfo(@NonNull CommitResponse commitResponse);

    void showError();
}
