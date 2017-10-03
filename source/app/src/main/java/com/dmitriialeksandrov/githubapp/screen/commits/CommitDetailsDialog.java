package com.dmitriialeksandrov.githubapp.screen.commits;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.dmitriialeksandrov.githubapp.R;
import com.dmitriialeksandrov.githubapp.content.Commit;
import com.dmitriialeksandrov.githubapp.content.CommitResponse;
import com.dmitriialeksandrov.githubapp.content.CommitResponseAuthor;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommitDetailsDialog extends DialogFragment {

    private final static String AUTHOR_NAME = "author_name";
    private final static String AUTHOR_AVATAR_URL = "avatar_url";
    private final static String COMMIT_MESSAGE = "commit_message";
    private final static String SHA = "sha";

    @BindView(R.id.commitAuthor)
    TextView commitAuthorView;

    @BindView(R.id.avatar)
    ImageView avatarView;

    @BindView(R.id.commitName)
    TextView commitMessageView;

    @BindView(R.id.commitSha)
    TextView commitShaView;

    public static CommitDetailsDialog newInstance(CommitResponse commitResponse) {
        CommitDetailsDialog fragment = new CommitDetailsDialog();
        Bundle bundle = new Bundle();
        Commit commit = commitResponse.getCommit();
        bundle.putString(AUTHOR_NAME, commit.getAuthor().getAuthorName());
        bundle.putString(COMMIT_MESSAGE, commit.getMessage());
        bundle.putString(SHA, commitResponse.getSha());
        CommitResponseAuthor commitResponseAuthor = commitResponse.getAuthor();
        if (commitResponseAuthor != null) {
            bundle.putString(AUTHOR_AVATAR_URL, commitResponseAuthor.getAvatarUrl());
        }
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_commit_details_dialog,
                container, false);
        ButterKnife.bind(this, rootView);
        Bundle args = getArguments();
        String authorName = args.getString(AUTHOR_NAME);
        String avatarUrl = args.getString(AUTHOR_AVATAR_URL, null);
        String commitMessage = args.getString(COMMIT_MESSAGE);
        String sha = args.getString(SHA);
        commitAuthorView.setText(authorName);
        commitMessageView.setText(commitMessage);
        commitMessageView.setMovementMethod(ScrollingMovementMethod.getInstance());
        commitShaView.setText(sha);
        if (avatarUrl != null) {
            Picasso.with(getContext()).load(avatarUrl).fit().into(avatarView);
        }
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Dialog);
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(layoutParams);
        layoutParams.dimAmount = 0.6f;
        window.setAttributes(layoutParams);
    }

    @OnClick(R.id.dismiss)
    public void onDismissClick() {
        dismiss();
    }
}