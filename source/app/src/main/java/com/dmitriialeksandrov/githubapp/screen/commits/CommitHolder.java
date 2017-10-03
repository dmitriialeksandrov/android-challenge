package com.dmitriialeksandrov.githubapp.screen.commits;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dmitriialeksandrov.githubapp.R;
import com.dmitriialeksandrov.githubapp.content.Commit;
import com.dmitriialeksandrov.githubapp.content.CommitResponse;
import com.dmitriialeksandrov.githubapp.content.CommitResponseAuthor;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommitHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.avatar)
    ImageView avatarView;

    @BindView(R.id.commitName)
    TextView commitNameView;

    @BindView(R.id.commitAuthor)
    TextView commitAuthorView;

    public CommitHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(@NonNull CommitResponse commitResponse) {
        Commit commit = commitResponse.getCommit();
        commitNameView.setText(commit.getMessage());
        commitAuthorView.setText(commit.getAuthor().getAuthorName());
        CommitResponseAuthor commitResponseAuthor = commitResponse.getAuthor();
        if (commitResponseAuthor != null && commitResponseAuthor.getAvatarUrl() != null) {
            Picasso.with(avatarView.getContext()).load(commitResponseAuthor.getAvatarUrl()).fit()
                    .into(avatarView);
        }
    }
}
